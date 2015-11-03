package com.github.tgiachi.ares.interfaces.database;

import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Interfaccia per creare le tipologia di query
 */
public interface IAresQuery extends Closeable {

    Statement getQuery();

    void prepare(String text, String ... args);

    ResultSet getResultSet() throws SQLException;

    void execute() throws SQLException;

    /**
     * Prova di mappatura di un oggetto / bean
     *
     * @return
     */
    <T> T mapResultToObject(Class<?> classz);
}
