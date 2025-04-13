package com.gtel.homework.utils;

import com.gtel.homework.common.Const;
import com.gtel.homework.exception.ApplicationException;

public class StringUtils {

    public static void validatePassword(String password) throws ApplicationException {
        if (password.matches(Const.ValidateRegex.PASSWORD)) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "password is invalid");
        }
    }

    public static void validateEmail(String email) throws ApplicationException {

    }

    public static void validateUsername(String username) throws ApplicationException {
        if (username.length() >= 6 && username.length() <= 20 &&
                Character.isLetter(username.charAt(0)) &&
                username.matches("^[a-zA-Z0-9._]+$") &&
                !username.contains(" ") &&
                !username.contains("..") &&
                !username.contains("__") &&
                !username.contains("._") &&
                !username.contains("_.")) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "username is invalid");
        }
        // Kiểm tra bắt đầu bằng chữ cái
        // Kiểm tra ký tự hợp lệ: chỉ chữ cái tiếng Anh, số, _ và .
        // Không chứa khoảng trắng
        // Không chứa 2 ký tự _, . liên tiếp hoặc kết hợp _. hoặc ._
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equalsIgnoreCase(str.trim());
    }

    public static String getStringLike(String content) {
        if (isNullOrEmpty(content)) {
            return "%%";
        }

        return "%" + TextUtils.normalize(content).toLowerCase() + "%";
    }
}
