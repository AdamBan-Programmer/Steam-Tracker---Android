package com.example.steam_tracker.DatabaseConnection;

import com.example.steam_tracker.Items.Item;
import com.example.steam_tracker.Items.ObservedItem;
import com.example.steam_tracker.Settings.AppSettings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQL_Server_Connection {
    AppSettings appSettingsController = new AppSettings();
    ObservedItem observedItemsController = new ObservedItem();
    Item importedItemsController = new Item();


    public SQL_Server_Connection()
    {
        //empty
    }

    //query
    public void modifyDbDataByQuery(String query) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Connection connection = appSettingsController.getCurrentAppSettings().getDbConnection();
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.executeUpdate();
                } catch (Exception e) {
                }
            }
        };
        thread.start();
    }

    //returns result
    public ResultSet getDbDataByQuery(String query){
        ResultSet[] result = {null};
            Thread thread = new Thread() {
                public void run() {
                    try {
                        Connection connection = appSettingsController.getCurrentAppSettings().getDbConnection();
                        PreparedStatement stmt = connection.prepareStatement(query);
                        result[0] = stmt.executeQuery();
                    } catch (Exception e) {
                    }
                }
            };
            thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }

    //gets observed item prices by item id
    public void getObservedItemsResults(ObservedItem observedItem)
    {
        ResultSet result = getDbDataByQuery("CALL GetObservedItemsDataById("+observedItem.getObservedId()+");");
        observedItemsController.setItemResults(result,observedItem);
    }

    //get today's avarage price
    public float getObservedItemsTodaysPriceAvg(ObservedItem observedItem)
    {
        ResultSet result = getDbDataByQuery("CALL GetTodaysPriceAvg("+observedItem.getObservedId()+");");
        try {
            if(result.next()) {
               return Float.parseFloat(result.getString(1));
            }
        }
        catch (Exception e)
        {
        }
        return 0;
    }

    //returns avarage price
    public float getObservedItemsCurrentPriceAvg(ObservedItem observedItem)
    {
        ResultSet result = getDbDataByQuery("CALL GetCurrentPriceAvg("+observedItem.getObservedId()+");");
        try {
            if(result.next()) {
                return Float.parseFloat(result.getString(1));
            }
        }
        catch (Exception e)
        {
        }
        return 0;
    }

    //gets all items added to observed
    public void getObservedItemsFromDb()
    {
        ResultSet result = getDbDataByQuery("CALL GetObservedItemsData();");
        observedItemsController.addItemsIntoArray(result);
    }

    //search prices by name or phrase
    public void getItemsByPhraseFromDb(String phrase)
    {
        phrase = phrase.toLowerCase(); // Big letters in phrase protection
        phrase = phrase.substring(0, 1).toUpperCase() + phrase.substring(1); // first letter uppercase
        ResultSet result = getDbDataByQuery("CALL GetItemsByPhrase('"+phrase+"');");
        importedItemsController.addItemsIntoArray(result);
    }

    public void addItemToObserved(int steamItemsId, int observedId)
    {
        modifyDbDataByQuery("CALL AddItemToObserved(" + steamItemsId + "," + observedId + ");");
    }

    public void removeItemFromObserved(int steamItemsId, int observedId)
    {
        modifyDbDataByQuery("CALL RemoveFromObserved(" + steamItemsId +"," + observedId +");");
    }
}
