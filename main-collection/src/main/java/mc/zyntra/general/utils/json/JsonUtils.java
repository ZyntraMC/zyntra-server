package mc.zyntra.general.utils.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mc.zyntra.general.Core;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.util.JSON;
import org.bson.BsonInvalidOperationException;
import org.bson.Document;

public class JsonUtils {

	public static <T> T jsonToObject(JsonElement json, Class<T> clas) {
		return Core.getGson().fromJson(json, clas);
	}
	
	public static <T> T jsonToObject(String json, Class<T> clas) {
		return Core.getGson().fromJson(json, clas);
	}
	
	public static JsonElement jsonTree(Object src) {
		return Core.getGson().toJsonTree(src);
	}

	public static Object elementToBson(JsonElement element) {
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isString()) {
				return primitive.getAsString();
			} else if (primitive.isNumber()) {
				return primitive.getAsNumber();
			} else if (primitive.isBoolean()) {
				return primitive.getAsBoolean();
			}
		}

		try {
			return Document.parse(Core.getGson().toJson(element));
		} catch (BsonInvalidOperationException ex) {
			return JSON.parse(Core.getGson().toJson(element));
		}
	}

	public static String elementToString(JsonElement element) {
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isString()) {
				return primitive.getAsString();
			}
		}
		return Core.getGson().toJson(element);
	}

	public static <T> T mapToObject(Map<String, String> map, Class<T> clazz) {
		JsonObject obj = new JsonObject();
		for (Entry<String, String> entry : map.entrySet()) {
			try {
				obj.add(entry.getKey(), Core.getJsonParser().parse(entry.getValue()));
			} catch (Exception e) {
				obj.addProperty(entry.getKey(), entry.getValue());
			}
		}
		return Core.getGson().fromJson(obj, clazz);
	}

	public static Map<String, String> objectToMap(Object src) {
		Map<String, String> map = new HashMap<>();
		JsonObject obj = (JsonObject) Core.getGson().toJsonTree(src);
		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			map.put(entry.getKey(), Core.getGson().toJson(entry.getValue()));
		}
		return map;
	}
}
