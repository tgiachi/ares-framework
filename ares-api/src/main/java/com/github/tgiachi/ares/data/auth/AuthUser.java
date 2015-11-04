package com.github.tgiachi.ares.data.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Oggetto da salvare nella sessione per dopo
 * averlo autorizzato
 */
@Data
public class AuthUser implements Serializable {

    private Object extra;

    private int level;

    private String username;

    private Date loginDate;
}
