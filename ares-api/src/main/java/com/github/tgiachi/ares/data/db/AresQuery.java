package com.github.tgiachi.ares.data.db;

import com.github.tgiachi.ares.data.debug.GenerationStat;
import com.github.tgiachi.ares.interfaces.database.IAresQuery;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.google.common.base.Stopwatch;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

/**
 * Oggetto di costruzione query
 */
@Data
public class AresQuery implements IAresQuery {

    private Statement query;

    private Connection connection;

    private Stopwatch stopwatch;

    private long executionTime;

    private String sql;

    @Override
    public void prepare(String text, String... args) {
        if (args.length > 0)
            sql = String.format(text, args);
        else
            sql = text;
    }


    @Override
    public ResultSet getResultSet() throws SQLException {
        return query.getResultSet();
    }

    @Override
    public void execute() throws SQLException {

        stopwatch = Stopwatch.createStarted();
        query.execute(sql);
        stopwatch.stop();
        executionTime = stopwatch.elapsed(TimeUnit.MICROSECONDS);

        SessionManager.broadcastMessage("DEBUG_GENERATION", new GenerationStat(getClass(), "QUERY_GENERATION", stopwatch.elapsed(TimeUnit.MICROSECONDS), sql));


    }

    @Override
    public <T> T mapResultToObject(Class<?> classz) {

        return SessionManager.getEngine().getDatabaseManager().mapEntity(this, classz);
    }

    @Override
    public void close() throws IOException {
        try
        {
            query.close();

        }
        catch (Exception ex)
        {

        }
    }
}
