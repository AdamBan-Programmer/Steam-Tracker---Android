package com.example.steam_tracker.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.steam_tracker.File.MemoryOperations;
import com.example.steam_tracker.R;
import com.example.steam_tracker.Scale.ScaleLayouts;
import com.example.steam_tracker.Settings.AppSettings;

public class AppSettingsActivity extends AppCompatActivity implements ActivityBuildInterface,View.OnClickListener {

    ScaleLayouts scallingController = new ScaleLayouts();
    AppSettings settingsController = new AppSettings();
    MemoryOperations memoryController = new MemoryOperations();

    private TextView pageTitleTV;
    private EditText dbPasswordET;
    private EditText dbIpET;
    private EditText dbPortET;
    private EditText dbNameET;
    private EditText dbLoginET;
    private TextView dbPasswordTV;
    private TextView dbIpTV;
    private TextView dbPortTV;
    private TextView dbNameTV;
    private TextView dbLoginTV;
    private Button saveBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

        matchControlsById();
        setControlsParams();
        setControlsToListeners();
        setValuesIntoControls();
    }

    @Override
    public void matchControlsById() {
        setContentView(R.layout.activity_app_settings);
        pageTitleTV = findViewById(R.id.pageTitleTV);
        dbIpET = findViewById(R.id.dbIpET);
        dbPortET = findViewById(R.id.dbPortET);
        dbNameET = findViewById(R.id.dbNameET);
        dbLoginET = findViewById(R.id.dbLoginET);
        dbPasswordET = findViewById(R.id.dbPasswordET);
        dbIpTV = findViewById(R.id.dbIpTV);
        dbPortTV = findViewById(R.id.dbPortTV);
        dbNameTV = findViewById(R.id.dbNameTV);
        dbLoginTV = findViewById(R.id.dbLoginTV);
        dbPasswordTV = findViewById(R.id.dbPasswordTV);
        saveBT = findViewById(R.id.saveBT);
    }

    @Override
    public void setControlsParams() {
        scallingController.setScallingParams(100, 7, 50, 0, 0, pageTitleTV);
        scallingController.setScallingParams(87, 10, 10,13, 16, dbIpTV);
        scallingController.setScallingParams(87, 10, 10,25, 16, dbPortTV);
        scallingController.setScallingParams(87, 10, 10,37, 16, dbNameTV);
        scallingController.setScallingParams(87, 10, 10,49, 16, dbLoginTV);
        scallingController.setScallingParams(87, 10, 10,61, 16, dbPasswordTV);
        scallingController.setScallingParams(70, 10, 20,13, 15, dbIpET);
        scallingController.setScallingParams(70, 10, 20,25, 15, dbPortET);
        scallingController.setScallingParams(70, 10, 20,37, 15, dbNameET);
        scallingController.setScallingParams(70, 10, 20,49, 15, dbLoginET);
        scallingController.setScallingParams(70, 10, 20,61, 15, dbPasswordET);
        scallingController.setScallingParams(60, 8, 20,85, 20, saveBT);
    }

    @Override
    public void setControlsToListeners() {
        saveBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == saveBT.getId())
        {
            try {
                getValuesFromControls();
                memoryController.serializeObjectToFile(settingsController.getCurrentAppSettings());
                this.finish();
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error, saving settings failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setValuesIntoControls()
    {
        dbIpET.setText(settingsController.getCurrentAppSettings().getIpAddress());
        dbPortET.setText(String.valueOf(settingsController.getCurrentAppSettings().getPort()));
        dbNameET.setText(settingsController.getCurrentAppSettings().getDbName());
        dbLoginET.setText(settingsController.getCurrentAppSettings().getDbLogin());
        dbPasswordET.setText(settingsController.getCurrentAppSettings().getDbPassword());
    }
    private void getValuesFromControls() {
        settingsController.getCurrentAppSettings().setIpAddress(dbIpET.getText().toString());
        settingsController.getCurrentAppSettings().setPort(Integer.parseInt(dbPortET.getText().toString()));
        settingsController.getCurrentAppSettings().setDbName(dbNameET.getText().toString());
        settingsController.getCurrentAppSettings().setDbLogin(dbLoginET.getText().toString());
        settingsController.getCurrentAppSettings().setDbPassword(dbPasswordET.getText().toString());
    }
}