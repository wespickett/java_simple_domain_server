package co.instarecipe.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.instarecipe.db.PostgresHelper;
import co.instarecipe.model.Recipe;

public class HomePageData implements DataProvider {
	
	private Recipe[] getItems(int itemCount, int pageNum, String queryString) {
		Recipe[] videos = new Recipe[itemCount];
	
		String query = "SELECT * FROM recipe"; //TODO: make this based on some other value
		PreparedStatement pst;
		String[] searchStringPieces;
		
		try {
			
			if (queryString != null && !queryString.isEmpty()) {
				
				searchStringPieces = queryString.split("\\s+");
				
				if (searchStringPieces.length > 0) {
					query += " WHERE title ~* ?";
				}

				for (int i = 1; i < searchStringPieces.length; i++) {
					query += " AND title ~* ?";
				}
			} else {
				queryString = "";
				searchStringPieces = new String[0];
				
				if (pageNum == 1) {
					LocalDate firstDate = LocalDate.of(2017, Month.APRIL, 21);
					LocalDate secondDate = LocalDate.now();					
					Long days = ChronoUnit.DAYS.between(firstDate, secondDate);
					//page number from 1 to 9 based on date. changes homepage every day
					pageNum = (int) (days % 9 + 1);
				}
			}
			
			query += " LIMIT ? OFFSET ?";
			
			pst = PostgresHelper.getConnection().prepareStatement(query);
			
			int argCount = 0;
			for (int i = 0; i < searchStringPieces.length; i++) {
				pst.setString(++argCount, searchStringPieces[i]);
			}
			
			pst.setInt(++argCount, itemCount);
			pst.setInt(++argCount, itemCount * (pageNum - 1));
			
			int count = 0;
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Recipe v = new Recipe();
				v.populateFromDbRow(rs);
				videos[count++] = v;
			}
			    
			rs.close();
			pst.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return videos;
	}
	
	@Override
	public JsonObject route(String action, JsonObject data) {
		JsonObject returnData = null;
		
		switch (action) {
		case "GET":
			JsonElement jePage = data.get("page");
			JsonElement jeQuery = data.get("query");
			int pageNum = 1;
			String queryString = null;
			
			if (jePage != null) {
				pageNum = jePage.getAsInt();
			}
			
			if (jeQuery != null) {
				queryString = jeQuery.getAsString();
			}
			
			//TODO cache this for a certain time period
			Recipe[] videos = getItems(100, pageNum, queryString);
			
			JsonArray videoArrayJson = new JsonArray(); 
			for (int i = 0; i < videos.length; i++) {
				Recipe curVid = videos[i];
				if (curVid != null) {
					videoArrayJson.add(curVid.toJson());
				}
			}
			returnData = new JsonObject();
			returnData.add("items", videoArrayJson);
		}

		return returnData;
	}

}
