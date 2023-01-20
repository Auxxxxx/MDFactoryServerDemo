package online.mdfactory.backend.controller;

import online.mdfactory.backend.model.Employee;
import online.mdfactory.backend.model.Production;
import online.mdfactory.backend.service.EmployeeService;
import online.mdfactory.backend.service.ProductionService;
import online.mdfactory.backend.service.JsonParserService;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.nio.file.StandardOpenOption.APPEND;

@RestController
@RequestMapping("/production")
public class ProductionController {
    @Autowired
    private ProductionService productionService;
    @Autowired
    private JsonParserService jsonParserService;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/record")
    public void recordProduction(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        ZoneId zoneId = ZoneId.of(jsonObject.getString("zoneId"));
        String itinerary = jsonObject.getString("itinerary");
        String productionJson = jsonObject.getString("production");
        Production production = jsonParserService.parse(productionJson, Production.class);
        productionService.save(production);
        Path logPath = buildLogPath(zoneId, production.getEmployeeLogin());
        writeLog(logPath, itinerary, production);
    }

    private void writeLog(Path path, String itinerary, Production production) {

        try(BufferedWriter out = Files.newBufferedWriter(path, APPEND)) {
            Long millis = production.getDuration().toMillis();

            String operationId = String.valueOf(production.getOperationId());
            String duration = DurationFormatUtils.formatDuration(millis, "H:mm:ss", true);
            String entry = String.join(" ", itinerary, operationId, duration);
            out.write(entry);
            out.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path buildLogPath(ZoneId zoneId, String employeeLogin) {
        LocalDate today = LocalDate.now(zoneId);
        Employee employee = employeeService.get(employeeLogin).orElseThrow();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return Path.of("D:")
                .resolve("reports")
                .resolve("operation")
                .resolve(employee.getName())
                .resolve(dtf.format(today));
    }
}
