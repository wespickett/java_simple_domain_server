package co.instarecipe.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public class Recipe extends Model {

	private int recipeId;
	private String videoType;
	private String videoUrl;
	private String duration;
	private String posterImg;
	private String title;
	private String urlId;
	private String urlEncodedTitle;

	public String getUrlEncodedTitle() {
		return urlEncodedTitle;
	}
	public void setUrlEncodedTitle(String urlEncodedTitle) {
		this.urlEncodedTitle = urlEncodedTitle;
	}
	public String getUrlId() {
		return urlId;
	}
	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}
	
	public int getRecipeId() {
		return recipeId;
	}
	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getPosterImg() {
		return posterImg;
	}
	public void setPosterImg(String posterImg) {
		this.posterImg = posterImg;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public JsonObject toJson() {
		JsonObject jsObj = new JsonObject();
		
		jsObj.addProperty("recipeId", getRecipeId());
		jsObj.addProperty("videoType", getVideoType());
		jsObj.addProperty("videoUrl", getVideoUrl());
		jsObj.addProperty("duration", getDuration());
		jsObj.addProperty("posterImg", getPosterImg());
		jsObj.addProperty("title", getTitle());
		jsObj.addProperty("urlId", getUrlId());
		jsObj.addProperty("urlEncodedTitle", getUrlEncodedTitle());
		
		return jsObj;
	}
	public void populateFromDbRow(ResultSet rs) throws SQLException {
		setRecipeId(rs.getInt(1));
		setVideoType(rs.getString(2));
		setVideoUrl(rs.getString(3));
		setDuration(rs.getString(4));
		setPosterImg(rs.getString(5));
		setTitle(rs.getString(6));
		setUrlId(rs.getString(7));
		setUrlEncodedTitle(rs.getString(8));
	}
	
}
