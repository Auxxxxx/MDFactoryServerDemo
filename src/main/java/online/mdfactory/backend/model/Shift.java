package online.mdfactory.backend.model;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDate date;
    @NotNull
    private String employeeLogin;
    @NotNull
    private LocalTime startTime;
    @Nullable
    private LocalTime finishTime;
    @NotNull
    private Duration breakDuration = Duration.of(0, ChronoUnit.SECONDS);
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "shift_batches", joinColumns = {
            @JoinColumn(name = "shift_date", referencedColumnName = "date"),
            @JoinColumn(name = "shift_login", referencedColumnName = "employeeLogin"),
    })
    private List<Batch> batches = new ArrayList<>();

    public Shift() {
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public void addBatch(Batch batch) {
        batches.add(batch);
    }

    public List<Batch> getBatches() {
        return batches;
    }

    public Batch firstBatch() {
        return batches.get(0);
    }

    public Batch lastBatch() {
        return batches.get(batches.size() - 1);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEmployeeLogin() {
        return employeeLogin;
    }

    public void setEmployeeLogin(String employeeLogin) {
        this.employeeLogin = employeeLogin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shift)) return false;
        Shift shift = (Shift) o;
        return id.equals(shift.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Duration getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(Duration breakDuration) {
        this.breakDuration = breakDuration;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "date=" + date +
                ", employeeLogin='" + employeeLogin + '\'' +
                '}';
    }
}
