package com.example.steam_tracker.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.steam_tracker.DatabaseConnection.SQL_Server_Connection;
import com.example.steam_tracker.File.FileScanner;
import com.example.steam_tracker.File.MemoryOperations;
import com.example.steam_tracker.GuiPanels.AddItemPanel;
import com.example.steam_tracker.GuiPanels.ObservedItemPanel;
import com.example.steam_tracker.Items.ObservedItem;
import com.example.steam_tracker.R;
import com.example.steam_tracker.Scale.ScaleLayouts;
import com.example.steam_tracker.SteamConnnection.SteamRequests;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity implements ActivityBuildInterface,View.OnClickListener, View.OnLongClickListener, View.OnScrollChangeListener {

    ScaleLayouts scallingController = new ScaleLayouts();
    SteamRequests steamRequestController = new SteamRequests();
    FileScanner fileScannerController = new FileScanner();
    MemoryOperations memoryContorller = new MemoryOperations();
    SQL_Server_Connection sqlController = new SQL_Server_Connection();
    ItemDetailsActivity itemDetailsController = new ItemDetailsActivity();
    ObservedItem observedItemsController = new ObservedItem();

    private View optionsBarView;
    private RelativeLayout observedItemsView;
    private TextView pageTitleTV;
    private TextView itemsCounter;
    private ImageButton updateItemsIB;
    private ImageButton openOptionsBarIB;
    private Button settingsModeBT;
    private Button addNewItemModeBT;
    private Button topMoversModeBT;
    private ScrollView observedItemsScrollView;

    static HashMap<ObservedItem, ImageButton> observedItemPanels = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

        matchControlsById();
        setControlsParams();
        setControlsToListeners();

        fileScannerController.checkSettingsFolderExists();
        memoryContorller.readSerializedObject(getApplicationContext());
        sqlController.getObservedItemsFromDb();

        createObservedItemsPanel();
        updateItemsCounter();
    }

    @Override
    public void matchControlsById() {
        setContentView(R.layout.activity_main);
        pageTitleTV = findViewById(R.id.pageTitleTV);
        itemsCounter = findViewById(R.id.itemsCounterTV);
        observedItemsScrollView = findViewById(R.id.observedItemsScrollView);
        observedItemsView = findViewById(R.id.observedItemsView);
        updateItemsIB = findViewById(R.id.updateItemsIB);
        openOptionsBarIB = findViewById(R.id.openOptionsBarIB);
        optionsBarView = findViewById(R.id.optionsBarView);
        settingsModeBT = findViewById(R.id.settingsModeBT);
        addNewItemModeBT = findViewById(R.id.addNewItemModeBT);
        topMoversModeBT = findViewById(R.id.topMoversModeBT);
    }

    @Override
    public void setControlsParams() {
        scallingController.setScallingParams(100, 7, 40, 0, 0, pageTitleTV);
        scallingController.setScallingParams(10, 5, 40, 1, 38, itemsCounter);
        scallingController.setScallingParams(6, 5, 0, 1, 80, updateItemsIB);
        scallingController.setScallingParams(6, 5, 0, 1, 90, openOptionsBarIB);
        scallingController.setScallingParams(100, 92, 0, 7, 0, observedItemsScrollView);
        scallingController.setTextMargins(5, 0, 0, 0, pageTitleTV);

        scallingController.setScallingParams(60, 93, 0, 7, 40, optionsBarView);
        scallingController.setScallingParams(100, 6, 30, 0, 0, addNewItemModeBT);
        scallingController.setScallingParams(100, 6, 30, 6, 0, topMoversModeBT);
        scallingController.setScallingParams(100, 6, 30, 12, 0, settingsModeBT);
    }
    @Override
    public void setControlsToListeners() {
        updateItemsIB.setOnClickListener(this);
        openOptionsBarIB.setOnClickListener(this);
        settingsModeBT.setOnClickListener(this);
        addNewItemModeBT.setOnClickListener(this);
        topMoversModeBT.setOnClickListener(this);
        observedItemsScrollView.setOnScrollChangeListener(this);
    }

    public void onBackPressed() {
        if(optionsBarView.getVisibility() == View.VISIBLE)
        {
            optionsBarView.setVisibility(View.GONE);
        }
        else {
            showCloseAppDialog();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateObservedItems();
        updateItemsCounter();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == updateItemsIB.getId()) {
            updateObservedItems();
            updateItemsCounter();
            return;
        }

        if (id == openOptionsBarIB.getId()) {
            if (optionsBarView.getVisibility() == View.VISIBLE) {
                optionsBarView.setVisibility(View.GONE);
            } else {
                optionsBarView.setVisibility(View.VISIBLE);
            }
            return;
        }

        if (id == settingsModeBT.getId()) {
            openNewActivity(AppSettingsActivity.class);
            return;
        }

        if (id == addNewItemModeBT.getId()) {
            openNewActivity(NewItemToObserveActivity.class);
            return;
        }

        if(id == topMoversModeBT.getId())
        {
            openNewActivity(TopMoversActivity.class);
            return;
        }

        //when observed item panel clicked
        if (getClickedObservedItem((ImageButton) view) != null) {
            itemDetailsController.setObservedItemDetails(getClickedObservedItem((ImageButton) view));
            openNewActivity(ItemDetailsActivity.class);
            return;
        }

        // in case when addNewItemButton Clicked
        openNewActivity(NewItemToObserveActivity.class);
    }

    private void openNewActivity(Class targetActivity) {
        optionsBarView.setVisibility(View.GONE);
        Intent newActivity = new Intent(MainActivity.this, targetActivity);
        this.startActivity(newActivity);
    }

    private void updateItemsCounter() {
        int itemsCount = observedItemsController.getObservedItemsArray().size();
        itemsCounter.setText("(" + itemsCount + ")");
    }

    //refresh prices and images of all observed items
    private void updateObservedItems() {
        sqlController.getObservedItemsFromDb();
        createObservedItemsPanel();
        onWindowFocusChanged(true);
    }

    //creates panel for observed item
    private void createObservedItemsPanel() {
        observedItemsView.removeAllViews();
        ArrayList<ObservedItem> observedItems = observedItemsController.getObservedItemsArray();
        for (int i = 0; i <= observedItems.size(); i++) {
            Point location = getObservedItemLocation(i);
            if (i == observedItems.size()) {
                AddItemPanel addItemPanelController = new AddItemPanel(location, this.getApplicationContext(), observedItemsView);
                addItemPanelController.getPanel().getBtn().setOnClickListener(this);
            } else {
                ObservedItemPanel panel = new ObservedItemPanel(location, this.getApplicationContext(), observedItemsView).getPanel();
                setValuesInPanelsControls(panel, observedItems.get(i));
            }
        }
        updateObservedItemsPanel();
    }

    private Point getObservedItemLocation(int index) {
        int rowIndex = (index % 3);
        int x = (rowIndex * 30) + 1 + rowIndex + 3;

        int columnIndex = (index / 3);
        int y = (columnIndex * 21) + 1 + columnIndex + 1;
        return new Point(x, y);
    }


    private void setValuesInPanelsControls(ObservedItemPanel panel, ObservedItem observedItem) {
        ImageButton btn = panel.getItemBtnIB();
        observedItemPanels.put(observedItem, btn);
        btn.setOnClickListener(this);
        btn.setOnLongClickListener(this);
        panel.getItemNameTV().setText(observedItem.getName());
        panel.getItemPriceTV().setText(String.format("%.02f",(observedItem.getPrice())) + "zł");
    }

    //Only when is visible in scrollview
    private void updateObservedItemsPanel() {
        ViewTreeObserver viewTreeObserver = observedItemsView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ArrayList<ObservedItem> itemsToUpdate = getActualVisibleItems(observedItemsScrollView);
                updateItemsImage(itemsToUpdate);
                observedItemsView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    //updates only actual visible items (scrollview)
    private ArrayList<ObservedItem> getActualVisibleItems(View scrollView) {
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        ArrayList<ObservedItem> itemsToUpdate = new ArrayList<>();
        for (ObservedItem observedItem : observedItemsController.getObservedItemsArray()) {
            ImageButton btn = observedItemPanels.get(observedItem);
            try {
                if (btn.getLocalVisibleRect(scrollBounds)) {
                    if (!observedItem.isImageUpdated() && !(btn.getDrawable() instanceof BitmapDrawable)) {
                        btn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.no_item_image));
                        getImageFromSteam(observedItem);
                        itemsToUpdate.add(observedItem);
                    }
                }
            } catch (Exception e) {
                if (btn == null) {
                    createObservedItemsPanel();
                }
            }
        }
        return itemsToUpdate;
    }

    //sends request to api for image
    private void getImageFromSteam(ObservedItem observedItem) {
        steamRequestController.sendImageRequest(observedItem);
        observedItem.setImageUpdatedStatus(true);
    }

    //updating images dinamically
    private void updateItemsImage(ArrayList<ObservedItem> itemsToUpdate) {
        if (itemsToUpdate.size() > 0) {
            Thread thread = new Thread() {
                public void run() {
                    boolean imageReadyToSet = false;
                    while (!imageReadyToSet) {
                        for (ObservedItem observedItem : itemsToUpdate) {
                            ImageButton btn = observedItemPanels.get(observedItem);
                            imageReadyToSet = observedItem.getImageBytes() != null;
                            if (imageReadyToSet) {
                                setItemImageIntoBtn(observedItem, btn);
                            } else {
                                try {
                                    Thread.sleep(100);
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

    private void setItemImageIntoBtn(ObservedItem observedItem, ImageButton btn) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte[] imageData = observedItem.getImageBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                btn.setImageBitmap(bitmap);
            }
        });
    }

    private ObservedItem getClickedObservedItem(ImageButton btn) {
        for (ObservedItem observedItem : observedItemsController.getObservedItemsArray()) {
            if (observedItemPanels.get(observedItem) == btn) {
                return observedItem;
            }
        }
        return null;
    }

    @Override
    public void onScrollChange(View scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        ArrayList<ObservedItem> itemsToUpdate = getActualVisibleItems(scrollView);
        updateItemsImage(itemsToUpdate);
    }

    //removing observed item
    @Override
    public boolean onLongClick(View v) {
        ImageButton imgBtn = (ImageButton) v;
        ObservedItem itemToRemove = getObservedItemToRemove(imgBtn);
        showRemovingItemDialog(itemToRemove);
        return true;
    }

    private ObservedItem getObservedItemToRemove(ImageButton btn) {
        for (ObservedItem observedItem : observedItemsController.getObservedItemsArray()) {
            if (observedItemPanels.get(observedItem) == btn) {
                return observedItem;
            }
        }
        return null;
    }

    //CANCEL or REMOVE
    private void showRemovingItemDialog(ObservedItem itemToRemove) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to remove " + itemToRemove.getName() + "?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeObservedItem(itemToRemove);
                        updateObservedItems();
                        updateItemsCounter();
                        Toast.makeText(getApplicationContext(), itemToRemove.getName() + " has been removed.", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#c43f35"));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#d9d3d2"));
            }
        });
        dialog.show();
    }

    private void removeObservedItem(ObservedItem observedItem) {
        sqlController.removeItemFromObserved(observedItem.getSteamItemsId(), observedItem.getObservedId());
    }

    //CANCEL or CLOSE
    private void showCloseAppDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to leave?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#c43f35"));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#d9d3d2"));
            }
        });
        dialog.show();
    }
}