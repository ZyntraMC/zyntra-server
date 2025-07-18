package mc.zyntra.general.data.impl;

import mc.zyntra.general.Core;
import mc.zyntra.general.data.DataServer;
import mc.zyntra.general.networking.PacketServerInfo;
import mc.zyntra.general.networking.Payload;
import mc.zyntra.general.server.ServerConfiguration;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.server.types.MinigameServerConfiguration;
import mc.zyntra.general.utils.json.JsonUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class DataServerImpl implements DataServer {

    @Getter
    private ServerConfiguration localServer;

    public DataServerImpl(String serverName, ServerType serverType, int maxPlayers) {
        switch (serverType) {
            case ESCONDE_ESCONDE: {
                localServer = new MinigameServerConfiguration(serverName, serverType);
                break;
            }
            default: {
                localServer = new ServerConfiguration(serverName, serverType);
                break;
            }
        }
    }

    @Override
    public void updateServer() {
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            jedis.hmset("server:" + localServer.getName(), JsonUtils.objectToMap(localServer));
        }

        PacketServerInfo packet = new PacketServerInfo(localServer, PacketServerInfo.Action.UPDATE);
        Core.getRedisBackend().publish(Payload.SERVER_INFO.name(), Core.getGson().toJson(packet));
    }

    @Override
    public void startServer() {
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            jedis.hmset("server:" + localServer.getName(), JsonUtils.objectToMap(localServer));
            jedis.sadd("server:type:" + localServer.getServerType().name().toLowerCase(), localServer.getName());
        }

        PacketServerInfo packet = new PacketServerInfo(localServer, PacketServerInfo.Action.START);
        Core.getRedisBackend().publish(Payload.SERVER_INFO.name(), Core.getGson().toJson(packet));
    }

    @Override
    public void stopServer() {
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            jedis.del("server:" + localServer.getName());
            jedis.srem("server:type:" + localServer.getServerType().name().toLowerCase(), localServer.getName());
        }

        PacketServerInfo packet = new PacketServerInfo(localServer, PacketServerInfo.Action.STOP);
        Core.getRedisBackend().publish(Payload.SERVER_INFO.name(), Core.getGson().toJson(packet));
    }

    @Override
    public ServerConfiguration getServer(String name) {
        ServerConfiguration serverConfiguration;
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            serverConfiguration = JsonUtils.mapToObject(jedis.hgetAll("server:" + name), ServerConfiguration.class);
        }
        return serverConfiguration;
    }

    @Override
    public MinigameServerConfiguration getMinigameServer(String name) {
        MinigameServerConfiguration minigameServer;
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            minigameServer = JsonUtils.mapToObject(jedis.hgetAll("server:" + name), MinigameServerConfiguration.class);
        }
        return minigameServer;
    }

    @Override
    public int getOnlineCount(String name) {
        return getServer(name).getOnlineCount();
    }

    @Override
    public int getOnlineCount(ServerType... serverTypes) {
        int onlineCount = 0;

        for (ServerType serverType : serverTypes)
            for (ServerConfiguration serverConfiguration : getServers(serverType))
                onlineCount += serverConfiguration.getOnlineCount();

        return onlineCount;
    }

    @Override
    public Set<ServerConfiguration> getServers(ServerType serverType) {
        Set<ServerConfiguration> servers = new HashSet<>();
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            for (String server : jedis.sunion("server:type:" + serverType.toString().toLowerCase())) {
                Map<String, String> map = jedis.hgetAll("server:" + server);
                ServerConfiguration serverConfiguration = JsonUtils.mapToObject(map, ServerConfiguration.class);
                servers.add(serverConfiguration);
            }
        }
        return servers;
    }

    @Override
    public Set<MinigameServerConfiguration> getMinigameServers(ServerType serverType) {
        Set<MinigameServerConfiguration> servers = new HashSet<>();
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            for (String server : jedis.sunion("server:type:" + serverType.toString().toLowerCase())) {
                Map<String, String> map = jedis.hgetAll("server:" + server);
                MinigameServerConfiguration minigameServer = JsonUtils.mapToObject(map, MinigameServerConfiguration.class);
                servers.add(minigameServer);
            }
        }
        return servers;
    }

    @Override
    public Set<ServerConfiguration> getServers() {
        Set<ServerConfiguration> servers = new HashSet<>();
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            String[] str = new String[ServerType.values().length];
            for (int i = 0; i < ServerType.values().length; i++) {
                str[i] = "server:type:" + ServerType.values()[i].toString().toLowerCase();
            }
            for (String server : jedis.sunion(str)) {
                Map<String, String> map = jedis.hgetAll("server:" + server);
                ServerConfiguration serverConfiguration = JsonUtils.mapToObject(map, ServerConfiguration.class);
                servers.add(serverConfiguration);
            }
        }
        return servers;
    }
}