package co.instarecipe.routing;

import com.google.gson.JsonObject;

public class RouterPayload {
	public String resource;
	public String action;
	public JsonObject data;
}