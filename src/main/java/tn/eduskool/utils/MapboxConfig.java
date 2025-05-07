package tn.eduskool.utils;

public class MapboxConfig {
    private static final String API_KEY = "pk.eyJ1IjoiZ2Fkb3VyNTMiLCJhIjoiY21hMTV5czlqMjR6ejJrc2UxbGhiNjd0bSJ9._Cvd2xx8PzgStAseWralkQ";
    private static final String BASE_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/";

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}