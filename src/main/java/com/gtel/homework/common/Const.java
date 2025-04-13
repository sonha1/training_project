package com.gtel.homework.common;

public class Const {
    public static class ValidateRegex {
        public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\[\\]{}<>.?])(?!.*\\s).{8,32}$";
    }

    public static class MessageCode {
        public static final String USER_NOT_FOUND = "User not found";
        public static final String USER_ALREADY_EXISTS = "User already exists";
    }
}
