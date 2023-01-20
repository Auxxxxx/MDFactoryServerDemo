package online.mdfactory.backend.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalTime locTime) {
        return locTime == null ? null : Timestamp.valueOf(locTime.atDate(LocalDate.now()));
    }

    @Override
    public LocalTime convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime().toLocalTime();
    }
}