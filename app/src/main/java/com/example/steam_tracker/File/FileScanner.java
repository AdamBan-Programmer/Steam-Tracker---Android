package com.example.steam_tracker.File;

import android.os.Environment;

public class FileScanner {

    private static final String APP_NAME = "Steam_Tracker";
    private static final String SETTINGS_FILE_NAME = "SettingsFile.txt";

    public String getPathToSettingsFile() {
        return "/Android/"+APP_NAME+"/"+SETTINGS_FILE_NAME;
    }

    public void checkSettingsFolderExists() {
        try {
            String DirectoryPath = Environment.getExternalStorageDirectory() + "/Android/"+APP_NAME;
            java.io.File SettingsDirectory = new java.io.File(DirectoryPath);
            java.io.File SettingsFile = new java.io.File(DirectoryPath + "/" + SETTINGS_FILE_NAME);
            if (SettingsDirectory.exists() == false) {
                SettingsDirectory.mkdirs();
            }
            if (SettingsFile.exists() == false) {
                SettingsFile.createNewFile();
            }
        } catch (Exception e) {
        }
    }
}
