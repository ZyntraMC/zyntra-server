package mc.zyntra.backend.packet;

import mc.zyntra.general.Core;
import lombok.Getter;
import redis.clients.jedis.*;

@Getter
public class RedisConnection {

    private String hostname;
    private String password;
    private int port;

    private JedisPool pool;

    public void connect(String hostname, String password, int port) {
        this.hostname = hostname;
        this.password = password;
        this.port = port;

        if (!this.password.isEmpty())
            pool = new JedisPool(new JedisPoolConfig(), hostname, port, 0,
                    password);
        else {
            pool = new JedisPool(new JedisPoolConfig(), hostname, port, 0);
        }
    }

    public void publish(String channel, String message) {
        try (Jedis j = this.pool.getResource()) {
            Pipeline pipeline = j.pipelined();
            pipeline.publish(channel, message);
            pipeline.sync();
        }
    }

    public void close() {
        if (this.pool != null) {
            this.pool.close();
        }
    }

    public boolean isConnected() {
        return this.pool != null && !this.pool.isClosed();
    }

    public void registerPubSub(JedisPubSub pubSub, String... channels) {
        Core.getPlatform().runAsync(new RedisConnection.PubSubTask(pubSub, channels));
    }

    public class PubSubTask implements Runnable {

        private final JedisPubSub jpsh;
        private final String[] channels;

        public PubSubTask(JedisPubSub s, String... channels) {
            this.jpsh = s;
            this.channels = channels;
        }

        @Override
        public void run() {
            boolean broken = false;
            try (Jedis rsc = pool.getResource()) {
                try {
                    rsc.subscribe(jpsh, channels);
                } catch (Throwable e) {
                    e.printStackTrace();
                    try {
                        jpsh.unsubscribe();
                    } catch (Throwable e1) {
                    }
                    broken = true;
                }
            }
            if (broken) {
                run();
            }
        }

        public void addChannel(String... channel) {
            jpsh.subscribe(channel);
        }

        public void removeChannel(String... channel) {
            jpsh.unsubscribe(channel);
        }

        public void poison() {
            jpsh.unsubscribe();
        }
    }
}
