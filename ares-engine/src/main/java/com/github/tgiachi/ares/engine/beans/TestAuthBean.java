package com.github.tgiachi.ares.engine.beans;

import com.github.tgiachi.ares.annotations.container.AresBean;
import com.github.tgiachi.ares.annotations.container.AresInject;
import com.github.tgiachi.ares.data.auth.AuthUser;
import com.github.tgiachi.ares.interfaces.auth.IAuthProcessor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Bean per testare l'autenticazione
 */
@AresBean
public class TestAuthBean implements IAuthProcessor {

    @AresInject
    private Logger logger;

    @Override
    public AuthUser authenticate(String username, String password) {
        log(Level.INFO, "Simulating authenticate for username: %s with password %s", username, password);

        AuthUser user = new AuthUser();
        user.setLevel(1);
        user.setLoginDate(new Date());
        user.setUsername(username);
        user.setExtra("Administrator");

        return user;
    }


    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}
