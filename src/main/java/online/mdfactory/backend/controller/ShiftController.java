package online.mdfactory.backend.controller;

import online.mdfactory.backend.model.Batch;
import online.mdfactory.backend.model.Employee;
import online.mdfactory.backend.model.Shift;
import online.mdfactory.backend.service.EmployeeService;
import online.mdfactory.backend.service.JsonParserService;
import online.mdfactory.backend.service.ShiftService;
import online.mdfactory.backend.service.TimeReportService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.WebInitParam;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@RestController
@RequestMapping("/shift")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private JsonParserService jsonParserService;
    @Autowired
    private TimeReportService timeReportService;
    @Autowired
    private EmployeeService employeeService;
    private final Logger logger = LoggerFactory.getLogger(ShiftService.class);

    @PostMapping("/start")
    public void startShift(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        String employeeLogin = jsonObject.getString("employeeLogin");
        ZoneId zoneId = ZoneId.of(jsonObject.getString("zoneId"));
        String startTimeJson = jsonObject.getString("startTime");
        LocalTime startTime = jsonParserService.parse(startTimeJson, LocalTime.class);

        logger.info(employeeLogin + " " + zoneId + " " + startTime);
        shiftService.startShift(employeeLogin, zoneId, startTime);
        writeLog(employeeLogin, zoneId);
    }

    @PostMapping("/is_started")
    public ResponseEntity<Boolean> isShiftStarted(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        String employeeLogin = jsonObject.getString("employeeLogin");
        ZoneId zoneId = ZoneId.of(jsonObject.getString("zoneId"));

        logger.info(employeeLogin + " " + zoneId);
        Optional<Shift> shift = shiftService.findTodayShift(employeeLogin, zoneId);
        return new ResponseEntity<>(shift.isPresent(), HttpStatus.OK);
    }

    @PostMapping("/add_batch")
    public void addBatch(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        String employeeLogin = jsonObject.getString("employeeLogin");
        ZoneId zoneId = ZoneId.of(jsonObject.getString("zoneId"));
        String batchJson = jsonObject.getString("batch");
        Batch batch = jsonParserService.parse(batchJson, Batch.class);

        logger.info(employeeLogin + " " + zoneId + " " + batch);
        shiftService.addBatch(employeeLogin, zoneId, batch);
        writeLog(employeeLogin, zoneId);
    }

    @PostMapping("/record_break")
    public void recordBreak(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        String employeeLogin = jsonObject.getString("employeeLogin");
        ZoneId zoneId = ZoneId.of(jsonObject.getString("zoneId"));
        String breakDurationJson = jsonObject.getString("breakDuration");
        Duration breakDuration = jsonParserService.parse(breakDurationJson, Duration.class);

        String durationFormatted = String.format("%d:%02d:%02d",
                breakDuration.toHoursPart(),
                breakDuration.toMinutesPart(),
                breakDuration.toSecondsPart());
        logger.info(employeeLogin + " " + zoneId + " " + durationFormatted);
        shiftService.recordBreak(employeeLogin, zoneId, breakDuration);
        writeLog(employeeLogin, zoneId);
    }

    @PostMapping("/finish")
    public void finishShift(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        String employeeLogin = jsonObject.getString("employeeLogin");
        ZoneId zoneId = ZoneId.of(jsonObject.getString("zoneId"));
        String finishTimeJson = jsonObject.getString("finishTime");
        LocalTime finishTime = jsonParserService.parse(finishTimeJson, LocalTime.class);

        logger.info(employeeLogin + " " + zoneId + " " + finishTime);
        shiftService.finishShift(employeeLogin, zoneId, finishTime);
        writeLog(employeeLogin, zoneId);
    }

    @PostMapping("/is_finished")
    public ResponseEntity<Boolean> isShiftFinished(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        String employeeLogin = jsonObject.getString("employeeLogin");
        ZoneId zoneId = ZoneId.of(jsonObject.getString("zoneId"));

        logger.info(employeeLogin + " " + zoneId);
        boolean isShiftFinished = shiftService.isShiftFinished(employeeLogin, zoneId);
        return new ResponseEntity<>(isShiftFinished, HttpStatus.OK);
    }

    @DeleteMapping("/{employeeLogin}")
    @Transactional
    public void deleteTodayShift(@PathVariable String employeeLogin) {
        shiftService.deleteTodayShift(employeeLogin);
    }

    private void writeLog(String employeeLogin, ZoneId zoneId) {
        LocalDate today = LocalDate.now(zoneId);
        Path path = buildLogPath(employeeLogin, today);

        try(XSSFWorkbook workbook = timeReportService.buildSingleLogEntryXls(employeeLogin, today);
            OutputStream out = Files.newOutputStream(path, CREATE_NEW)) {

            workbook.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path buildLogPath(String employeeLogin, LocalDate today) {
        Employee employee = employeeService.get(employeeLogin).orElseThrow();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return Path.of("D:")
                .resolve("reports")
                .resolve("time")
                .resolve(employee.getName())
                .resolve(dtf.format(today));
    }
}
