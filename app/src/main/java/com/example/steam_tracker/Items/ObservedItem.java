package com.example.steam_tracker.Items;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ObservedItem extends Item {

    private int observedId;
    private float price;
    private float priceTrend;
    private ArrayList<Float> priceArray;
    private ArrayList<String> logDateArray;

    private static ArrayList<ObservedItem> observedItemsArray = new ArrayList<>();

    public ObservedItem(int steamItemId, String name, byte[] imageBytes, boolean imageUpdated, String imageUrl, int observedId, float price, float priceTrend, ArrayList<Float> priceArray, ArrayList<String> logDateArray) {
        super(steamItemId,name,imageBytes,imageUpdated,imageUrl);
        this.observedId = observedId;
        this.price = price;
        this.priceTrend = priceTrend;
        this.priceArray = priceArray;
        this.logDateArray = logDateArray;
    }

    public ObservedItem() {

    }
    @Override
    public void addItemsIntoArray(ResultSet resultData)
    {
        try {
            observedItemsArray.clear();
            while(resultData != null && resultData.next())
            {
                ObservedItem observedItem = new ObservedItem();
                try {
                    observedItem.setImageUpdatedStatus(false);
                    observedItem.setSteamItemsId(Integer.parseInt(resultData.getString("item_id")));
                    observedItem.setObservedId(Integer.parseInt(resultData.getString("observed_id")));
                    observedItem.setName(resultData.getString("name"));
                    observedItem.setImageUrl(resultData.getString("image_url"));
                    observedItem.setPrice(Float.parseFloat(resultData.getString("price").replaceAll("\\s","").replace("zł", "").replaceAll(",", ".")));
                }
                // when cannot get an item price
                catch (NullPointerException n)
                {
                    observedItem.setPrice(0.00f);
                }
                finally {
                    observedItemsArray.add(observedItem);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setItemResults(ResultSet resultData, ObservedItem observedItem)
    {
        ArrayList<Float> newPriceArray = new ArrayList<>();
        ArrayList<String> newLogDateArray = new ArrayList<>();
        try {
        while(resultData != null && resultData.next()) {
            newPriceArray.add(Float.parseFloat(resultData.getString("price").replaceAll("\\s","").replace("zł","").replaceAll(",", ".")));
            newLogDateArray.add(resultData.getString("log_date"));
        }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        observedItem.setPriceArray(newPriceArray);
        observedItem.setLogDateArray(newLogDateArray);
    }

    public ArrayList<ObservedItem> getObservedItemsArray()
    {
        return observedItemsArray;
    }

    public int getObservedId() {
        return this.observedId;
    }

    public void setObservedId(int observedId) {
        this.observedId = observedId;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPriceTrend() {
        return this.priceTrend;
    }

    public void setPriceTrend(float priceTrend) {
        this.priceTrend = priceTrend;
    }

    public ArrayList<Float> getPriceArray() {
        return this.priceArray;
    }

    public void setPriceArray(ArrayList<Float> priceArray) {
        this.priceArray = priceArray;
    }

    public ArrayList<String> getLogDateArray() {
        return this.logDateArray;
    }

    public void setLogDateArray(ArrayList<String> logDateArray) {
        this.logDateArray = logDateArray;
    }
}
