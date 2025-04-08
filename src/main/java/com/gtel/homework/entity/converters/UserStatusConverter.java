package com.gtel.homework.entity.converters;

import com.gtel.homework.utils.USER_STATUS;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<USER_STATUS, Integer> {
    @Override
    public Integer convertToDatabaseColumn(USER_STATUS userStatus) {
        return userStatus.getValue();
    }

    @Override
    public USER_STATUS convertToEntityAttribute(Integer integer) {
        return USER_STATUS.fromValue(integer);
    }
}
