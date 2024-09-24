package com.bcsdlab.internal.global.google.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class GoogleSheetsService {

    private final Sheets sheets;
    private final String spreadsheetId;

    public GoogleSheetsService(
        Sheets sheets,
        @Value("${google.spreadsheet.directory.id}") String spreadsheetId
    ) {
        this.sheets = sheets;
        this.spreadsheetId = spreadsheetId;
    }

    public List<List<Object>> readSheetData(String spreadsheetId, String range) throws IOException {
        ValueRange response = sheets.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute();
        return response.getValues();
    }

    public List<List<Object>> readSheet() throws IOException {
        String range = "명단!A2:N";
        return readSheetData(spreadsheetId, range);
    }
}
