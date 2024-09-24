package com.bcsdlab.internal.global.google.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleSheetsService {

    private final Sheets sheetsService;

    public List<List<Object>> readSheetData(String spreadsheetId, String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute();
        return response.getValues();
    }

    public List<List<Object>> readSheet() throws IOException {
        String spreadsheetId = "16krjCV1iUakS1EQF5IK-nN5bOCeUBJ3fieL1zfhiVSs";
        String range = "명단!A2:N";

        return readSheetData(spreadsheetId, range);
    }
}
