package com.example.steam_tracker.GuiPanels;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.steam_tracker.Scale.ScaleLayouts;

public class SearchItemPanel extends ItemPanel {

    ScaleLayouts scallingController = new ScaleLayouts();

    private ImageView backgroundView;
    private ImageView itemImageIV;
    private ImageButton addToObservedIB;
    private TextView itemNameTV;

    public SearchItemPanel(Point location, Context context, RelativeLayout view) {
        super(location,context,view);
    }

    public SearchItemPanel(Point location,ImageView BackgroundView, ImageView ItemImageIV, TextView ItemNameTV,ImageButton AddToObservedBT) {
        super(location);
        this.backgroundView = BackgroundView;
        this.itemImageIV = ItemImageIV;
        this.itemNameTV = ItemNameTV;
        this.addToObservedIB = AddToObservedBT;
    }

    @Override
    public SearchItemPanel getPanel()
    {
        Point location = this.location;
        return createNewPanel(location);
    }

    @Override
    public SearchItemPanel createNewPanel(Point location)
    {
        ImageView backgroundView = createBackground(location);
        ImageView ItemImageIV = createItemImage(location);
        TextView ItemNameTV = createItemNameTV(location);
        ImageButton addToObservedIB = createAddToObservedBtn(location);
        return new SearchItemPanel(location,backgroundView,ItemImageIV,ItemNameTV,addToObservedIB);
    }

    private ImageView createBackground(Point location)
    {
        ImageView backgroundView = new ImageView(this.context);
        backgroundView.setBackgroundColor(Color.parseColor("#1b2838"));
        this.view.addView(backgroundView);
        scallingController.setScallingParams(90, 10, 20, location.y, location.x, backgroundView);
        return backgroundView;
    }

    private ImageView createItemImage(Point location)
    {
        ImageView itemImage = new ImageView(this.context);
        itemImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        itemImage.setElevation(10);
        this.view.addView(itemImage);
        scallingController.setScallingParams(10, 10, 20, location.y, location.x, itemImage);
        return itemImage;
    }

    private ImageButton createAddToObservedBtn(Point location)
    {
        ImageButton addToObservedBtn = new ImageButton(this.context);
        addToObservedBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addToObservedBtn.setElevation(10);
        addToObservedBtn.setImageDrawable(ContextCompat.getDrawable(this.context, android.R.drawable.btn_star_big_off));
        addToObservedBtn.setBackgroundColor(Color.parseColor("#1b2838"));
        this.view.addView(addToObservedBtn);
        scallingController.setScallingParams(10, 10, 20, location.y, location.x+80, addToObservedBtn);
        return addToObservedBtn;
    }

    private TextView createItemNameTV(Point location)
    {
        TextView observedItemNameTV = new TextView(this.context);
        observedItemNameTV.setTextColor(Color.BLACK);
        observedItemNameTV.setElevation(10);
        observedItemNameTV.setGravity(Gravity.CENTER);
        observedItemNameTV.setTextColor(Color.WHITE);
        this.view.addView(observedItemNameTV);
        scallingController.setScallingParams(70, 10, 25, location.y, location.x+10, observedItemNameTV);
        return observedItemNameTV;
    }

    public ImageView getItemImageIV() {
        return this.itemImageIV;
    }

    public TextView getItemNameTV() {
        return this.itemNameTV;
    }

    public ImageButton getAddToObservedIB() {
        return this.addToObservedIB;
    }
}
