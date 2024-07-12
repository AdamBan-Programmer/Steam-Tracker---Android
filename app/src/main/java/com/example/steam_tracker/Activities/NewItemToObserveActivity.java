package com.example.steam_tracker.Activities;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.steam_tracker.DatabaseConnection.SQL_Server_Connection;
import com.example.steam_tracker.GuiPanels.SearchItemPanel;
import com.example.steam_tracker.Items.Item;
import com.example.steam_tracker.Items.ObservedItem;
import com.example.steam_tracker.R;
import com.example.steam_tracker.Scale.ScaleLayouts;
import com.example.steam_tracker.SteamConnnection.SteamRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewItemToObserveActivity extends AppCompatActivity implements ActivityBuildInterface,View.OnClickListener,View.OnScrollChangeListener {

    ScaleLayouts scallingController = new ScaleLayouts();
    Item itemsController = new Item();
    SQL_Server_Connection sqlController = new SQL_Server_Connection();
    ObservedItem observedItemsController = new ObservedItem();
    SteamRequests steamRequestController = new SteamRequests();

    Map<Item, SearchItemPanel> itemsPanelsList = new HashMap<>();
    private ArrayList<Item> selectedItemsToAdd = new ArrayList<>();

    private TextView pageTitleTV;
    private EditText itemNameET;
    private ImageButton searchIB;
    private ScrollView searchedScrollView;
    private RelativeLayout searchedItemsView;
    private TextView resultsCountTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

        matchControlsById();
        setControlsParams();
        setControlsToListeners();
    }

    @Override
    public void matchControlsById() {
        setContentView(R.layout.activity_new_item_to_observe);
        pageTitleTV = findViewById(R.id.pageTitleTV);
        itemNameET = findViewById(R.id.itemNameET);
        searchIB = findViewById(R.id.searchIB);
        searchedScrollView = findViewById(R.id.searchedScrollView);
        searchedItemsView = findViewById(R.id.searchedItemsView);
        resultsCountTV = findViewById(R.id.resultsCountTV);
    }

    @Override
    public void setControlsParams() {
        scallingController.setScallingParams(100, 7, 40, 0, 0, pageTitleTV);
        scallingController.setScallingParams(70, 5, 40, 8, 15, resultsCountTV);
        scallingController.setScallingParams(70, 5, 50, 13, 15, itemNameET);
        scallingController.setScallingParams(5, 4, 0, 13.5f, 79, searchIB);
        scallingController.setTextMargins(5, 0, 0, 0, pageTitleTV);
        scallingController.setTextMargins(2, 7, 0, 0, itemNameET);
        scallingController.setScallingParams(100, 76, 0, 21, 0, searchedScrollView);
    }

    @Override
    public void setControlsToListeners() {
        searchIB.setOnClickListener(this);
        searchedScrollView.setOnScrollChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == searchIB.getId()) {
            String phrase = itemNameET.getText().toString();
            if (phrase.length() >= 3) {
                sqlController.getItemsByPhraseFromDb(phrase);
                searchItemsByPhrase();
            }
            else {
                Toast.makeText(this, "Write minimum 3 letters.", Toast.LENGTH_SHORT).show();
            }
        } else {
            addItemToObserved(v);
        }
    }

    private void searchItemsByPhrase() {
        int listedItemsCount = createItemPanel();
        resultsCountTV.setText("Results: " + listedItemsCount);
        searchedScrollView.scrollTo(0, 0);
        Toast.makeText(this, "found: " + listedItemsCount + " records.", Toast.LENGTH_SHORT).show();
    }

    private int createItemPanel() {
        searchedItemsView.removeAllViews();
        ArrayList<Item> items = itemsController.getItemsArray();
        int listedItemsCount = 0;
        for (int i = 0; i < items.size(); i++) {
            if (!isAlreadyObserved(items.get(i))) {
                SearchItemPanel panel = new SearchItemPanel(getPanelLocationByIndex(listedItemsCount), NewItemToObserveActivity.this, searchedItemsView).getPanel();
                setValuesInPanelControls(panel, items.get(i));
                items.get(i).setImageUpdatedStatus(false);
                listedItemsCount++;
            }
        }
        updateItemsView();
        return listedItemsCount;
    }

    private Point getPanelLocationByIndex(int index) {
        int x = 5;
        int y = (index * 10) + index;
        return new Point(x, y);
    }

    private void setValuesInPanelControls(SearchItemPanel panel, Item item) {
        panel.getItemNameTV().setText(item.getName());
        ImageButton addToObservedIB = panel.getAddToObservedIB();
        addToObservedIB.setOnClickListener(NewItemToObserveActivity.this);
        itemsPanelsList.put(item, panel);
    }

    private void updateItemsView() {
        ViewTreeObserver viewTreeObserver = searchedItemsView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    ArrayList<Item> itemsToUpdate = getVisibleItemImages(searchedItemsView);
                    updateItemsImage(itemsToUpdate);
                    searchedItemsView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } catch (Exception e) {
                }
            }
        });
    }

    //visible when scrolling
    private ArrayList<Item> getVisibleItemImages(View scrollView) {
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        ArrayList<Item> itemsToUpdate = new ArrayList<>();
        for (Item item : itemsController.getItemsArray()) {
            if (itemsPanelsList.get(item) != null) {
                ImageView imageView = itemsPanelsList.get(item).getItemImageIV();
                if (imageView.getLocalVisibleRect(scrollBounds)) {
                    if (!item.isImageUpdated()) {
                        steamRequestController.sendImageRequest(item);
                        itemsToUpdate.add(item);
                        item.setImageUpdatedStatus(true);
                    }
                }
            }
        }
        return itemsToUpdate;
    }

    @Override
    public void onScrollChange(View scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        ArrayList<Item> itemsToUpdate = getVisibleItemImages(scrollView);
        updateItemsImage(itemsToUpdate);
    }

    //updating images dinamically
    private void updateItemsImage(ArrayList<Item> itemsToUpdate) {
        if (itemsToUpdate.size() > 0) {
            Thread thread = new Thread() {
                public void run() {
                    boolean notUpdated = true;
                    while (notUpdated) {
                        notUpdated = false;
                        for (Item item : itemsToUpdate) {
                            ImageView btn = itemsPanelsList.get(item).getItemImageIV();
                            if (item.getImageBytes() != null) {
                                setItemImageIntoBtn(item, btn);
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
            };
            thread.start();
        }
    }

    private void setItemImageIntoBtn(Item item, ImageView imageView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte[] imageData = item.getImageBytes();
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
            }
        });
    }

    private void addItemToObserved(View v) {
        ImageButton btn = (ImageButton) v;
        btn.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.btn_star_big_on));
        Item item = getClickedObservedItem(btn);
        if (!isAlreadyObserved(item)) {
            selectedItemsToAdd.add(item);
            sqlController.addItemToObserved(item.getSteamItemsId(), getLastObservedItemId() + selectedItemsToAdd.size());
            itemsPanelsList.remove(btn);
            Toast.makeText(this, item.getName() + "added into observed list.", Toast.LENGTH_SHORT).show();
        }
    }

    private Item getClickedObservedItem(ImageButton btn) {
        for (Item item : itemsController.getItemsArray()) {
            if (itemsPanelsList.get(item) != null && itemsPanelsList.get(item).getAddToObservedIB() == btn) {
                return item;
            }
        }
        return null;
    }

    private int getLastObservedItemId() {
        int observedItemsCount = observedItemsController.getObservedItemsArray().size();
        int last_observed_id;
        if (observedItemsCount > 0) {
            last_observed_id = observedItemsController.getObservedItemsArray().get(observedItemsCount - 1).getObservedId();
        } else {
            last_observed_id = 1;
        }
        return last_observed_id;
    }

    private boolean isAlreadyObserved(Item item) {
        ArrayList<ObservedItem> observedItems = observedItemsController.getObservedItemsArray();
        for (ObservedItem observedItem : observedItems) {
            if (observedItem.getSteamItemsId() == item.getSteamItemsId()) {
                return true;
            }
        }
        for (Item selectedItemToAdd : selectedItemsToAdd) {
            if (selectedItemToAdd == item) {
                return true;
            }
        }
        return false;
    }
}