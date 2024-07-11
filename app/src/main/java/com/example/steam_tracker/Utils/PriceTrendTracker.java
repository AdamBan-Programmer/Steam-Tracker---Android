package com.example.steam_tracker.Utils;

import com.example.steam_tracker.DatabaseConnection.SQL_Server_Connection;
import com.example.steam_tracker.Items.ObservedItem;

import java.util.ArrayList;
import java.util.Collections;

public class PriceTrendTracker {

    static ObservedItem observedItemsController = new ObservedItem();
    SQL_Server_Connection sqlController = new SQL_Server_Connection();

    public PriceTrendTracker()
    {
    }

    public void updatePricesTrend()
    {
        for(ObservedItem observedItem : observedItemsController.getObservedItemsArray())
        {
            float TodaysAvg = sqlController.getObservedItemsTodaysPriceAvg(observedItem);
            float CurrentAvg = sqlController.getObservedItemsCurrentPriceAvg(observedItem);
            float priceTrend = getCalculatedPriceTrend(TodaysAvg,CurrentAvg);
            observedItem.setPriceTrend(priceTrend);
        }
    }

    private float getCalculatedPriceTrend(float todaysAvg, float currentAvg)
    {
        if(todaysAvg > 0) {
            return ((currentAvg - todaysAvg) * 100) / todaysAvg;
        }
        else
        {
            return 0;
        }
    }

    //sorted by price trend
    public ArrayList<ObservedItem> getSortedObservedItemsArray() {
        ArrayList<ObservedItem> itemArray = observedItemsController.getObservedItemsArray();
        boolean shouldRepeat = true;
        while (shouldRepeat) {
            shouldRepeat = false;
            for (int i = 0; i < itemArray.size() - 1; i++) {
                float trend1 = itemArray.get(i).getPriceTrend();
                float trend2 = itemArray.get(i + 1).getPriceTrend();
                if ((trend1 < trend2 && trend2 >= 0) || (trend1 > trend2 && trend1 < 0)) {
                    Collections.swap(itemArray, i, i + 1);
                    shouldRepeat = true;
                }
            }
        }
        return itemArray;
    }
}
