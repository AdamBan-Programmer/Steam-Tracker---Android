package com.example.steam_tracker.File;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;


import com.example.steam_tracker.Settings.AppSettings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

public class MemoryOperations extends FileEncryption {

    AppSettings settingsController = new AppSettings();
    FileEncryption encryptionController = new FileEncryption();
    FileScanner fileScannerController = new FileScanner();

    // Saves settings into a file (serialization)
    public void serializeObjectToFile(AppSettings settingsObject) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        fileScannerController.checkSettingsFolderExists();
        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + fileScannerController.getPathToSettingsFile());
        encryptObject(settingsObject, fos);
    }

    public void readSerializedObject(Context context) {
        try {
            fileScannerController.checkSettingsFolderExists();
            SecretKeySpec sks = new SecretKeySpec(encryptionController.getKey(), encryptionController.getTransformation());
            Cipher cipher = Cipher.getInstance(encryptionController.getTransformation());
            cipher.init(Cipher.DECRYPT_MODE, sks);

            FileInputStream istream = new FileInputStream(Environment.getExternalStorageDirectory() + fileScannerController.getPathToSettingsFile());
            CipherInputStream cipherInputStream = new CipherInputStream(istream, cipher);
            ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
            try {
                SealedObject sealedObject = (SealedObject) inputStream.readObject();
                settingsController.setCurrentAppSettings((com.example.steam_tracker.Settings.AppSettings) sealedObject.getObject(cipher));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(context, "ERROR! Firstly configure your app in Settings panel!", Toast.LENGTH_SHORT).show();
        }
    }
}
