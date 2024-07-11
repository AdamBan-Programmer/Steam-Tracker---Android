package com.example.steam_tracker.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.steam_tracker.GuiPanels.TopMoversItemPanel;
import com.example.steam_tracker.Items.ObservedItem;
import com.example.steam_tracker.R;
import com.example.steam_tracker.Scale.ScaleLayouts;
import com.example.steam_tracker.SteamConnnection.SteamRequests;
import com.example.steam_tracker.Utils.PriceTrendTracker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TopMoversActivity extends AppCompatActivity implements ActivityBuildInterface,View.OnClickListener {

    ScaleLayouts scallingController = new ScaleLayouts();
    ObservedItem observedItemsController = new ObservedItem();
    ItemDetailsActivity itemDetailsController = new ItemDetailsActivity();
    PriceTrendTracker priceTrendTrackerController = new PriceTrendTracker();
    SteamRequests steamRequestController = new SteamRequests();


    static Map<ObservedItem, TopMoversItemPanel> topMoversPanels = new HashMap<>();

    private TextView pageTitleTV;
    private TextView bestTrendsTitleTV;
    private TextView worstTrendsTitleTV;
    private RelativeLayout bestTrendsSectionView;
    private RelativeLayout worstTrendsSectionView;
    private ScrollView bestTrendsScrollView;
    private ScrollView worstTrendsScrollView;
    private RelativeLayout bestTrendsView;
    private RelativeLayout worstTrendsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

        matchControlsById();
        setControlsParams();
        setControlsToListeners();

        priceTrendTrackerController.updatePricesTrend();
        insertItemsIntoTables();
        updateItemsImages();
    }

    @Override
    public void matchControlsById() {
        setContentView(R.layout.activity_top_movers);
        pageTitleTV = findViewById(R.id.pageTitleTV);
        bestTrendsSectionView = findViewById(R.id.bestTrendsSection);
        worstTrendsSectionView = findViewById(R.id.worstTrendsSection);
        bestTrendsTitleTV = findViewById(R.id.bestTrendsTitleTV);
        worstTrendsTitleTV = findViewById(R.id.worstTrendsTitleTV);
        bestTrendsView = findViewById(R.id.bestTrendsView);
        worstTrendsView = findViewById(R.id.worstTrendsView);
        bestTrendsScrollView = findViewById(R.id.bestTrendsScrollView);
        worstTrendsScrollView = findViewById(R.id.worstTrendsScrollView);
        worstTrendsView = findViewById(R.id.worstTrendsView);
    }

    @Override
    public void setControlsParams() {
        scallingController.setScallingParams(100, 7, 40, 0, 0, pageTitleTV);
        scallingController.setScallingParams(100, 5, 55, 7, 5, bestTrendsTitleTV);
        scallingController.setScallingParams(90, 40, 3, 12, 5, bestTrendsSectionView);
        scallingController.setScallingParams(100, 36, 0, 2, 0, bestTrendsScrollView);
        scallingController.setScallingParams(100, 5, 55, 52, 5, worstTrendsTitleTV);
        scallingController.setScallingParams(90, 40, 3, 57, 5, worstTrendsSectionView);
        scallingController.setScallingParams(100, 36, 0, 2, 0, worstTrendsScrollView);
        scallingController.setTextMargins(5, 0, 0, 0, pageTitleTV);
    }

    @Override
    public void setControlsToListeners() {
    }

    private void insertItemsIntoTables() {
        ArrayList<ObservedItem> sortedObservedItemsArray = priceTrendTrackerController.getSortedObservedItemsArray();
        for (int i = 0; i < sortedObservedItemsArray.size(); i++) {
            ObservedItem observedItem = sortedObservedItemsArray.get(i);
            TopMoversItemPanel panelController = null;
            float priceTrend = observedItem.getPriceTrend();
            if (priceTrend != 0) {
                if (priceTrend > 0) {
                    panelController = new TopMoversItemPanel(getPanelLocation(bestTrendsView), this.getApplicationContext(), bestTrendsView).getPanel();
                }
                if (priceTrend < 0) {
                    panelController = new TopMoversItemPanel(getPanelLocation(worstTrendsView), this.getApplicationContext(), worstTrendsView).getPanel();
                }
                displayTopMoversObjects(panelController, observedItem);
            }
        }
    }

    private Point getPanelLocation(RelativeLayout view)
    {
        int panelElements = 5;
        int index = view.getChildCount()/panelElements;
        int x = 0;
        int y = (index * 5)+index;
        return new Point(x,y);
    }

    private void displayTopMoversObjects(TopMoversItemPanel panelController, ObservedItem observedItem)
    {
        setTopMoversPanelValues(panelController,observedItem);
        panelController.getItemButtonBT().setOnClickListener(this);
        topMoversPanels.put(observedItem, panelController);
    }

    private void setTopMoversPanelValues(TopMoversItemPanel panel, ObservedItem observedItem) {
        panel.getItemNameTV().setText(observedItem.getName());
        panel.getItemPriceTV().setText(String.format("%.02f",observedItem.getPrice()) + "zł");
        configurePriceTrendPanel(panel.getItemTrendValueTV(), observedItem.getPriceTrend());
        if (observedItem.getImageBytes() != null) {
            byte[] imageData = observedItem.getImageBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            panel.getItemImageIV().setImageBitmap(bitmap);
        } else {
            steamRequestController.sendImageRequest(observedItem);
        }
    }

    private void configurePriceTrendPanel(TextView TrendValueTV, float priceTrend)
    {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if(priceTrend > 0)
        {
            TrendValueTV.setText("+"+decimalFormat.format(priceTrend)+"%");
            TrendValueTV.setTextColor(Color.GREEN);
        }
        else
        {
            TrendValueTV.setText(decimalFormat.format(priceTrend)+"%");
            TrendValueTV.setTextColor(Color.RED);
        }
    }

    private void updateItemsImages() {
        ArrayList<ObservedItem> itemsToUpdate = priceTrendTrackerController.getSortedObservedItemsArray();
        if (itemsToUpdate.size() > 0) {
            Thread thread = new Thread() {
                public void run() {
                    boolean notUpdated = true;
                    while (notUpdated) {
                        notUpdated = false;
                        for (ObservedItem observedItem : itemsToUpdate) {
                            TopMoversItemPanel itemPanel = topMoversPanels.get(observedItem);
                            if(itemPanel != null) {
                                if (observedItem.getImageBytes() != null) {
                                    setItemImageIntoBtn(observedItem, itemPanel.getItemImageIV());
                                } else {
                                    try {
                                        Thread.sleep(100);
                                        notUpdated = true;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            };
            thread.start();
        }
    }

    private void setItemImageIntoBtn(ObservedItem observedItem, ImageView imageView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte[] imageData = observedItem.getImageBytes();
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (getClickedObservedItem((Button) view) != null) {
            itemDetailsController.setObservedItemDetails(getClickedObservedItem((Button) view));
            OpenNewActivity(ItemDetailsActivity.class);
        }
    }

    private ObservedItem getClickedObservedItem(Button btn) {
        for (ObservedItem observedItem : observedItemsController.getObservedItemsArray()) {
            if (topMoversPanels.get(observedItem) != null && topMoversPanels.get(observedItem).getItemButtonBT() == btn) {
                return observedItem;
            }
        }
        return null;
    }

    private void OpenNewActivity(Class targetActivity) {
        Intent newActivity = new Intent(TopMoversActivity.this, targetActivity);
        this.startActivity(newActivity);
    }
}