package mc.zyntra.general.data.impl;

import mc.zyntra.general.Constant;
import mc.zyntra.general.account.statistics.Statistics;
import mc.zyntra.general.account.statistics.StatisticsType;
import mc.zyntra.general.Core;
import mc.zyntra.general.data.DataStatus;
import mc.zyntra.general.utils.json.JsonUtils;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DataStatusImpl implements DataStatus {

    private final MongoDatabase database = Core.getMongoStorage().getClient().getDatabase(Constant.MONGO_DATABASE_STATISTICS);

    @Override
    public Statistics load(UUID uniqueId, StatisticsType statisticsType) {
        MongoCollection<Document> collection = database.getCollection(statisticsType.getMongoCollection());
        Document found = collection.find(Filters.eq("uniqueId", uniqueId.toString())).first();
        if (found == null)
            return null;

        return Core.getGson().fromJson(Core.getGson().toJson(found), statisticsType.getStatusClass());
    }

    @Override
    public void save(Statistics statistics) {
        MongoCollection<Document> collection = database.getCollection(statistics.getStatisticsType().getMongoCollection());
        Document found = collection.find(Filters.eq("uniqueId", statistics.getUniqueId().toString())).first();

        if (found == null) {
            found = Document.parse(Core.getGson().toJson(statistics));
            collection.insertOne(found);
            return;
        }

        collection.updateOne(Filters.eq("uniqueId", statistics.getUniqueId().toString()),
                new Document("$set", Document.parse(Core.getGson().toJson(statistics))));
    }

    @Override
    public void update(Statistics statistics, String fieldName) {
        MongoCollection<Document> collection = database.getCollection(statistics.getStatisticsType().getMongoCollection());
        JsonObject tree = (JsonObject) JsonUtils.jsonTree(statistics);
        Document found = collection.find(Filters.eq("uniqueId", statistics.getUniqueId().toString())).first();
        if (tree.has(fieldName)) {
            final Object value = JsonUtils.elementToBson(tree.get(fieldName));
            if (found != null) {
                collection.updateOne(found, new Document("$set", new Document(fieldName, value)));
            }
        }
    }

    @Override
    public Collection<Statistics> ranking(StatisticsType statisticsType, String fieldName) {
        MongoCollection<Document> collection = database.getCollection(statisticsType.getMongoCollection());
        List<Statistics> list = new ArrayList<>();
        for (Document element : collection.find().sort(new Document(fieldName, -1)).limit(10)) {
            list.add(Core.getGson().fromJson(Core.getGson().toJson(element), statisticsType.getStatusClass()));
        }
        return list;
    }
}
