package com.gtel.homework.service;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class BaseService {

    public Integer getParam(Map<String, String> params, String key){

        if (params.get(key) == null || StringUtils.isBlank(params.get(key)))
            return null;

        try {
            return Integer.valueOf(params.get(key));
        }catch (Exception e) {
            return null;
        }

    }

    public Integer getParam(Map<String, String> params , String key, int defaultValue){

        if (params.get(key) == null || StringUtils.isBlank(params.get(key)))
            return defaultValue;

        try {
            return Integer.valueOf(params.get(key));
        }catch (Exception e) {
            return defaultValue;
        }
    }

    public int validatePage(int page){
        return page < 1 ? 1 : page;
    }

    public int validatePageSize(int pageSize){
        return pageSize < 1 ? 10 : pageSize;
    }
}
