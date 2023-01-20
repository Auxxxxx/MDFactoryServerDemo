package online.mdfactory.backend.repository;

import online.mdfactory.backend.model.Employee;
import online.mdfactory.backend.model.Operation;
import online.mdfactory.backend.model.OperationGroup;
import online.mdfactory.backend.model.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationGroupRepository extends JpaRepository<OperationGroup, Long> {


}
