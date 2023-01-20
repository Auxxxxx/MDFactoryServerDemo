package online.mdfactory.backend.controller;

import online.mdfactory.backend.model.Operation;
import online.mdfactory.backend.service.OperationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/operation")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @GetMapping("/getAll")
    public List<Operation> list() {
        return operationService.listAll();
    }

    @PostMapping("/performed_operation_options")
    public List<Operation> performedOperationOptions(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        String employeeLogin = jsonObject.getString("employeeLogin");
        Long specificationId = jsonObject.getLong("specificationId");

        return operationService.performedOperationOptions(employeeLogin, specificationId);
    }
}
