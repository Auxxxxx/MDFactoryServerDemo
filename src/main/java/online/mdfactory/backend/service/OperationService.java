package online.mdfactory.backend.service;

import online.mdfactory.backend.model.Operation;
import online.mdfactory.backend.repository.OperationRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperationService {
    
    @Autowired
    private OperationRepository operationRepository;

    public List<Operation> listAll() {
        return operationRepository.findAll();
    }

    public List<Operation> performedOperationOptions(String employeeLogin, Long specificationId) {
        return operationRepository.performedOperationOptions(employeeLogin, specificationId);
    }

    public Optional<Operation> get(Long id) {
        return operationRepository.findById(id);
    }
}
