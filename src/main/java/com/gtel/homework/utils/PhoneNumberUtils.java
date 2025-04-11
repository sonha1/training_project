package com.gtel.homework.utils;

import com.gtel.homework.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;

public class PhoneNumberUtils {

    public static String validatePhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER , "phoneNumber is invalid");
        }

        phoneNumber = phoneNumber.replaceAll(" " , "");
        // check length :
        //+84982573860
        //0982573860
        //84982573860

        if (phoneNumber.length() < 10 || phoneNumber.length() > 13) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER , "phoneNumber is invalid");
        }
        // check prefix
        if (!phoneNumber.startsWith("0") && !phoneNumber.startsWith("+84") && !phoneNumber.startsWith("84")) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER , "phoneNumber is invalid");
        }

        if (phoneNumber.startsWith("84")) {
            return  phoneNumber;
        }

        if (phoneNumber.startsWith("0")) {
            return  "84" + phoneNumber.substring(1);
        }

        if (phoneNumber.startsWith("+84")) {
            return phoneNumber.substring(1);
        }

        return phoneNumber;

    }


    public static void main(String[] args) {
        System.out.println(validatePhoneNumber("+84982573860"));
        System.out.println(validatePhoneNumber("84982573860"));
        System.out.println(validatePhoneNumber("0982573860"));
    }
}
