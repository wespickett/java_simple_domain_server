package co.instarecipe.data;

import com.google.gson.JsonObject;

@FunctionalInterface
public interface DataProvider {
	public JsonObject route(String action, JsonObject data);
}
