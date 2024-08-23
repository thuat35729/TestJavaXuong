package com.example.xuong.Seciver;

import com.example.xuong.Model.Staff;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelSevice {
    public List<Staff> importStaff(MultipartFile file) throws IOException {
        List<Staff> staffList = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Staff staff = new Staff();
                staff.setStaffCode(getCellValue(row.getCell(1)));
                staff.setName(getCellValue(row.getCell(2)));
                staff.setAccountFE(getCellValue(row.getCell(3)));
                staff.setAccountFPT(getCellValue(row.getCell(4)));
                staffList.add(staff);
            }
        }
        return staffList;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


}
