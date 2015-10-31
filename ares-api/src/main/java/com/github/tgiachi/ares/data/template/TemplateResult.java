package com.github.tgiachi.ares.data.template;

import lombok.Data;

import java.io.Serializable;

/**
 * Risultato del template / pagina
 */
@Data
public class TemplateResult implements Serializable {

    private String result;

    private boolean error;

    private String errorString;

    private long generationTime;
}
