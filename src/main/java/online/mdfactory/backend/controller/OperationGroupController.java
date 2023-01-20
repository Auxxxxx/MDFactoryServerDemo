package online.mdfactory.backend.controller;

import online.mdfactory.backend.model.OperationGroup;
import online.mdfactory.backend.service.OperationGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operation_group")
public class OperationGroupController {

    @Autowired
    private OperationGroupService operationGroupService;

    @GetMapping("/getAll")
    public ResponseEntity<List<OperationGroup>> list() {
        return new ResponseEntity<>(operationGroupService.listAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationGroup> get(@PathVariable Long id) {
        OperationGroup operationGroup = operationGroupService.get(id).orElseThrow();
        return new ResponseEntity<>(operationGroup, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        operationGroupService.delete(id);
    }
}
