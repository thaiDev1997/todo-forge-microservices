package com.todoforge.account.converter;

import com.todoforge.account.constant.ScopePermission;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ScopeConverter implements AttributeConverter<ScopePermission, String> {

    @Override
    public String convertToDatabaseColumn(ScopePermission attribute) {
        return attribute == null ? null : attribute.getName();
    }

    @Override
    public ScopePermission convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ScopePermission.fromName(dbData);
    }
}
