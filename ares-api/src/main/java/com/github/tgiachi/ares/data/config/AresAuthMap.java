package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;

/**
 * Configurazione per impostare i methodi di login/logout
 */
@Data
public class AresAuthMap implements Serializable {

    private String loginAction = "authorize.doHomepage";

    private String logoutAction = "authorize.doLogout";

    private String loginPostAction = "authorize.doLoginPost";

    private String loginJsonPostAction = "doLoginPostJson";


}
