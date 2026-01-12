package io.jaamong.recordMCI.api.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Converts between Java LocalDate and SQLite TEXT format.
 *
 * SQLite stores dates as "YYYY-MM-DD" format.
 * This converter handles bidirectional conversion.
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.format(FORMATTER);
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return LocalDate.parse(dbData, FORMATTER);
    }
}