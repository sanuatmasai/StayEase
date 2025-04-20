package com.takehome.stayease.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.takehome.stayease.service.EasterEggService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Easter Egg API", description = "Hidden API for number facts")
@RestController
public class EasterEggController {

    @Autowired
    private EasterEggService easterEggService;

    @Operation(summary = "Get number fact", description = "Fetches number fact from Numbers API")
    @GetMapping("/easter-egg/hidden-feature")
    public ResponseEntity<String> triggerBulkFactGeneration() {
        try {
        	easterEggService.fetchAndSaveFactsInBulk(30, "number_facts.txt");
            return ResponseEntity.ok("File generation started in background!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
