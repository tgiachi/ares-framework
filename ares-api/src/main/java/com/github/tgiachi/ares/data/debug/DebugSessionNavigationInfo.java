package com.github.tgiachi.ares.data.debug;

import com.github.tgiachi.ares.annotations.actions.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.log4j.Level;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

/**
 * Oggetto di debug per salvare le informazioni di debug
 */
@Data
@AllArgsConstructor
public class DebugSessionNavigationInfo implements Serializable {

    private HttpServletRequest request;

    private RequestType type;

    private String sessionId;

    private Date date = new Date();

    private String navigateUrl;

    private Level level;

    private String source;

    private String log;

    private int resultCode = 200;

    public DebugSessionNavigationInfo()
    {

    }

}
