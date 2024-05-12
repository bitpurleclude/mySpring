package com.purplecloud.exception;

public class APITooFrequentException extends RuntimeException {
    private final String apiName;
    private final int maxRequestsPerMinute;

    public APITooFrequentException(String apiName, int maxRequestsPerMinute) {
        super("API requests are too frequent for: " + apiName);
        this.apiName = apiName;
        this.maxRequestsPerMinute = maxRequestsPerMinute;
    }

    public String getApiName() {
        return apiName;
    }

    public int getMaxRequestsPerMinute() {
        return maxRequestsPerMinute;
    }
}
