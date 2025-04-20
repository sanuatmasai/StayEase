package com.takehome.stayease.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EasterEggService {

	@Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<String> fetchFact(int number) {
        try {
            String fact = restTemplate.getForObject("http://numbersapi.com/" + number, String.class);
            System.out.println(number+"-----");
            return CompletableFuture.completedFuture(fact);
        } catch (Exception e) {
            return CompletableFuture.completedFuture("Failed for " + number);
        }
    }

    public void fetchAndSaveFactsInBulk(int count, String filePath) throws Exception {
        List<CompletableFuture<String>> futureList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            futureList.add(fetchFact(i));
        }

        // Wait for all futures to complete
        List<String> allFacts = futureList.stream()
                .map(CompletableFuture::join) // Blocking call - waits for each future
                .collect(Collectors.toList());

        // Save to text file
        File dir = new File("output");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String fact : allFacts) {
                writer.write(fact);
                writer.newLine();
            }
        }

        System.out.println("✅ All facts saved to: " + file.getAbsolutePath());
    }
}
