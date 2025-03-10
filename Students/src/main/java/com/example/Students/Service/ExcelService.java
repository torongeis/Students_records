package com.example.Students.Service;

import com.example.Students.Student.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    public List<Student> parseExcelFile(MultipartFile file) {
        List<Student> students = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming first sheet

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Student student = Student.builder()
                        .studentId(getCellValueAsString(row.getCell(0))) // Handle numeric values
                        .firstName(getCellValueAsString(row.getCell(1)))
                        .lastName(getCellValueAsString(row.getCell(2)))
                        .dateOfBirth(getDateValue(row.getCell(3))) // Handle dates properly
                        .email(getCellValueAsString(row.getCell(4)))
                        .phoneNumber(getCellValueAsString(row.getCell(5)))
                        .courseEnrolled(getCellValueAsString(row.getCell(6)))
                        .registrationDate(getDateValue(row.getCell(7))) // Handle dates properly
                        .build();

                students.add(student);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }
        return students;
    }

    // Converts both STRING and NUMERIC cells to a String
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // Convert date to string
                }
                return String.valueOf((long) cell.getNumericCellValue()); // Convert numeric to String
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }


    private LocalDate getDateValue(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(cell)) {
            return null;
        }
        return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

