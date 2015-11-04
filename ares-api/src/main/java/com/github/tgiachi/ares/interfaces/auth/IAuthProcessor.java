package com.github.tgiachi.ares.interfaces.auth;

import com.github.tgiachi.ares.data.auth.AuthUser;

/**
 * Interfaccia per creare il processore che
 * permette di autorizzare gli utenti
 */
public interface IAuthProcessor {

    AuthUser authenticate(String username, String password);


}
