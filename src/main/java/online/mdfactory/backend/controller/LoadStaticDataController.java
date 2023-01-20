package online.mdfactory.backend.controller;

import online.mdfactory.backend.model.Employee;
import online.mdfactory.backend.service.EmployeeService;
import online.mdfactory.backend.service.LoadStaticDataService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/load_static_data")
public class LoadStaticDataController {
    @Autowired
    private LoadStaticDataService loadStaticDataService;

    @PostMapping("/json")
    public String add(@RequestBody String json) {
        loadStaticDataService.parseJson(new JSONObject(json));
        return "static_data_loaded";
    }
}
