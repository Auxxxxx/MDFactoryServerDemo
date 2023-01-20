package online.mdfactory.backend.controller;

import online.mdfactory.backend.constant.LoginResult;
import online.mdfactory.backend.model.Employee;
import online.mdfactory.backend.service.EmployeeService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Employee>> list() {
        return new ResponseEntity<>(employeeService.listAll(), HttpStatus.OK);
    }

    @PostMapping("/update")
    public void update(@RequestBody Employee employee) {
        employeeService.save(employee);
    }

    @PostMapping("/attempt_log_in")
    public ResponseEntity<String> attemptLogIn(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        String login = jsonObject.getString("login");
        String password = jsonObject.getString("password");
        ZoneId zoneId = ZoneId.of(jsonObject.getString("zoneId"));
        LoginResult loginResult = employeeService.checkLogIn(login, password, zoneId);

        JSONObject response = new JSONObject();
        response.put("loginResultCode", loginResult.getCode());
        response.put("employeeLogin", login);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/{login}")
    public void delete(@PathVariable String login) {
        employeeService.delete(login);
    }
}