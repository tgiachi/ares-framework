package com.github.tgiachi.ares.data.template.base;

import lombok.Data;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Result bag di base
 */
@Data
public class BaseResult implements Serializable {
    
    private List<Cookie> cookies = new ArrayList<>();

    private HashMap<String, String> headers = new HashMap<>();

}
