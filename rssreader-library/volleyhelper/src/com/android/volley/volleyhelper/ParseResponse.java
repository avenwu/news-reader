package com.android.volley.volleyhelper;

import java.util.ArrayList;

public class ParseResponse<T> {
    public byte state;
    public Exception exception;
    public static final byte SUCCESS = 1;
    public static final byte FAILED = 0;
    public ArrayList<T> result;
}
