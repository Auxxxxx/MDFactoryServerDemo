package online.mdfactory.backend.repository;

import online.mdfactory.backend.model.Production;
import online.mdfactory.backend.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface ProductionRepository extends JpaRepository<Production, Long> {
    @Query(value =
            "SELECT *" +
            "FROM production as p " +
            "WHERE " +
            "   p.specification_id = :specificationId AND " +
            "   p.operation_id = :operationId AND " +
            "   p.employee_login = :employeeLogin AND " +
            "   p.date >= :start AND " +
            "   p.date <= :end ",
            nativeQuery = true)
    Optional<Production> findProductionInPeriod(Long specificationId,
                                                Long operationId,
                                                LocalDate start,
                                                LocalDate end,
                                                String employeeLogin);
}
