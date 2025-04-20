package com.takehome.stayease.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.takehome.stayease.service.ExcelExportService;

@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private ExcelExportService exportService;

    @GetMapping("/excel")
    public ResponseEntity<String> exportExcel(@RequestParam(required = true) String entity) {
    	exportService.exportExcelInBackground(entity);
        return ResponseEntity.ok("Export started for entity: " + entity);
    }
}
