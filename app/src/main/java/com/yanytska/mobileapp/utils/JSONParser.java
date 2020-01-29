package com.yanytska.mobileapp.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.yanytska.mobileapp.data.model.Fare;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    private JSONParser() {
        //default empty constructor for Singleton class with no attributes
    }

    public static <T> List<T> getFromJSONtoArrayList(String jsonString, Type type) {
        if (!isValid(jsonString))
            return new ArrayList<>();
        return new Gson().fromJson(jsonString, type);
    }

    public static Fare getFromJSONToFare(String json, Type type) {
        if (!isValid(json)) {
            return null;
        } else {
            return new Gson().fromJson(json, type);
        }
    }

    private static boolean isValid(String jsonString) {
        try {
            new JsonParser().parse(jsonString);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }
}
