package com.example.steam_tracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steam_tracker.DatabaseConnection.SQL_Server_Connection;
import com.example.steam_tracker.Items.ObservedItem;
import com.example.steam_tracker.R;
import com.example.steam_tracker.Scale.ScaleLayouts;
import com.example.steam_tracker.Utils.ChartModeEnum;
import com.example.steam_tracker.Utils.ChartSettings;
import com.example.steam_tracker.Utils.DateFormat;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class ItemDetailsActivity extends AppCompatActivity implements ActivityBuildInterface,View.OnClickListener,OnChartValueSelectedListener {

    ScaleLayouts scallingController = new ScaleLayouts();
    ChartSettings chartSettingsController = new ChartSettings();
    SQL_Server_Connection sql_Controller = new SQL_Server_Connection();
    DateFormat dateFormatController = new DateFormat();

    private View itemImageView;
    private View itemPriceView;
    private View recentPriceLogsList;
    private ScrollView pricesListScrollView;
    private TextView pageTitleTV;
    private TextView itemNameTV;
    private LineChart itemPriceChart;
    private ImageView itemImageIV;
    private TextView itemPriceTV;
    private TextView itemPriceTitleTV;
    private Button todayPricesBT;
    private Button weeklyPricesBT;
    private Button monthlyPricesBT;
    private Button allTimePricesBT;
    private Button openRecentPriceLogsListBT;
    private TextView recentPriceLogsListTV;
    private ImageButton closeRecentPriceLogsListIB;
    private TextView pricesListTitleTV;
    private TextView priceUpdateTV;
    private TextView selectedPriceTV;

    static ObservedItem observedItemDetails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

        matchControlsById();
        setControlsParams();
        setControlsToListeners();

        //if results not imported
        if (observedItemDetails.getPriceArray() == null && observedItemDetails.getLogDateArray() == null) {
            sql_Controller.getObservedItemsResults(observedItemDetails);
        }
        setDefaultValues();
        displayAllTimePrices();//default
        setItemImage();
    }

    @Override
    public void matchControlsById() {
        setContentView(R.layout.activity_item_details);
        pageTitleTV = findViewById(R.id.pageTitleTV);
        itemNameTV = findViewById(R.id.itemNameTV);
        itemImageView = findViewById(R.id.itemImageView);
        itemPriceView = findViewById(R.id.itemPriceView);
        recentPriceLogsList = findViewById(R.id.recentPriceLogsList);
        itemPriceChart = findViewById(R.id.itemPriceChart);
        itemImageIV = findViewById(R.id.itemImageIV);
        itemPriceTitleTV = findViewById(R.id.itemPriceTitleTV);
        itemPriceTV = findViewById(R.id.itemPriceTV);
        allTimePricesBT = findViewById(R.id.allTimePricesBT);
        todayPricesBT = findViewById(R.id.todayPricesBT);
        weeklyPricesBT = findViewById(R.id.weeklyPricesBT);
        monthlyPricesBT = findViewById(R.id.monthlyPricesBT);
        openRecentPriceLogsListBT = findViewById(R.id.openRecentPriceLogsListBT);
        recentPriceLogsListTV = findViewById(R.id.recentPriceLogsListTV);
        closeRecentPriceLogsListIB = findViewById(R.id.closeRecentPriceLogsListIB);
        pricesListScrollView = findViewById(R.id.pricesListScrollView);
        pricesListTitleTV = findViewById(R.id.pricesListTitleTV);
        priceUpdateTV = findViewById(R.id.priceUpdateTV);
        selectedPriceTV = findViewById(R.id.selectedPriceTV);
    }

    @Override
    public void setControlsParams() {
        scallingController.setScallingParams(100, 7, 40, 0, 0, pageTitleTV);
        scallingController.setScallingParams(90, 7, 40, 7.5f, 5, itemNameTV);
        scallingController.setScallingParams(50, 30, 30, 16, 5, itemImageView);
        scallingController.setScallingParams(48, 28, 30, 1, 1, itemImageIV);
        scallingController.setScallingParams(20, 5, 30, 48, 9, allTimePricesBT);
        scallingController.setScallingParams(20, 5, 30, 48, 31, todayPricesBT);
        scallingController.setScallingParams(20, 5, 30, 48, 53, weeklyPricesBT);
        scallingController.setScallingParams(20, 5, 30, 48, 75, monthlyPricesBT);
        scallingController.setScallingParams(90, 5, 40, 53, 5, selectedPriceTV);

        scallingController.setScallingParams(100, 40, 30, 57, 0, itemPriceChart);
        scallingController.setScallingParams(38, 30.1f, 30, 16, 57, itemPriceView);
        scallingController.setScallingParams(38, 7, 40, 0, 0, itemPriceTitleTV);
        scallingController.setScallingParams(38, 40, 10, 10, 0, itemPriceTV);
        scallingController.setScallingParams(100, 3, 40, 22, 0, priceUpdateTV);
        scallingController.setScallingParams(100, 5, 30, 25, 0, openRecentPriceLogsListBT);
        scallingController.setTextMargins(5, 0, 0, 0, pageTitleTV);

        scallingController.setScallingParams(90, 70, 30, 15, 5, recentPriceLogsList);
        scallingController.setScallingParams(100, 5, 50, 0, 5, pricesListTitleTV);
        scallingController.setScallingParams(100, 90, 0, 5, 0, pricesListScrollView);
        scallingController.setScallingParams(8, 5, 0, 0, 82, closeRecentPriceLogsListIB);
        scallingController.setScallingParams(100, 90, 2.5f, 5, 0, pricesListScrollView);
    }

    @Override
    public void setControlsToListeners() {
        allTimePricesBT.setOnClickListener(this);
        todayPricesBT.setOnClickListener(this);
        weeklyPricesBT.setOnClickListener(this);
        monthlyPricesBT.setOnClickListener(this);
        openRecentPriceLogsListBT.setOnClickListener(this);
        closeRecentPriceLogsListIB.setOnClickListener(this);
        itemPriceChart.setOnChartValueSelectedListener(this);
    }

    private void setDefaultValues() {
        try {
            int lastIndex = observedItemDetails.getLogDateArray().size() - 1;
            itemNameTV.setText(observedItemDetails.getName() + ":");
            itemPriceTV.setText(String.format("%.02f", observedItemDetails.getPrice()) + "zł");
            priceUpdateTV.setText(observedItemDetails.getLogDateArray().get(lastIndex));
        } catch (ArrayIndexOutOfBoundsException a) {
            priceUpdateTV.setText("no data collected.");
        }
    }

    private void setItemImage() {
        try {
            byte[] imageData = observedItemDetails.getImageBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            itemImageIV.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(ItemDetailsActivity.this, "Couldn't load an image...", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == allTimePricesBT.getId()) {
            displayAllTimePrices();
        }

        if (id == todayPricesBT.getId()) {
            displayTimeRangedPrices(ChartModeEnum.TODAY);
        }

        if (id == weeklyPricesBT.getId()) {
            displayTimeRangedPrices(ChartModeEnum.WEEKLY);
        }

        if (id == monthlyPricesBT.getId()) {
            displayTimeRangedPrices(ChartModeEnum.MONTHLY);
        }

        if (id == openRecentPriceLogsListBT.getId()) {
            recentPriceLogsList.setVisibility(View.VISIBLE);
            setRecentPriceLogs();
        }

        if (id == closeRecentPriceLogsListIB.getId()) {
            recentPriceLogsList.setVisibility(View.GONE);
        }
    }

    //ALL TIME
    private void displayAllTimePrices() {
        String[] xValues = observedItemDetails.getLogDateArray().toArray(new String[0]);
        String[] yValues = convertToStringArray(observedItemDetails.getPriceArray());
        chartSettingsController.drawItemPriceChart(itemPriceChart, xValues, yValues);
    }

    //price in Float -> price in String
    private static String[] convertToStringArray(ArrayList<Float> floatArrayList) {
        String[] stringArray = new String[floatArrayList.size()];
        for (int i = 0; i < floatArrayList.size(); i++) {
            stringArray[i] = Float.toString(floatArrayList.get(i));
        }
        return stringArray;
    }

    // TODAY, WEEK, MONTH
    private void displayTimeRangedPrices(ChartModeEnum mode) {
        DateFormat currentDay = dateFormatController.getCurrentDate();
        ArrayList<String> datesArray = new ArrayList<>();
        ArrayList<String> pricesArray = new ArrayList<>();
        for (int i = 0; i < observedItemDetails.getLogDateArray().size(); i++) {
            DateFormat date = dateFormatController.getDateFromString(observedItemDetails.getLogDateArray().get(i));
            if (currentDay.getYear() == date.getYear()) {
                switch (mode) {
                    case TODAY: {
                        if ((currentDay.getDayOfYear() == date.getDayOfYear())) {
                            datesArray.add(observedItemDetails.getLogDateArray().get(i));
                            pricesArray.add(String.valueOf(observedItemDetails.getPriceArray().get(i)));
                        }
                        break;
                    }

                    case WEEKLY: {
                        if (currentDay.getDayOfYear() - date.getDayOfYear() < 7) {
                            datesArray.add(observedItemDetails.getLogDateArray().get(i));
                            pricesArray.add(String.valueOf(observedItemDetails.getPriceArray().get(i)));
                        }
                        break;
                    }

                    case MONTHLY: {
                        if (currentDay.getDayOfYear() - date.getDayOfYear() < 30) {
                            datesArray.add(observedItemDetails.getLogDateArray().get(i));
                            pricesArray.add(String.valueOf(observedItemDetails.getPriceArray().get(i)));
                        }
                        break;
                    }
                }
            }
        }
        chartSettingsController.drawItemPriceChart(itemPriceChart, datesArray.toArray(new String[0]), pricesArray.toArray(new String[0]));
    }

    //price view (TODAY)
    private void setRecentPriceLogs() {
        DateFormat currentDay = dateFormatController.getCurrentDate();
        String priceLog = "";
        int priceLogIndex = 1;
        for (int i = 0; i < observedItemDetails.getLogDateArray().size(); i++) {
            DateFormat date = dateFormatController.getDateFromString(observedItemDetails.getLogDateArray().get(i));
            if (currentDay.getDayOfYear() == date.getDayOfYear()) {
                String price = String.valueOf(observedItemDetails.getPriceArray().get(i));
                String refresh_date = observedItemDetails.getLogDateArray().get(i);
                priceLog += priceLogIndex + ")   " + price + "zł     -     " + refresh_date + "\n";
                priceLogIndex++;
            }
        }
        recentPriceLogsListTV.setText(priceLog);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String formattedXValue = itemPriceChart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), null);
        selectedPriceTV.setText("Selected: " + h.getY() + "zł   -   " + formattedXValue);
    }

    @Override
    public void onNothingSelected() {
        //nothing
    }

    public void setObservedItemDetails(ObservedItem newObservedItemDetails) {
        observedItemDetails = newObservedItemDetails;
    }
}