package com.github.tgiachi.ares.engine.persistence;

import com.github.tgiachi.ares.annotations.database.AresEntity;
import com.github.tgiachi.ares.annotations.database.AresEntityField;
import lombok.Data;

import java.io.Serializable;

/**
 * Entita' per provare il mapping dei dati nelle query
 */
@Data
@AresEntity("tables")
public class TableInfo implements Serializable {

    @AresEntityField("table_catalog")
    private String tableCatalog;

    @AresEntityField("table_schema")
    private String tableSchema;

    @AresEntityField("table_name")
    private String tableName;

    @AresEntityField("table_type")
    private String tableType;

    //@AresEntityField("is_insertable_into")
    //private String isInsertableInto;

}
