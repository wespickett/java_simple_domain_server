package co.instarecipe.routing;

import co.instarecipe.data.*;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class Router {
	
	Map <String, DataProvider> routeHandler;
	
	public Router() {
		routeHandler = new HashMap<>();

		get("recipe", new RecipeData());
		get("homepage", new HomePageData());
	}

	private void get(String route, DataProvider dp) {
		routeHandler.put(route, dp);
	}

	RouterPayload routePayload(RouterPayload payload) {
		DataProvider dp = routeHandler.get(payload.resource);
		
		if (dp == null) {
			throw new NullPointerException("No dataprovider linked to this resource."); 
		} else {
			JsonObject responsePayload = dp.route(payload.action, payload.data);
			payload.data = responsePayload;
			return payload;
		}
	}
}
