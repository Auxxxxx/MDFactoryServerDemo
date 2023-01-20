package online.mdfactory.backend.service;

import online.mdfactory.backend.model.Batch;
import online.mdfactory.backend.model.Shift;
import online.mdfactory.backend.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;

    public void startShift(String employeeLogin, ZoneId zoneId, LocalTime startTime) {
        Shift shift = new Shift();
        shift.setEmployeeLogin(employeeLogin);
        shift.setDate(LocalDate.now(zoneId));
        shift.setStartTime(startTime);
        shiftRepository.save(shift);
    }

    public void finishShift(String employeeLogin, ZoneId zoneId, LocalTime finishTime) {
        Shift shift = shiftRepository.findByDateAndEmployeeLogin(LocalDate.now(zoneId), employeeLogin).orElseThrow();
        shift.setFinishTime(finishTime);
        shiftRepository.save(shift);
    }

    public void addBatch(String employeeLogin, ZoneId zoneId, Batch batch) {
        Shift shift = shiftRepository.findByDateAndEmployeeLogin(LocalDate.now(zoneId), employeeLogin).orElseThrow();
        shift.addBatch(batch);
        shiftRepository.save(shift);
    }

    public void recordBreak(String employeeLogin, ZoneId zoneId, Duration breakDuration) {
        Shift shift = shiftRepository.findByDateAndEmployeeLogin(LocalDate.now(zoneId), employeeLogin).orElseThrow();
        Duration newBreakDuration = shift.getBreakDuration().plus(breakDuration);
        shift.setBreakDuration(newBreakDuration);
        shiftRepository.save(shift);
    }

    public boolean isShiftFinished(String employeeLogin, ZoneId zoneId) {
        Optional<Shift> shift = shiftRepository.findByDateAndEmployeeLogin(LocalDate.now(zoneId), employeeLogin);
        return shift.isPresent() && shift.get().getFinishTime() != null;
    }

    public void deleteTodayShift(String employeeLogin) {
        shiftRepository.removeShiftByDateAndEmployeeLogin(LocalDate.now(), employeeLogin);
    }
    public Optional<Shift> findTodayShift(String employeeLogin, ZoneId zoneId) {
        return shiftRepository.findByDateAndEmployeeLogin(LocalDate.now(zoneId), employeeLogin);
    }
}
