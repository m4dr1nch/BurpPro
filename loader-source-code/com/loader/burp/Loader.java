package com.loader.burp;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.IllegalClassFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.security.ProtectionDomain;
import java.nio.charset.StandardCharsets;
import java.lang.instrument.ClassFileTransformer;

public class Loader implements ClassFileTransformer
{
    public Loader() {
        System.out.println(Main.banner);
    }
    
    public static String toStringHex2(String s) {
        final byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; ++i) {
            try {
                bytes[i] = (byte)(0xFF & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            s = new String(bytes, StandardCharsets.UTF_8);
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        return s;
    }
    
    public static String printHexBinary(final byte[] array) {
        final char[] charArray = "0123456789ABCDEF".toCharArray();
        final StringBuilder sb = new StringBuilder(array.length * 2);
        for (final byte b : array) {
            sb.append(charArray[b >> 4 & 0xF]);
            sb.append(charArray[b & 0xF]);
        }
        return sb.toString();
    }
    
    @Override
    public byte[] transform(final ClassLoader classLoader, final String s, final Class<?> clazz, final ProtectionDomain protectionDomain, final byte[] array) throws IllegalClassFormatException {
        if (!toStringHex2(printHexBinary(array)).contains("751a8be34c1a9ed9633d04be3ba075a7")) {
            return null;
        }
        final byte[] a = new byte[array.length];
        System.arraycopy(array, 0, a, 0, a.length);
        final String string = Arrays.toString(a);
        final Matcher matcher = Pattern.compile("90, .\\w+, -89, 0, 4, -65, 25, 5, 21, 6, 5, 100, 51, 28, -102, 0, 18, -89, 0, 4, -65, 16, 61, -89, 0, 4, -65, -96, 90, .\\w+, 21, 9, -68, 8, 58, 10, 3, 54, 11, 21, 11, 21, 9, -94, 0, 31, 25, 10, 28, -102, 0, 29, 21, 11, 25, 5, 21, 8, 21, 11, 96, 51, 84, -124, 11, 1, 28, -103, -1, -28, -89, 0, 4, -65, 21, 8, -68, 8, 58, 7, 3, 54, 11, 21, 11, 21, 8, -94, 0, 28, 25, 7, 21, 11, 25, 5, 21, 11, 51, 84, -124, 11, 1, 28, -102, 0, 14, 28, -103, -1, -25, -89, 0, 4, -65").matcher(string);
        matcher.find();
        final String[] split = string.substring(0, matcher.start()).split(", ");
        final Matcher matcher2 = Pattern.compile(", 21, 9, -68, 8, 58, 10, 3, 54, 11, 21, 11, 21, 9, -94, 0, 31, 25, 10, 28, -102, 0, 29, 21, 11, 25, 5, 21, 8, 21, 11, 96, 51, 84, -124, 11, 1, 28, -103, -1, -28, -89, 0, 4, -65, 21, 8, -68, 8, 58, 7, 3, 54, 11, 21, 11, 21, 8, -94, 0, 28, 25, 7, 21, 11, 25, 5, 21, 11, 51, 84, -124, 11, 1, 28, -102, 0, 14, 28, -103, -1, -25, -89, 0, 4, -65").matcher(string);
        matcher2.find();
        final int[] array2 = { split.length, 0, split.length + 1, 3, split.length + 28, 0, split.length + 29, 3, split.length + 115, 167, split.length + 116, 90, split.length + 117, 39 + a[string.substring(0, matcher2.start()).split(", ").length - 1] - 117 };
        for (int i = 0; i < array2.length; i += 2) {
            a[array2[i]] = (byte)array2[i + 1];
        }
        return a;
    }
    
    public static void premain(final String s, final Instrumentation instrumentation) throws Exception {
        instrumentation.addTransformer(new Loader());
    }
}