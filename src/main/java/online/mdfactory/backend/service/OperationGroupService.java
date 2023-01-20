package online.mdfactory.backend.service;

import online.mdfactory.backend.model.Employee;
import online.mdfactory.backend.model.Operation;
import online.mdfactory.backend.model.OperationGroup;
import online.mdfactory.backend.repository.EmployeeRepository;
import online.mdfactory.backend.repository.OperationGroupRepository;
import online.mdfactory.backend.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OperationGroupService {

    @Autowired
    private OperationGroupRepository operationGroupRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<OperationGroup> listAll() {
        return operationGroupRepository.findAll();
    }

    public Optional<OperationGroup> get(Long id) {
        return operationGroupRepository.findById(id);
    }

    public void delete(Long id) {
        OperationGroup operationGroup = operationGroupRepository.findById(id).orElseThrow();
        List<Operation> operations = operationGroup.getOperations();
        operationRepository.deleteAll(operations);
        List<Employee> employees = operationGroup.getEmployees();
        for (Employee employee : employees) {
            employee.removeOperationGroup(operationGroup);
            employeeRepository.save(employee);
        }
        operationGroupRepository.delete(operationGroup);
    }
}
