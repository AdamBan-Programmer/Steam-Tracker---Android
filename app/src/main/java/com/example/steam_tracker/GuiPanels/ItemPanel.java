package com.example.steam_tracker.GuiPanels;

import android.content.Context;
import android.graphics.Point;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class ItemPanel {

    public Point location;
    public Context context;
    public RelativeLayout view;
    public ImageButton ImageBT;

    public ItemPanel(Point location, Context context, RelativeLayout view) {
        this.location = location;
        this.context = context;
        this.view = view;
    }

    public ItemPanel(Point location, ImageButton ImageBT) {
        this.location = location;
        this.ImageBT = ImageBT;
    }

    public ItemPanel(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return this.location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public RelativeLayout getView() {
        return this.view;
    }

    public void setView(RelativeLayout view) {
        this.view = view;
    }

    public ImageButton getImageBT() {
        return this.ImageBT;
    }

    public void setImageBT(ImageButton imageBT) {
        this.ImageBT = imageBT;
    }

    public ItemPanel getPanel()
    {
       return null;
    }
    public ItemPanel createNewPanel(Point location)
    {
        return null;
    }

}
