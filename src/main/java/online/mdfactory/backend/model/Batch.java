package online.mdfactory.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.springframework.cglib.core.GeneratorStrategy;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalTime;

@Embeddable
public class Batch {
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime finishTime;
    @NotNull
    private Duration breakDuration;
    @NotNull
    private Duration duration;

    public Batch() {
    }

    public Batch(LocalTime startTime, LocalTime finishTime, Duration breakDuration, Duration duration) {
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.breakDuration = breakDuration;
        this.duration = duration;
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

    public Duration getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(Duration breakDuration) {
        this.breakDuration = breakDuration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", breakDuration=" + breakDuration +
                ", duration=" + duration +
                '}';
    }
}
