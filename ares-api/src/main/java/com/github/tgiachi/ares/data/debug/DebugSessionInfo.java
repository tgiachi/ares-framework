package com.github.tgiachi.ares.data.debug;

import lombok.Data;

import java.io.Serializable;

/**
 * Oggetto di debug per salvare le informazioni della sessione
 */
@Data
public class DebugSessionInfo implements Serializable {

    private String sessionId;

    private String remoteIp;
}
