package online.mdfactory.backend.excel;

import online.mdfactory.backend.model.Batch;
import online.mdfactory.backend.model.Shift;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TimeReportLine {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime finishTime;
    private Duration batchDuration = Duration.ZERO;
    private Duration breakDuration = Duration.ZERO;
    private Duration innerWaitingDuration = Duration.ZERO;
    private Duration outerWaitingDuration = Duration.ZERO;
    private Boolean isFinishedManually;

    private TimeReportLine(Shift shift) {
        if (shift == null || shift.getDate() == null) return;
        date = shift.getDate();
        startTime = shift.getStartTime();
        isFinishedManually = shift.getFinishTime() != null;
        if (isFinishedManually) {
            finishTime = shift.getFinishTime();
        } else {
            finishTime = LocalDate.now().atTime(LocalTime.MAX).toLocalTime();
        }
        List<Batch> batches = shift.getBatches();

        batchDuration = batches
                .stream()
                .map(Batch::getDuration)
                .reduce(Duration.ZERO, Duration::plus, Duration::plus);

        breakDuration = batches
                .stream()
                .map(Batch::getBreakDuration)
                .reduce(Duration.ZERO, Duration::plus, Duration::plus)
                .plus(shift.getBreakDuration());

        if (batches.size() > 0) {
            Duration gap1 = Duration.between(startTime, shift.firstBatch().getStartTime()).abs();
            Duration gap2 = Duration.between(finishTime, shift.lastBatch().getFinishTime()).abs();
            outerWaitingDuration = gap1.plus(gap2);
        }

        if (batches.size() > 1) {
            for (int i = 0; i < batches.size() - 1; i++) {
                LocalTime lastFinishTime = batches.get(i).getFinishTime();
                LocalTime nextStartTime = batches.get(i + 1).getStartTime();
                Duration d = Duration.between(lastFinishTime, nextStartTime).abs();
                innerWaitingDuration = innerWaitingDuration.plus(d);
            }
        }
    }

    public static TimeReportLine of(@NonNull Shift shift) {
        return new TimeReportLine(shift);
    }

    public static TimeReportLine empty() {
        return new TimeReportLine(null);
    }

    public void plus(TimeReportLine timeReportLine) {
        batchDuration = batchDuration.plus(timeReportLine.batchDuration);
        breakDuration = breakDuration.plus(timeReportLine.breakDuration);
        innerWaitingDuration = innerWaitingDuration.plus(timeReportLine.innerWaitingDuration);
        outerWaitingDuration = outerWaitingDuration.plus(timeReportLine.outerWaitingDuration);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public Duration getBatchDuration() {
        return batchDuration;
    }

    public Duration getBreakDuration() {
        return breakDuration;
    }

    public Duration getInnerWaitingDuration() {
        return innerWaitingDuration;
    }

    public Duration getOuterWaitingDuration() {
        return outerWaitingDuration;
    }

    public Boolean getFinishedManually() {
        return isFinishedManually;
    }
}
