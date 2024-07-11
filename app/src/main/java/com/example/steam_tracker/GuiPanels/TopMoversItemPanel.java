package com.example.steam_tracker.GuiPanels;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.steam_tracker.Scale.ScaleLayouts;

public class TopMoversItemPanel extends ItemPanel {

    ScaleLayouts scallingController = new ScaleLayouts();

    private ImageView itemImageIV;
    private Button itemButtonBT;
    private TextView itemNameTV;
    private TextView itemPriceTV;
    private TextView itemTrendValueTV;

    public TopMoversItemPanel(Point location, Context context, RelativeLayout view)
    {
        super(location,context,view);
    }

    public TopMoversItemPanel(Point location,ImageView ItemImageIV,Button ItemButton,TextView ItemNameTV,TextView ItemPriceTV,TextView ItemTrendValueTV) {
        super(location);
        this.itemImageIV = ItemImageIV;
        this.itemButtonBT = ItemButton;
        this.itemNameTV = ItemNameTV;
        this.itemPriceTV = ItemPriceTV;
        this.itemTrendValueTV = ItemTrendValueTV;
    }

    @Override
    public TopMoversItemPanel getPanel()
    {
        Point loc = this.getLocation();
        Button btn = createItemBtn();
        TextView priceTV = createItemPriceTV();
        TextView nameTV = createItemNameTV();
        TextView trendTV = createItemTrendTV();
        ImageView imageIV = createItemImageIV();
        return new TopMoversItemPanel(loc,imageIV,btn,nameTV,priceTV,trendTV);
    }

    private ImageView createItemImageIV()
    {
        ImageView imageView = new ImageView(this.context);
        this.getView().addView(imageView);
        imageView.setElevation(10);
        scallingController.setScallingParams(10, 5, 0, this.getLocation().y, this.getLocation().x + 3, imageView);
        return imageView;
    }

    private Button createItemBtn()
    {
        Button NewItemBtn = new Button(this.context);
        NewItemBtn.setBackgroundColor(Color.parseColor("#1b2838"));
        this.getView().addView(NewItemBtn);
        scallingController.setScallingParams(86, 5, 20, this.getLocation().y, this.getLocation().x+2, NewItemBtn);
        return NewItemBtn;
    }

    private TextView createItemPriceTV()
    {
        TextView itemPriceTV = new TextView(this.context);
        itemPriceTV.setTextColor(Color.WHITE);
        itemPriceTV.setElevation(10);
        itemPriceTV.setGravity(Gravity.CENTER);
        this.getView().addView(itemPriceTV);
        scallingController.setScallingParams(30, 5, 25, this.getLocation().y, this.getLocation().x + 50, itemPriceTV);
        return itemPriceTV;
    }

    private TextView createItemNameTV()
    {
        TextView itemNameTV = new TextView(this.context);
        itemNameTV.setTextColor(Color.WHITE);
        itemNameTV.setElevation(10);
        itemNameTV.setGravity(Gravity.CENTER);
        this.getView().addView(itemNameTV);
        scallingController.setScallingParams(30, 5, 25, this.getLocation().y, this.getLocation().x+20, itemNameTV);
        return itemNameTV;
    }

    private TextView createItemTrendTV()
    {
        TextView itemTrendTV = new TextView(this.context);
        itemTrendTV.setTextColor(Color.WHITE);
        itemTrendTV.setElevation(10);
        itemTrendTV.setGravity(Gravity.CENTER);
        this.getView().addView(itemTrendTV);
        scallingController.setScallingParams(20, 5, 25, this.getLocation().y, this.getLocation().x+75, itemTrendTV);
        return itemTrendTV;
    }

    public ImageView getItemImageIV() {
        return this.itemImageIV;
    }

    public void setItemImageIV(ImageView itemImageIV) {
        this.itemImageIV = itemImageIV;
    }

    public Button getItemButtonBT() {
        return this.itemButtonBT;
    }

    public void setItemButtonBT(Button itemButtonBT) {
        this.itemButtonBT = itemButtonBT;
    }

    public TextView getItemNameTV() {
        return this.itemNameTV;
    }

    public void setItemNameTV(TextView itemNameTV) {
        this.itemNameTV = itemNameTV;
    }

    public TextView getItemPriceTV() {
        return this.itemPriceTV;
    }

    public void setItemPriceTV(TextView itemPriceTV) {
        this.itemPriceTV = itemPriceTV;
    }

    public TextView getItemTrendValueTV() {
        return this.itemTrendValueTV;
    }

    public void setItemTrendValueTV(TextView itemTrendValueTV) {
        this.itemTrendValueTV = itemTrendValueTV;
    }
}
