package com.gtel.homework.utils;

import com.gtel.homework.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;

public class PhoneNumberUtils {

    public static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\d+")) {
            if (phoneNumber.startsWith("+84")) {
                phoneNumber = phoneNumber.substring(1);
            }

            if (phoneNumber.startsWith("0") && phoneNumber.length() == 10) {
                return "84" + phoneNumber.substring(1);
            }

            if (phoneNumber.startsWith("84") && phoneNumber.length() == 11) {
                return phoneNumber;
            }
        }

        throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "phoneNumber is invalid");
    }

    public static void main(String[] args) {
        System.out.println(validatePhoneNumber("+84982573860"));
        System.out.println(validatePhoneNumber("842573860"));
        System.out.println(validatePhoneNumber("0982573860"));
    }
}
