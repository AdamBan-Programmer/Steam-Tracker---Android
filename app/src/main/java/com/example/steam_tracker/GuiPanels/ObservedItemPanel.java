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

import com.example.steam_tracker.R;
import com.example.steam_tracker.Scale.ScaleLayouts;

public class ObservedItemPanel extends ItemPanel {

    ScaleLayouts scallingController = new ScaleLayouts();

    private TextView itemNameTV;
    private TextView itemPriceTV;

    public ObservedItemPanel(Point location, Context context, RelativeLayout view)
    {
        super(location,context,view);
    }

    public ObservedItemPanel(Point location, ImageButton btn,TextView itemName,TextView itemPrice) {
        super(location,btn);
        this.itemNameTV = itemName;
        this.itemPriceTV = itemPrice;
    }
    @Override
    public ObservedItemPanel getPanel()
    {
        Point loc = this.getLocation();
        TextView nameTV = createObservedItemNameTV(loc);
        ImageButton btn = createItemBtn(loc);
        TextView priceTV = createObservedItemPriceTV(loc);
        return new ObservedItemPanel(loc,btn,nameTV,priceTV);
    }

    private ImageButton createItemBtn(Point location)
    {
        ImageButton newItemBtn = new ImageButton(this.context);
        newItemBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        newItemBtn.setBackground(ContextCompat.getDrawable(this.context, R.drawable.observed_item_button));
        this.getView().addView(newItemBtn);
        scallingController.setScallingParams(30, 21, 20, location.y, location.x, newItemBtn);
        return newItemBtn;
    }
    private TextView createObservedItemPriceTV(Point location)
    {
        TextView observedItemPriceTV = new TextView(this.context);
        observedItemPriceTV.setTextColor(Color.BLACK);
        observedItemPriceTV.setElevation(10);
        observedItemPriceTV.setGravity(Gravity.CENTER);
        this.getView().addView(observedItemPriceTV);
        scallingController.setScallingParams(30, 5, 40, location.y+16, location.x, observedItemPriceTV);
        return observedItemPriceTV;
    }

    private TextView createObservedItemNameTV(Point location)
    {
        TextView observedItemNameTV = new TextView(this.context);
        observedItemNameTV.setTextColor(Color.BLACK);
        observedItemNameTV.setElevation(10);
        observedItemNameTV.setGravity(Gravity.CENTER);
        this.getView().addView(observedItemNameTV);
        scallingController.setScallingParams(28, 5, 25, location.y, location.x+1, observedItemNameTV);
        return observedItemNameTV;
    }

    public TextView getItemNameTV() {
        return this.itemNameTV;
    }

    public TextView getItemPriceTV() {
        return this.itemPriceTV;
    }

    public ImageButton getItemBtnIB()
    {
        return this.ImageBT;
    }

}
