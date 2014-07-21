package com.ijoomer.common.classes;

/**
 * This Class Contains All Method Related To IjoomerUser.
 * 
 * @author tasol
 * 
 */
public class IjoomerUser {

	private static IjoomerUser userObject;

	private String name;
	private String status;
	private String thumb;
	private String avatar;
	private String viewCount;
	private String isProfileLike;
	private String likes;
	private String dislikes;
	private String liked;
	private String disliked;
	private String latitude;
	private String longitude;
	private String totalFriend;
	private String totalGroup;
	private String totalPhotos;
	private String totalVideos;

	private IjoomerUser() {
	}

	public static IjoomerUser getInstance() {
		if (userObject == null) {
			userObject = new IjoomerUser();
		}
		return userObject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getIsProfileLike() {
		return isProfileLike;
	}

	public void setIsProfileLike(String isProfileLike) {
		this.isProfileLike = isProfileLike;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public String getDislikes() {
		return dislikes;
	}

	public void setDislikes(String dislikes) {
		this.dislikes = dislikes;
	}

	public String getLiked() {
		return liked;
	}

	public void setLiked(String liked) {
		this.liked = liked;
	}

	public String getDisliked() {
		return disliked;
	}

	public void setDisliked(String disliked) {
		this.disliked = disliked;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public String getTotalFriend() {
		return totalFriend;
	}

	public void setTotalFriend(String totalFriend) {
		this.totalFriend = totalFriend;
	}

	public String getTotalGroup() {
		return totalGroup;
	}

	public void setTotalGroup(String totalGroup) {
		this.totalGroup = totalGroup;
	}

	public String getTotalPhotos() {
		return totalPhotos;
	}

	public void setTotalPhotos(String totalPhotos) {
		this.totalPhotos = totalPhotos;
	}

	public String getTotalVideos() {
		return totalVideos;
	}

	public void setTotalVideos(String totalVideos) {
		this.totalVideos = totalVideos;
	}
}
