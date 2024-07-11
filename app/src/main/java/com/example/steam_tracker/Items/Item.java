package com.example.steam_tracker.Items;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Item {
    private int steamItemsId;
    private String name;
    private byte[] imageBytes;
    private boolean imageUpdated;
    private String imageUrl;

    private static ArrayList<Item> importedItemsArray = new ArrayList<>();

    public Item(int id, String name, byte[] imageBytes, boolean imageUpdated, String imageUrl) {
        this.steamItemsId = id;
        this.name = name;
        this.imageBytes = imageBytes;
        this.imageUrl = imageUrl;
        this.imageUpdated = imageUpdated;
    }

    public Item()
    {
    }

    public void addItemsIntoArray(ResultSet resultData) {
        try {
            importedItemsArray.clear();
            while(resultData != null && resultData.next()) {
                Item item = new Item();
                item.setSteamItemsId(Integer.parseInt(resultData.getString("item_id")));
                item.setName(resultData.getString("name"));
                item.setImageUrl(resultData.getString("image_url"));
                item.setImageUpdatedStatus(false);
                importedItemsArray.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSteamItemsId() {
        return this.steamItemsId;
    }

    public void setSteamItemsId(int steamItemsId) {
        this.steamItemsId = steamItemsId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public byte[] getImageBytes() {
        return this.imageBytes;
    }

    public boolean isImageUpdated() {
        return this.imageUpdated;
    }

    public void setImageUpdatedStatus(boolean imageUpdated) {
        this.imageUpdated = imageUpdated;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<Item> getItemsArray() {
        return importedItemsArray;
    }
}
