package online.mdfactory.backend.service;

import online.mdfactory.backend.model.Operation;
import online.mdfactory.backend.model.OperationGroup;
import online.mdfactory.backend.model.Specification;
import online.mdfactory.backend.repository.OperationGroupRepository;
import online.mdfactory.backend.repository.OperationRepository;
import online.mdfactory.backend.repository.SpecificationRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoadStaticDataService {

    @Autowired
    private OperationGroupRepository operationGroupRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private SpecificationRepository specificationRepository;

    public void parseJson(JSONObject jsonObject) {
        parseOperationGroups(jsonObject.getJSONObject("OperationGroups"));
        parseSpecifications(jsonObject.getJSONObject("Specifications"));
    }

    private void parseOperationGroups(JSONObject operationGroups) {
        for (String s : operationGroups.keySet()) {
            createOperationGroup(operationGroups, s);
        }
    }

    private void createOperationGroup(JSONObject operationGroups, String key) {
        OperationGroup operationGroup = new OperationGroup();
        operationGroup.setId(Long.parseLong(key));
        JSONObject operationGroupJson = operationGroups.getJSONObject(key);
        operationGroup.setName(operationGroupJson.getString("name"));
        saveOperationGroup(operationGroup);

        JSONObject operations = operationGroupJson.getJSONObject("Operations");

        for (String key2 : operations.keySet()) {
            createOperation(operations, key2, operationGroup);
        }

    }

    private void createOperation(JSONObject operations, String key, OperationGroup group) {
        Operation operation = new Operation();
        operation.setId(Long.parseLong(key));
        JSONObject operationJson = operations.getJSONObject(key);
        operation.setName(operationJson.getString("name"));
        operation.setOperationGroup(group);
        saveOperation(operation);
    }

    private void parseSpecifications(JSONObject specifications) {
        for (String s : specifications.keySet()) {
            createSpecification(specifications, s);
        }
    }

    private void createSpecification(JSONObject specifications, String key) {
        Specification specification = new Specification();
        specification.setId(Long.parseLong(key));
        JSONObject specificationJSONObject = specifications.getJSONObject(key);
        specification.setName(specificationJSONObject.getString("name"));

        JSONArray operations = specificationJSONObject.getJSONArray("Operations");
        for (int i = 1; i < operations.length(); i++) {
            Long operationId = Long.parseLong(operations.getString(i));
            Operation operation = operationRepository.findById(operationId).get();
            specification.addOperation(operation);
        }
        saveSpecification(specification);
    }


    public void saveOperationGroup(OperationGroup operationGroup) {
        operationGroupRepository.save(operationGroup);
    }

    public void saveOperation(Operation operation) {
        operationRepository.save(operation);
    }

    public void saveSpecification(Specification specification) {
        specificationRepository.save(specification);
    }

}
