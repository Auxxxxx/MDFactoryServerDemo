package online.mdfactory.backend.service;

import online.mdfactory.backend.constant.LoginResult;
import online.mdfactory.backend.model.Employee;
import online.mdfactory.backend.model.OperationGroup;
import online.mdfactory.backend.repository.EmployeeRepository;
import online.mdfactory.backend.repository.OperationGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OperationGroupRepository operationGroupRepository;
    @Autowired
    private ShiftService shiftService;

    public List<Employee> listAll() {
        return employeeRepository.findAll();
    }
    public void save(Employee employee) {
        List<OperationGroup> existingOperationGroups = new ArrayList<>();
        for (OperationGroup operationGroup : employee.getOperationGroups()) {
            existingOperationGroups.add(operationGroupRepository.findById(operationGroup.getId()).orElseThrow());
        }
        employee.getOperationGroups().clear();
        employee.getOperationGroups().addAll(existingOperationGroups);
        employeeRepository.save(employee);
    }

    public Optional<Employee> get(String login) {
        return employeeRepository.findById(login);
    }

    public void delete(String login) {
        employeeRepository.deleteById(login);
    }

    public LoginResult checkLogIn(String login, String password, ZoneId zoneId) {
        Optional<Employee> employee = employeeRepository.findById(login);
        if (employee.isEmpty()) return LoginResult.NO_EMPLOYEE;

        boolean validPassword = employee.get().getPassword().equals(password);
        boolean isShiftFinished = shiftService.isShiftFinished(login, zoneId);

        if (validPassword) {
            return isShiftFinished ? LoginResult.SHIFT_FINISHED : LoginResult.OK;
        } else {
            return LoginResult.WRONG_PASSWORD;
        }
    }
}
