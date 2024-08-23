package com.example.xuong.Controller;

import com.example.xuong.Model.Staff;
import com.example.xuong.Repository.StaffRepo;
import com.example.xuong.Seciver.ExcelSevice;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class StaffController {
    @Autowired
    ExcelSevice excelService;
    @Autowired
    private StaffRepo staffRepo;

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("staff", staffRepo.findAll());
        model.addAttribute("staffEr", new Staff());
        return "Home";
    }

    @PostMapping("/add/staff")
    public String add(@Validated @ModelAttribute Staff staff, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("staffEr", staff);
            return "Home";
        }

        // Validate email addresses
        if (staff.getAccountFPT() != null && !staff.getAccountFPT().endsWith("@fpt.edu.vn")) {
            model.addAttribute("error", "Email FPT phải kết thúc với @fpt.edu.vn");
            model.addAttribute("staffEr", staff);
            return "Home";
        }
        if (staff.getAccountFE() != null && !staff.getAccountFE().endsWith("@fe.edu.vn")) {
            model.addAttribute("error", "Email FE phải kết thúc với @fe.edu.vn");
            model.addAttribute("staffEr", staff);
            return "Home";
        }

        // Validate required fields
        if (staff.getStaffCode().isEmpty() || staff.getName().isEmpty() ||
                staff.getAccountFPT().isEmpty() || staff.getAccountFE().isEmpty()) {
            model.addAttribute("error", "Không được để trống");
            model.addAttribute("staffEr", staff);
            return "Home";
        }

        // Check for duplicates
        if (staffRepo.existsByAccountFE(staff.getAccountFE()) ||
                staffRepo.existsByAccountFPT(staff.getAccountFPT()) ||
                staffRepo.existsByStaffCode(staff.getStaffCode())) {
            model.addAttribute("error", "Mã hoặc email đã tồn tại");
            model.addAttribute("staffEr", staff);
            return "Home";
        }

        // Save the staff member if all checks pass
        staffRepo.save(staff);
        return "redirect:/home";
    }


    @GetMapping("/staff/doi")
    public String changeStatus(@RequestParam("id") UUID id, Model model) {
        Staff staff = staffRepo.findById(id).orElse(null);
        if (staff != null) {
            staff.setStatus(staff.getStatus() == 1 ? 0 : 1);
            staffRepo.save(staff);
            model.addAttribute("message", "Đổi trạng thái thành công");
        } else {
            model.addAttribute("error", "Nhân viên không tồn tại");
        }

        return "redirect:/home";
    }

    @GetMapping("/detail/staff")
    public String detail(@RequestParam("id") UUID id, Model model) {
        Staff staff = staffRepo.findById(id).orElse(null);
        model.addAttribute("staff", staff);

        return "Detail";
    }

    @PostMapping("/update/staff")
    public String updateStaff(@ModelAttribute("staff") @Validated Staff staff, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Detail";
        }
        if (staff.getAccountFPT() != null && !staff.getAccountFPT().endsWith("@fpt.edu.vn")) {
            model.addAttribute("error", "Email FPT phải kết thúc với @fpt.edu.vn");
            return "Detail";
        } else if (staff.getAccountFE() != null && !staff.getAccountFE().endsWith("@fe.edu.vn")) {
            model.addAttribute("error", "Email FE phải kết thúc với @fe.edu.vn");
            return "Detail";
        }

        if (staff.getStaffCode().isEmpty() || staff.getName().isEmpty() || staff.getAccountFPT().isEmpty() || staff.getAccountFE().isEmpty()) {
            model.addAttribute("error", "Không được để trống");
            return "Detail";
        }

        staffRepo.save(staff);
        return "redirect:/home";
    }
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<Staff> staffList = staffRepo.findAll(); // Lấy toàn bộ dữ liệu từ database

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Staff List");

        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Mã nhân viên");
        headerRow.createCell(2).setCellValue("Họ tên");
        headerRow.createCell(3).setCellValue("Email FPT");
        headerRow.createCell(4).setCellValue("Email FE");
        headerRow.createCell(5).setCellValue("Trạng thái");

        // Tạo các dòng dữ liệu
        int rowIndex = 1;
        for (Staff staff : staffList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(staff.getId().toString());
            row.createCell(1).setCellValue(staff.getStaffCode());
            row.createCell(2).setCellValue(staff.getName());
            row.createCell(3).setCellValue(staff.getAccountFPT());
            row.createCell(4).setCellValue(staff.getAccountFE());
            row.createCell(5).setCellValue(staff.getStatus() == 1 ? "Đang hoạt động" : "Ngưng hoạt động");
        }

        // Ghi dữ liệu ra ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Tạo header cho file download
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=staff_list.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
    @PostMapping("/import/staff")
    public String importStaff(@RequestParam("file") MultipartFile file, Model model) {
        try {
            List<Staff> staffList = excelService.importStaff(file);
            staffRepo.saveAll(staffList);
            model.addAttribute("message", "Import thành công");
        } catch (IOException e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi đọc file");
        }

        return "redirect:/home";
    }

}
