package com.example.steam_tracker.Utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartSettings {

    public void drawItemPriceChart(LineChart itemPriceChart, String[] xValues, String[] yValues)
    {
        setDescriptions(itemPriceChart);
        set_X_Axis(itemPriceChart,xValues);
        set_Y_Axis(itemPriceChart,yValues);
        List<Entry> entry = setEntry(yValues);
        setEntryAndDrawChart(itemPriceChart,entry);
    }

    private void setDescriptions(LineChart itemPriceChart)
    {
        Description desc = new Description();
        desc.setText("Date");
        desc.setTextColor(Color.WHITE);
        desc.setPosition(0,0);
        itemPriceChart.setDescription(desc);
        itemPriceChart.getAxisRight().setDrawLabels(false);
    }

    private void set_X_Axis(LineChart itemPriceChart,String[] xValues)//date
    {
        XAxis xAxis = itemPriceChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineWidth(2f);
        xAxis.setGranularity(1f);
    }
    private void set_Y_Axis(LineChart itemPriceChart,String[] yValues)//price
    {
        YAxis yAxis = itemPriceChart.getAxisLeft();
        yAxis.setAxisMinimum(getMinValue(yValues));
        yAxis.setAxisMaximum(getMaxValue(yValues));
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.WHITE);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setGranularity(1f);
    }

    private List<Entry> setEntry(String[] yValues)
    {
        List<Entry> entry = new ArrayList<>();
        for(int i =0;i<yValues.length;i++) {
            entry.add(new Entry(i,Float.parseFloat(yValues[i].replace("zł","").replace(",","."))));
        }
        return entry;
    }

    private void setEntryAndDrawChart(LineChart itemPriceChart,List<Entry> entry)
    {
        LineDataSet dataSet = new LineDataSet(entry,"Price (zł)");
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.WHITE);
        LineData lineData = new LineData(dataSet);
        itemPriceChart.setData(lineData);
        itemPriceChart.invalidate();
    }

    private float getMinValue(String[] yValues)
    {
        for(int i=0;i<yValues.length;i++)
        {
            float potencialLowestValue = Float.parseFloat(yValues[i].replace("zł","").replace(",","."));
            for(int j =0;j< yValues.length;j++)
            {
                float value = Float.parseFloat(yValues[j].replace("zł","").replace(",","."));
                if(i!=j && potencialLowestValue > value)
                {
                    break;
                }
                if(j == yValues.length-1)
                {
                    return potencialLowestValue;
                }
            }
        }
        return 0;
    }

    private float getMaxValue(String[] yValues)
    {
        for(int i=0;i<yValues.length;i++)
        {
            float potencialMaxValue = Float.parseFloat(yValues[i].replace("zł","").replace(",","."));
            for(int j =0;j< yValues.length;j++)
            {
                float value = Float.parseFloat(yValues[j].replace("zł","").replace(",","."));
                if(i!=j && potencialMaxValue < value)
                {
                    break;
                }
                if(j == yValues.length-1)
                {
                    return potencialMaxValue;
                }
            }
        }
        return 0;
    }
}
