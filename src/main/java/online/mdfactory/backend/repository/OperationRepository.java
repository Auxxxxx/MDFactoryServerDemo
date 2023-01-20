package online.mdfactory.backend.repository;

import online.mdfactory.backend.model.Operation;
import online.mdfactory.backend.model.OperationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Query(value =
            "SELECT " +
            "   o.id, " +
            "   o.name," +
            "   o.operation_group_id " +
            "FROM " +
            "   operation as o " +
            "LEFT JOIN employee_operation_group as eog " +
            "   ON o.operation_group_id = eog.operation_group_id " +
            "LEFT JOIN specification_operation as so " +
            "   ON o.id = so.operation_id " +
            "WHERE " +
            "   eog.employee_login = :employeeLogin AND " +
            "   so.specification_id = :specificationId ",
            nativeQuery = true)
    List<Operation> performedOperationOptions(String employeeLogin, Long specificationId);
}
