package online.mdfactory.backend.repository;

import online.mdfactory.backend.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByDateAndEmployeeLogin(LocalDate localDate, String employeeLogin);

    @Query(value =
            "SELECT *" +
            "FROM shift as s " +
            "WHERE " +
            "   s.employee_login = :employeeLogin AND " +
            "   s.date >= :start AND " +
            "   s.date <= :end ",
            nativeQuery = true)
    List<Shift> findShiftsInPeriod(String employeeLogin, LocalDate start, LocalDate end);

    void removeShiftByDateAndEmployeeLogin(LocalDate localDate, String employeeLogin);
}
