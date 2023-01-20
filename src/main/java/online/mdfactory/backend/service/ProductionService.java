package online.mdfactory.backend.service;

import online.mdfactory.backend.model.Production;
import online.mdfactory.backend.repository.ProductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionService {
    @Autowired
    private ProductionRepository productionRepository;

    public void save(Production production) {
        productionRepository.save(production);
    }

}
