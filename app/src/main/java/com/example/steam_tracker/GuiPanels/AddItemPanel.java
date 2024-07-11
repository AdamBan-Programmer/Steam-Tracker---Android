package com.example.steam_tracker.GuiPanels;

import android.content.Context;
import android.graphics.Point;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.example.steam_tracker.R;
import com.example.steam_tracker.Scale.ScaleLayouts;

public class AddItemPanel extends ItemPanel {

    ScaleLayouts scallingController = new ScaleLayouts();

    public AddItemPanel(Point location,ImageButton btn)
    {
        super(location,btn);
    }

    public AddItemPanel(Point location,Context context,RelativeLayout view)
    {
        super(location, context, view);
    }

    @Override
    public AddItemPanel getPanel()
    {
        Point location = this.location;
        return createNewPanel(location);
    }

    @Override
    public AddItemPanel createNewPanel(Point location)
    {
        ImageButton newItemBtn = new ImageButton(this.context);
        newItemBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        newItemBtn.setElevation(10);
        newItemBtn.setBackground(ContextCompat.getDrawable(this.context, R.drawable.observed_item_button));
        newItemBtn.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.add_new_item_button));
        this.view.addView(newItemBtn);
        scallingController.setScallingParams(30, 21, 20, location.y, location.x, newItemBtn);
        return new AddItemPanel(location,newItemBtn);
    }

    public Point getLocation() {
        return this.location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public ImageButton getBtn() {
        return this.ImageBT;
    }

    public void setBtn(ImageButton btn) {
        this.ImageBT = btn;
    }
}
