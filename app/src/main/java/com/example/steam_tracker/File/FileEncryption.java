package com.example.steam_tracker.File;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryption extends FileScanner {

    private byte[] key;
    private String transformation;

    static FileEncryption encryptor = new FileEncryption("79857987498379859760967097898622".getBytes(),"AES/ECB/PKCS5Padding");

    public FileEncryption()
    {

    }
    public FileEncryption(byte[] key, String transformation)
    {
        this.key = key;
        this.transformation = transformation;
    }
    public void encryptObject(Serializable object, OutputStream ostream) throws InvalidKeyException, IllegalBlockSizeException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        SecretKeySpec sks = new SecretKeySpec(encryptor.key, encryptor.transformation);

        // Create cipher
        Cipher cipher = Cipher.getInstance(encryptor.transformation);
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        SealedObject sealedObject = new SealedObject(object, cipher);

        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(ostream, cipher);
        ObjectOutputStream outputStream = new ObjectOutputStream(cos);
        outputStream.writeObject(sealedObject);
        outputStream.close();
    }

    public byte[] getKey() {
        return encryptor.key;
    }

    public String getTransformation() {
        return encryptor.transformation;
    }
}
