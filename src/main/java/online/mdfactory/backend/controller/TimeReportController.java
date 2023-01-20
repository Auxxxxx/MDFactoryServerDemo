package online.mdfactory.backend.controller;

import online.mdfactory.backend.service.JsonParserService;
import online.mdfactory.backend.service.TimeReportService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/time_report")
public class TimeReportController {
    @Autowired
    private TimeReportService timeReportService;
    @Autowired
    private JsonParserService jsonParserService;

    @GetMapping("/export-to-excel/{login}/{start}/{end}")
    public void exportIntoExcelFile(@PathVariable String login, @PathVariable String start, @PathVariable String end) {
        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE);
        try(XSSFWorkbook workbook = timeReportService.buildTimeXls(List.of(login), startDate, endDate);
            FileOutputStream out = new FileOutputStream("D:\\yourFileName.xls")) {

            workbook.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
