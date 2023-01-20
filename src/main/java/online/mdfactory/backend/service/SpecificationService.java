package online.mdfactory.backend.service;

import online.mdfactory.backend.model.Specification;
import online.mdfactory.backend.repository.SpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecificationRepository specificationRepository;

    public List<Specification> listAll() {
        return specificationRepository.findAll();
    }

    public Specification get(Long id) {
        return specificationRepository.findById(id).get();
    }
}