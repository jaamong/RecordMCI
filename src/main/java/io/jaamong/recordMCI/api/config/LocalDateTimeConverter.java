package io.jaamong.recordMCI.api.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Converts between Java LocalDateTime and SQLite TEXT format.
 *
 * SQLite stores CURRENT_TIMESTAMP as "YYYY-MM-DD HH:MM:SS" format.
 * This converter handles bidirectional conversion.
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.format(FORMATTER);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(dbData, FORMATTER);
    }
}