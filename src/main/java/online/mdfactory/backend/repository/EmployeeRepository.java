package online.mdfactory.backend.repository;

import online.mdfactory.backend.model.Employee;
import online.mdfactory.backend.model.OperationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    @Query(value =
            "INSERT INTO employee_operation_group (employee_login, operation_group_id) " +
            "VALUES (:employee_login, :operation_group_id)",
            nativeQuery = true)
    void bindOperationGroups(@Param("employee_login") String employeeLogin,
                     @Param("operation_group_id") Long operationGroupId);
}
