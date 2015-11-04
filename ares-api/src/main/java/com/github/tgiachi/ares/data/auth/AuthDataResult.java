package com.github.tgiachi.ares.data.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * Oggetto per la authenticazione in Json
 */
@Data
public class AuthDataResult implements Serializable {

    private String result;
    private boolean authenticated;
}
