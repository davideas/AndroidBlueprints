package eu.davidea.blueapp.persistence.db;

import android.arch.persistence.room.TypeConverter;

import eu.davidea.blueapp.viewmodels.enums.EnumAuthority;
import eu.davidea.blueapp.viewmodels.enums.EnumMessageStatus;
import eu.davidea.blueapp.viewmodels.enums.EnumUserStatus;

public class EnumConverters {

    @TypeConverter
    public static String fromEnum(EnumAuthority value) {
        // TODO: Handle null values
        return value.name();
    }

    @TypeConverter
    public static String fromEnum(EnumUserStatus value) {
        // TODO: Handle null values
        return value.name();
    }

    @TypeConverter
    public static String fromEnum(EnumMessageStatus value) {
        // TODO: Handle null values
        return value.name();
    }

    @TypeConverter
    public static EnumAuthority authorityToString(String value) {
        return EnumAuthority.valueOf(value);
    }

    @TypeConverter
    public static EnumUserStatus userStatusToString(String value) {
        return EnumUserStatus.valueOf(value);
    }

    @TypeConverter
    public static EnumMessageStatus messageStatusToString(String value) {
        return EnumMessageStatus.valueOf(value);
    }

}