package com.github.tgiachi.ares.interfaces.dispacher;

import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.data.actions.ServletResult;

import java.util.HashMap;
import java.util.List;

/**
 * Sistema per il routing delle view, static, ecc...
 */
public interface IAresDispacher {

    ServletResult dispach(String action, RequestType type, HashMap<String, String> headers, HashMap<String, String> values);
}