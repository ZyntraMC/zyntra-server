package mc.zyntra.general.utils.mojang;

import mc.zyntra.general.Core;
import mc.zyntra.general.utils.Validator;
import mc.zyntra.general.utils.http.HttpRequest;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MojangAPI {

    private final JsonParser jsonParser = new JsonParser();

    private final LoadingCache<String, Optional<UUID>> uuidCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.HOURS)
            .build(new CacheLoader<String, Optional<UUID>>() {
                @Override
                @ParametersAreNonnullByDefault
                public Optional<UUID> load(String key) throws Exception {
                    return Optional.ofNullable(requestUUID(key));
                }
            });

    private final LoadingCache<UUID, Optional<String>> nameCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.HOURS)
            .build(new CacheLoader<UUID, Optional<String>>() {
                @Override
                @ParametersAreNonnullByDefault
                public Optional<String> load(UUID key) throws Exception {
                    return Optional.ofNullable(requestName(key));
                }
            });

    private final LoadingCache<UUID, Optional<Property>> texturesCache = CacheBuilder.newBuilder()
            .maximumSize(2000)
            .expireAfterWrite(1L, TimeUnit.HOURS)
            .build(new CacheLoader<UUID, Optional<Property>>() {
                @Override
                @ParametersAreNonnullByDefault
                public Optional<Property> load(UUID uuid) throws Exception {
                    return Optional.ofNullable(requestTextures(uuid));
                }
            });

    private Property requestTextures(UUID uuid) throws Exception {
        try {
            HttpRequest request = HttpRequest.get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false")
                    .connectTimeout(5000)
                    .readTimeout(5000)
                    .userAgent("Zyntra/1.0.0")
                    .accept(HttpRequest.CONTENT_TYPE_JSON);

            if (request.ok()) {
                JsonObject json = request.json().getAsJsonObject();
                JsonObject prop = json.getAsJsonArray("properties").get(0).getAsJsonObject();
                return Core.getGson().fromJson(prop, Property.class);
            }

            return null;
        } catch (HttpRequest.HttpRequestException e) {
            throw new Exception(e);
        }
    }

    private UUID requestUUID(String name) throws Exception {
        try {
            HttpRequest request = HttpRequest.get("https://api.mojang.com/users/profiles/minecraft/" + name)
                    .connectTimeout(5000)
                    .readTimeout(5000)
                    .userAgent("Zyntra/1.0.0")
                    .acceptJson();

            if (request.ok()) {
                JsonObject object = jsonParser.parse(request.reader()).getAsJsonObject();
                String id = object.get("id").getAsString();
                return UUID.fromString(id.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
            } else if (request.noContent()) {
                return null;
            }

            throw new MojangException(MojangException.ErrorType.UNKNOWN);
        } catch (HttpRequest.HttpRequestException e) {
            throw new Exception(e);
        }
    }

    private String requestName(UUID uuid) throws Exception {
        String rawUUID = uuid.toString().replace("-", "");

        try {
            HttpRequest request = HttpRequest.get("https://api.mojang.com/user/profiles/" + rawUUID + "/names")
                    .connectTimeout(5000)
                    .readTimeout(5000)
                    .userAgent("Zyntra/1.0.0")
                    .acceptJson();

            if (request.ok()) {
                JsonArray jsonArray = jsonParser.parse(request.reader()).getAsJsonArray();
                JsonObject jsonObject = jsonArray.get(jsonArray.size() - 1).getAsJsonObject();
                return jsonObject.get("name").getAsString();
            } else if (request.noContent()) return null;

            throw new MojangException(MojangException.ErrorType.UNKNOWN);
        } catch (HttpRequest.HttpRequestException e) {
            throw new Exception(e);
        }
    }

    public UUID getUUID(String name) throws Exception {
        if (Validator.isNickname(name)) {
            return uuidCache.get(name.toLowerCase()).orElse(null);
        } else {
            throw new MojangException(MojangException.ErrorType.INVALID_NICKNAME);
        }
    }

    public String getName(UUID uuid) throws Exception {
        Objects.requireNonNull(uuid, "UUID cannot be null");
        return nameCache.get(uuid).orElse(null);
    }

    public Property getTextures(UUID uuid) throws Exception {
        Objects.requireNonNull(uuid, "UUID cannot be null");
        return texturesCache.get(uuid).orElse(null);
    }

    public void putUUID(String name, UUID uuid) {
        uuidCache.put(name.toLowerCase(), Optional.of(uuid));
    }

    public void putName(UUID uuid, String name) {
        nameCache.put(uuid, Optional.of(name));
    }

    public void dump() {

    }
}
