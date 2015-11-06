package com.github.tgiachi.ares.data.template;

import com.github.tgiachi.ares.data.template.base.BaseResult;
import lombok.Data;

import java.io.Serializable;

/**
 * Risultato del template / pagina
 */
@Data
public class TemplateResult extends BaseResult {

    private String result;

    private boolean error;

    private String errorString;

    private long generationTime;
}
