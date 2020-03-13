package com.codingnew.rentbuysell;

public class product_part {
    public String mode;
    public String imageUrl;
    public  String mobileNo;
    public String category;
    public String name;
    public String price;
    public String description;
    public String uid;
    public String duration_of_rent;
    public String parentid;
    public String myid;
    public int myidint;
    public String public_feed;


    public product_part() {

    }

    public product_part(String mode, String imageUrl, String mobileNo, String category, String name, String price, String description, String uid, String duration_of_rent, String parentid, String myid, int myidint,String public_feed) {
        this.mode = mode;
        this.imageUrl = imageUrl;
        this.mobileNo = mobileNo;
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.uid = uid;
        this.duration_of_rent = duration_of_rent;
        this.parentid = parentid;
        this.myid = myid;
        this.myidint = myidint;
        this.public_feed=public_feed;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDuration_of_rent() {
        return duration_of_rent;
    }

    public void setDuration_of_rent(String duration_of_rent) {
        this.duration_of_rent = duration_of_rent;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public int getMyidint() {
        return myidint;
    }

    public void setMyidint(int myidint) {
        this.myidint = myidint;
    }

    public String getPublic_feed() {
        return public_feed;
    }

    public void setPublic_feed(String public_feed) {
        this.public_feed = public_feed;
    }
}
