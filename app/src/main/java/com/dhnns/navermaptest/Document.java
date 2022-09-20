package com.dhnns.navermaptest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Document {

    @SerializedName("address_name")
    private String addressName;
    @SerializedName("category_group_code")
    private String categoryGroupCode;
    @SerializedName("category_group_name")
    private String categoryGroupName;
    @SerializedName("category_name")
    private String categoryName;
    @Expose
    private String distance;
    @Expose
    private String id;
    @Expose
    private String phone;
    @SerializedName("place_name")
    private String placeName;
    @SerializedName("place_url")
    private String placeUrl;
    @SerializedName("road_address_name")
    private String roadAddressName;
    @Expose
    private String x;
    @Expose
    private String y;

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getCategoryGroupCode() {
        return categoryGroupCode;
    }

    public void setCategoryGroupCode(String categoryGroupCode) {
        this.categoryGroupCode = categoryGroupCode;
    }

    public String getCategoryGroupName() {
        return categoryGroupName;
    }

    public void setCategoryGroupName(String categoryGroupName) {
        this.categoryGroupName = categoryGroupName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceUrl() {
        return placeUrl;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    public String getRoadAddressName() {
        return roadAddressName;
    }

    public void setRoadAddressName(String roadAddressName) {
        this.roadAddressName = roadAddressName;
    }

    @SerializedName("getX")
    public String getLongitude() {
        return x;
    }

    @SerializedName("setX")
    public void setLongitude(String x) {
        this.x = x;
    }

    @SerializedName("getY")
    public String getLatitude() {
        return y;
    }
    @SerializedName("setLongitude")
    public void setLatitude(String y) {
        this.y = y;
    }

}
