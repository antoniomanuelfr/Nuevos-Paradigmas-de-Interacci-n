package com.p207.npi.Museo;

    import android.support.annotation.NonNull;

public class LanguageConfig {

    private final String languageCode;
    private final String accessToken;

    LanguageConfig(final String languageCode, final String accessToken) {
        this.languageCode = languageCode;
        this.accessToken = accessToken;
    }

    String getLanguageCode() {
        return languageCode;
    }

    String getAccessToken() {
        return accessToken;
    }

    @NonNull
    @Override
    public String toString() {
        return languageCode;
    }

}
