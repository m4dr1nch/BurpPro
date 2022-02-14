package com.loader.burp;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import java.util.Random;
import java.util.Date;
import java.util.ArrayList;

public class Keygen
{
    private static final byte[] encryption_key;
    
    public String generateActivation(final String s) {
        final ArrayList<String> decodeActivationRequest = this.decodeActivationRequest(s);
        if (decodeActivationRequest == null) {
            return "Error decoding activation request :-(";
        }
        return this.prepareArray(new String[] { "0.4315672535134567", decodeActivationRequest.get(0), "activation", decodeActivationRequest.get(1), "True", "", decodeActivationRequest.get(2), decodeActivationRequest.get(3), "xMoYxfewJJ3jw/Zrqghq1nMHJIsZEtZLu9kp4PZw+kGt+wiTtoUjUfHyTt/luR3BjzVUj2Rt2tTxV2rjWcuV7MlwsbFrLOqTVGqstIYA1psSP/uspFkkhFwhMi0CJNRHdxe+xPYnXObzi/x6G4e0wH3iZ5bnYPRfn7IHiV1TVzslQur/KR5J8BG8CN3B9XaS8+HJ90Hn4sy81fW0NYRlNW8m5k4rMDNwCLvDzp11EN//wxYEdruNKqtxEvv6VesiFOg711Y6g/9Nf91C5dFedNEhPv2k2fYb4rJ+z1mCOBSmWIzjGlS1r2xOzITrrrMkr+ilBE3VFPPbES4KsRh/fw==", "tdq99QBI3DtnQQ7rRJLR0uAdOXT69SUfAB/8O2zi0lsk4/bXkM58TP6cuhOzeYyrVUJrM11IsJhWrv8SiomzJ/rqledlx+P1G5B3MxFVfjML9xQz0ocZi3N+7dHMjf9/jPuFO7KmGfwjWdU4ItXSHFneqGBccCDHEy4bhXKuQrA=" });
    }
    
    public String generateLicense(final String s) {
        return this.prepareArray(new String[] { this.getRandomString(), "license", s, String.valueOf(new Date().getTime() + 251887612380600L), "1", "full", "tdq99QBI3DtnQQ7rRJLR0uAdOXT69SUfAB/8O2zi0lsk4/bXkM58TP6cuhOzeYyrVUJrM11IsJhWrv8SiomzJ/rqledlx+P1G5B3MxFVfjML9xQz0ocZi3N+7dHMjf9/jPuFO7KmGfwjWdU4ItXSHFneqGBccCDHEy4bhXKuQrA=" });
    }
    
    private String getRandomString() {
        final String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        final StringBuilder sb = new StringBuilder();
        final Random random = new Random();
        while (sb.length() < 32) {
            sb.append(s.charAt((int)(random.nextFloat() * s.length())));
        }
        return sb.toString();
    }
    
    private String prepareArray(final String[] array) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i = 0; i < array.length - 1; ++i) {
                byteArrayOutputStream.write(array[i].getBytes());
                byteArrayOutputStream.write(0);
            }
            byteArrayOutputStream.write(array[array.length - 1].getBytes());
            return new String(Base64.getEncoder().encode(this.encrypt(byteArrayOutputStream.toByteArray())));
        }
        catch (Exception cause) {
            cause.printStackTrace();
            throw new RuntimeException(cause);
        }
    }
    
    private byte[] encrypt(final byte[] input) {
        try {
            final SecretKeySpec key = new SecretKeySpec(Keygen.encryption_key, "DES");
            final Cipher instance = Cipher.getInstance("DES");
            instance.init(1, key);
            return instance.doFinal(input);
        }
        catch (Exception cause) {
            cause.printStackTrace();
            throw new RuntimeException(cause);
        }
    }
    
    private byte[] decrypt(final byte[] input) {
        try {
            final SecretKeySpec key = new SecretKeySpec(Keygen.encryption_key, "DES");
            final Cipher instance = Cipher.getInstance("DES");
            instance.init(2, key);
            return instance.doFinal(input);
        }
        catch (Exception cause) {
            cause.printStackTrace();
            throw new RuntimeException(cause);
        }
    }
    
    private ArrayList<String> decodeActivationRequest(final String src) {
        try {
            final byte[] decrypt = this.decrypt(Base64.getDecoder().decode(src));
            final ArrayList<String> obj = new ArrayList<String>();
            int n = 0;
            for (int i = 0; i < decrypt.length; ++i) {
                if (decrypt[i] == 0) {
                    obj.add(new String(decrypt, n, i - n));
                    n = i + 1;
                }
            }
            obj.add(new String(decrypt, n, decrypt.length - n));
            if (obj.size() != 5) {
                System.out.print("Activation Request Decoded to wrong size! The following was Decoded: \n");
                System.out.print(obj);
                return null;
            }
            return obj;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    static {
        encryption_key = "burpr0x!".getBytes();
    }
}