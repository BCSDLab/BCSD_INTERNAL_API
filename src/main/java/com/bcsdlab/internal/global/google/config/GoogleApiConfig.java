package com.bcsdlab.internal.global.google.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

@Configuration
public class GoogleApiConfig {

    String credentialsFilePath;

    public GoogleApiConfig(
        @Value("${google.spreadsheet.directory.credentials-path}") String credentialsFilePath
    ) {
        this.credentialsFilePath = credentialsFilePath;
    }

    @Bean
    public Sheets sheets() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource(credentialsFilePath).getInputStream())
            .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
        return new Sheets.Builder(new com.google.api.client.http.javanet.NetHttpTransport(),
            new com.google.api.client.json.jackson2.JacksonFactory(),
            new HttpCredentialsAdapter(credentials))
            .setApplicationName("Google SpreadSheet")
            .build();
    }
}
