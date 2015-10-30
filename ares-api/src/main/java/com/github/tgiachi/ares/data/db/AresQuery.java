package com.github.tgiachi.ares.data.db;

import lombok.Data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Oggetto di costruzione query
 */
@Data
public class AresQuery implements Serializable {

    private Statement query;

    private Connection connection;
}
