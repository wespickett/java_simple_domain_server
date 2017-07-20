package co.instarecipe.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.instarecipe.db.PostgresHelper;
import co.instarecipe.model.Recipe;

public class RecipeData implements DataProvider {
	
	int ID_LENGTH = 6;
	char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
	
	public Recipe getItem(String urlId) {
		
		Recipe r = new Recipe();
		
		try {
			PreparedStatement pst = PostgresHelper.getConnection().prepareStatement("SELECT * FROM recipe WHERE urlId = ? LIMIT 1");
			pst.setString(1, urlId);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				r.populateFromDbRow(rs);
			}
			    
			rs.close();
			pst.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public Recipe getRandomItem() {
		Recipe r = new Recipe();
		
		try {
			PreparedStatement pst = PostgresHelper.getConnection().prepareStatement("SELECT * FROM recipe ORDER BY random() LIMIT 1");
			
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				r.populateFromDbRow(rs);
			}
			    
			rs.close();
			pst.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public Recipe[] getItems(String searchString) {
		
		String query = "SELECT * FROM recipe WHERE title ~* ?";
		String[] searchStringPieces = query.split("\\s+");
		
		Recipe[] recipesToReturn = new Recipe[searchStringPieces.length];
		
		for (String s : searchStringPieces) {
			query += " AND title ~* ?";
		}
		
		try {
			PreparedStatement pst = PostgresHelper.getConnection().prepareStatement(query);
			for (int i = 0; i < searchStringPieces.length; i++) {
				pst.setString(i, searchStringPieces[i]);
			}
			
			ResultSet rs = pst.executeQuery();
			int count = 0;
			while (rs.next()) {
				Recipe r = new Recipe();
				r.populateFromDbRow(rs);
				recipesToReturn[count++] = r;
			}
			
			rs.close();
			pst.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return recipesToReturn;
	}
	
	private String generateRecipeUrlId() {
		char[] id = new char[ID_LENGTH];
		Random randomGenerator = new Random();

	    int count = -1;
	    while (++count < 6) {
	    	int randomInt = randomGenerator.nextInt(alphabet.length);
	    	id[count] =  alphabet[randomInt];
	    }
	    
	    String newId = new String(id);
	    
	    return newId;
	}
	
	public String urlEncodeddTitle(String title) {
		String urlEncodedTitle = "";
		urlEncodedTitle = title.toLowerCase().replaceAll("[^a-z0-9_]", "_");
		urlEncodedTitle = urlEncodedTitle.replaceAll("_+", "_");
		
		if (urlEncodedTitle.length() > 80) {
			urlEncodedTitle = urlEncodedTitle.substring(0, 80);
		}

		if (urlEncodedTitle.charAt(urlEncodedTitle.length() - 1) == '_') {
			urlEncodedTitle = urlEncodedTitle.substring(0, urlEncodedTitle.length() - 1);
		}

		return urlEncodedTitle;
	}

	public JsonObject route(String action, JsonObject data) {
		
		JsonObject returnData = null;
		
		switch (action) {
		case "GET":
			JsonElement je = data.get("id");
			JsonElement jeRandom = data.get("random");
			String urlId = "";
			Boolean random = false;
			Recipe r = null;
			
			if (je != null) {
				urlId = je.getAsString();
				r = getItem(urlId);
			} else if (jeRandom != null) {
				random = jeRandom.getAsBoolean();
				if (random) {
					r = getRandomItem();
				}
			}
			 
			returnData = r.toJson();
		}

		return returnData;
	}
	
	
}
