package com.gtel.homework.utils;

import org.apache.logging.log4j.util.Strings;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

public class TextUtils {
    public static String normalize(String str) {
        str = str.trim();
        str =  str.replaceAll(" +"," ");
        str = str.replaceAll("à|á|ả|ã|ạ|â|ầ|ấ|ẩ|ẫ|ậ|ă|ằ|ắ|ẳ|ẵ|ặ", "a");
        str = str.replaceAll("À|Á|Ả|Ã|Ạ|Â|Ầ|Ấ|Ẩ|Ẫ|Ậ|Ă|Ằ|Ắ|Ẳ|Ẵ|Ặ", "a");
        str = str.replaceAll("ò|ó|ỏ|õ|ọ|ô|ồ|ố|ổ|ỗ|ộ|ơ|ờ|ớ|ở|ỡ|ợ", "o");
        str = str.replaceAll("Ò|Ó|Ỏ|Õ|Ọ|Ô|Ồ|Ố|Ổ|Ỗ|Ộ|Ơ|Ờ|Ớ|Ở|Ỡ|Ợ", "O");
        str = str.replaceAll("ù|ú|ủ|ũ|ụ|ư|ừ|ứ|ử|ữ|ự", "u");
        str = str.replaceAll("Ù|Ú|Ủ|Ũ|Ụ|Ư|Ừ|Ứ|Ử|Ữ|Ự", "U");
        str = str.replaceAll("ì|í|ỉ|ĩ|ị", "i");
        str = str.replaceAll("Ì|Í|Ỉ|Ĩ|Ị", "I");
        str = str.replaceAll("ỳ|ý|ỷ|ỹ|ỵ", "y");
        str = str.replaceAll("Ỳ|Ý|Ỷ|Ỹ|Ỵ", "Y");
        str = str.replaceAll("È|É|Ẻ|Ẽ|Ẹ|Ề|Ế|Ể|Ễ|Ệ", "E");
        str = str.replaceAll("è|é|ẻ|ẽ|ẹ|ề|ế|ể|ễ|ệ", "e");
        str = str.replaceAll("è|é|ẻ|ẽ|ẹ|ề|ế|ể|ễ|ệ", "e");
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("Đ", "D");
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(nfdNormalizedString).replaceAll("");
        return str;
    }

    public static boolean isBlank(String input) {
        if(Strings.isBlank(input)){
            return true;
        }

        return input.trim().isEmpty();
    }

    public static List<String> handleSearchText(String query){
        return List.of(normalize(query).toLowerCase().trim().split(" "));
    }
}
