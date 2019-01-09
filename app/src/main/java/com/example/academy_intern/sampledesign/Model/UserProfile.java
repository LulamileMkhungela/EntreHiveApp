package com.example.academy_intern.sampledesign.Model;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile
{

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("identityNumber")
    @Expose
    private String identityNumber;

    @SerializedName("name")
    @Expose
    private String name;

//    @SerializedName("surname")
//    @Expose
//    private String surname;

    @SerializedName("companyName")
    @Expose
    private String companyName;

    @SerializedName("companyNumber")
    @Expose
    private String companyNumber;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("username")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("resetToken")
    @Expose
    private String resetToken;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("points")
    @Expose
    private int points;

    @SerializedName("totalPoints")
    @Expose
    private int totalPoints;

    @SerializedName("usedPoints")
    @Expose
    private int usedPoints;

    @SerializedName("dateTime")
    @Expose
    private String dateTime;

    @SerializedName("photoPath")
    @Expose
    private String photoPath;

    @SerializedName("userRole")
    @Expose
    private boolean userRole;

    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;

    public UserProfile(String identityNumber, String name, String email, String password) {
        super();
        this.identityNumber = identityNumber;
        this.name = name;
        this.email = email;
        this.password = password;
//        this.dateTime = getCurrentTimeUsingCalendar();
        this.active = false;
    }

    public UserProfile(int userId, String name, int points)
    {
        this.userId = userId;
        this.name = name;
        this.points = points;
    }

    public UserProfile() {

    }

    public UserProfile(int userId) {
        this.userId = userId;
    }

    public UserProfile(int userId, String identityNumber, String name, String email, String phoneNumber, String companyName, String companyNumber)
    {
        this.userId = userId;
        this.identityNumber = identityNumber;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.companyNumber = companyNumber;
    }

    public UserProfile(int userId, String name, String email, String password, int points,
                       String dateTime, boolean active, boolean userRole) {
        super();
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.points = points;
        this.dateTime = dateTime;
        this.active = active;
        this.userRole = userRole;
    }

    public UserProfile(int userId, boolean active, String identityNumber, String name,
                       String companyName, String companyNumber, String address, String email, String phoneNumber,
                       String password, String resetToken, int points, String dateTime, String photoPath, boolean userRole) {
        this.userId = userId;
        this.active = active;
        this.identityNumber = identityNumber;
        this.name = name;
        this.companyName = companyName;
        this.companyNumber = companyNumber;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.resetToken = resetToken;
        this.points = points;
        this.dateTime = dateTime;
        this.photoPath = photoPath;
        this.userRole = userRole;
    }

    public boolean isUserRole() { return userRole; }

    public void setUserRole(boolean userRole) { this.userRole = userRole; }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public void setUsedPoints(int usedPoints) {
        this.usedPoints = usedPoints;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
