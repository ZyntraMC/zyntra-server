package mc.zyntra.backend.nosql;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

@Getter
public class MongoConnection {

    @Getter
    private MongoDatabase database;

    @Getter
    private MongoClient client;

    public void connect(String connectionURI, String db) {
        MongoClientURI uri = new MongoClientURI(connectionURI);
        client = new MongoClient(uri);
        database = client.getDatabase(db);
    }

    public void close() {
        client.close();
    }

}