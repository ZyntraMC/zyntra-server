package mc.zyntra.general.data.impl;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.data.DataPlayer;
import mc.zyntra.general.networking.PacketOutUpdateAccountField;
import mc.zyntra.general.networking.Payload;
import mc.zyntra.general.utils.json.JsonUtils;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;
import java.util.function.Consumer;

public class DataPlayerImpl implements DataPlayer {

    private final MongoCollection<Document> collection = Core.getMongoStorage().getDatabase().getCollection(Constant.MONGO_COLLECTION_ACCOUNTS);

    @Override
    public ZyntraPlayer create(UUID uniqueId, String name) {
        ZyntraPlayer zyntraPlayer = new ZyntraPlayer(uniqueId, name);
        Document found = collection.find(Filters.eq("uniqueId", zyntraPlayer.getUniqueId().toString())).first();

        if (found == null) {
            collection.insertOne(Document.parse(Core.getGson().toJson(zyntraPlayer)));
        }

        return zyntraPlayer;
    }

    @Override
    public void update(ZyntraPlayer zyntraPlayer, String fieldName) {
        JsonObject tree = (JsonObject) JsonUtils.jsonTree(zyntraPlayer);
        Document found = collection.find(Filters.eq("uniqueId", zyntraPlayer.getUniqueId().toString())).first();
        if (tree.has(fieldName)) {
            final Object value = JsonUtils.elementToBson(tree.get(fieldName));
            if (found != null) {
                collection.updateOne(found, new Document("$set", new Document(fieldName, value)));
            }
        }

        PacketOutUpdateAccountField packet = new PacketOutUpdateAccountField(zyntraPlayer.getUniqueId(), zyntraPlayer, fieldName);
        Core.getRedisBackend().publish(Payload.UPDATE_ACCOUNT_FIELD.name(), Core.getGson().toJson(packet));
    }

    @Override
    public void save(ZyntraPlayer zyntraPlayer) {
        if (!exists(zyntraPlayer.getUniqueId()))
            return;

        collection.updateOne(Filters.eq("uniqueId", zyntraPlayer.getUniqueId().toString()),
                new Document("$set", Document.parse(Core.getGson().toJson(zyntraPlayer))));
    }

    @Override
    public boolean exists(UUID uniqueId) {
        Document found = collection.find(Filters.eq("uniqueId", uniqueId.toString())).first();
        return found != null;
    }


    @Override
    public boolean exists(String name) {
        Document found = collection.find(Filters.eq("name", name)).first();
        return found != null;
    }

    @Override
    public ZyntraPlayer load(UUID uniqueId) {
        Document found = collection.find(Filters.eq("uniqueId", uniqueId.toString())).first();
        return found == null ? null : Core.getGson().fromJson(Core.getGson().toJson(found), ZyntraPlayer.class);
    }

    @Override
    public ZyntraPlayer load(String name) {
        Document found = collection.find(Filters.eq("name", name)).first();
        return found == null ? null : Core.getGson().fromJson(Core.getGson().toJson(found), ZyntraPlayer.class);
    }

    @Override
    public Collection<ZyntraPlayer> getPlayers() {
        List<ZyntraPlayer> players = new ArrayList<>();
        collection.find().forEach((Consumer<? super Document>) document -> players.add(Core.getGson().fromJson(Core.getGson().toJson(document), ZyntraPlayer.class)));
        return players;
    }

    public Collection<ZyntraPlayer> ranking(String fieldName) {
        List<ZyntraPlayer> players = new ArrayList<>();
        collection.find().sort(new Document(fieldName, -1)).limit(10).forEach((Consumer<? super Document>) element ->
                players.add(Core.getGson().fromJson(Core.getGson().toJson(element), ZyntraPlayer.class)));
        return players;
    }
}