package com.ubidots.ubidots;

public class Constants {
    public Constants() {
        // Required empty body constructor
    }

    public static final String FIRST_TIME = "first_time";
    public static final String URL = "url";
    public static final String TOKEN = "token";
    public static final String DATASOURCE_VARIABLE = "location";
    public static final String VARIABLE_ID = "variable";
    public static final String PUSH_TIME = "push_time";
    public static final String SERVICE_RUNNING = "service_running";

    public static final int NOTIFICATION_ID = 1337;

    public static class BROWSER_CONFIG {
        // User-agent
        public static final String USER_AGENT = "Android-Ubidots/1.6";

        // URLs
        public static final String LOGIN_URL = "http://app.ubidots.com";
        public static final String SIGN_UP_URL = "http://app.ubidots.com/accounts/signup/";
        public static final String HELP_URL = "http://ubidots.com/docs/";
    }

    public static class VARIABLE_CONTEXT {
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "lng";
    }
}
