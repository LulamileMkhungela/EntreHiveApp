package com.example.academy_intern.sampledesign.Model;

public class ItemProfile
{
    private String itemName;
    private int itemPrice;
    private String dateTime;

    public ItemProfile()
    {

    }

    public ItemProfile(String itemName, int itemPrice, String dateTime)
    {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.dateTime = dateTime;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
