package com.takehome.stayease.service;

import java.io.FileOutputStream;
import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.takehome.stayease.exception.BadRequestException;
import com.takehome.stayease.model.Booking;
import com.takehome.stayease.model.Hotel;
import com.takehome.stayease.model.User;
import com.takehome.stayease.repository.BookingRepository;
import com.takehome.stayease.repository.HotelRepository;
import com.takehome.stayease.repository.UserRepository;

@Service
public class ExcelExportService {

    @Autowired 
    private UserRepository userRepository;
    @Autowired 
    private HotelService hotelService;
    @Autowired 
    private BookingRepository bookingRepository;

    @Async("customExecutor") // Use your custom thread pool
    public void exportExcelInBackground(String entityType) {
        try {
            List<?> data;

            switch (entityType.toLowerCase()) {
                case "user":
                    data = userRepository.findAll();
                    break;
                case "hotel":
                    data = hotelService.getAllHotel();
                    break;
                case "booking":
                    data = bookingRepository.findAll();
                    break;
                default:
                    throw new BadRequestException("Invalid entity type: " + entityType);
            }

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(entityType + "_data");

            createHeaderRow(sheet, entityType);
            fillData(sheet, data, entityType);

            String fileName = entityType + "_export_" + System.currentTimeMillis() + ".xlsx";
            FileOutputStream out = new FileOutputStream(new File("exports/" + fileName));
            workbook.write(out);
            workbook.close();
            out.close();

            System.out.println("✅ Excel Export Completed: " + fileName);
        } catch (Exception e) {
            System.out.println("❌ Excel Export Failed: " + e.getMessage());
        }
    }

    private void createHeaderRow(Sheet sheet, String entityType) {
        Row header = sheet.createRow(0);
        switch (entityType.toLowerCase()) {
            case "user":
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("First Name");
                header.createCell(2).setCellValue("Last Name");
                header.createCell(3).setCellValue("Email");
                header.createCell(4).setCellValue("Role");
                break;
            case "hotel":
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Name");
                header.createCell(2).setCellValue("Location");
                header.createCell(3).setCellValue("Description");
                header.createCell(4).setCellValue("Total Rooms");
                header.createCell(5).setCellValue("Available Rooms");
                break;
            case "booking":
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Check-In");
                header.createCell(2).setCellValue("Check-Out");
                header.createCell(3).setCellValue("Hotel Name");
                header.createCell(4).setCellValue("Customer Name");
                break;
        }
    }

    private void fillData(Sheet sheet, List<?> data, String entityType) {
        int rowIdx = 1;

        for (Object obj : data) {
            Row row = sheet.createRow(rowIdx++);

            switch (entityType.toLowerCase()) {
                case "user":
                    User user = (User) obj;
                    row.createCell(0).setCellValue(user.getId());
                    row.createCell(1).setCellValue(user.getFirstName());
                    row.createCell(2).setCellValue(user.getLastName());
                    row.createCell(3).setCellValue(user.getEmail());
                    row.createCell(4).setCellValue(user.getRole().toString());
                    break;
                case "hotel":
                    Hotel hotel = (Hotel) obj;
                    row.createCell(0).setCellValue(hotel.getId());
                    row.createCell(1).setCellValue(hotel.getName());
                    row.createCell(2).setCellValue(hotel.getLocation());
                    row.createCell(3).setCellValue(hotel.getDescription());
                    row.createCell(4).setCellValue(hotel.getTotalRooms());
                    row.createCell(5).setCellValue(hotel.getAvailableRooms());
                    break;
                case "booking":
                    Booking booking = (Booking) obj;
                    row.createCell(0).setCellValue(booking.getId());
                    row.createCell(1).setCellValue(booking.getCheckInDate().toString());
                    row.createCell(2).setCellValue(booking.getCheckOutDate().toString());
                    row.createCell(3).setCellValue(booking.getHotel().getName());
                    row.createCell(4).setCellValue(booking.getCustomer().getFirstName());
                    break;
            }
        }
    }
}