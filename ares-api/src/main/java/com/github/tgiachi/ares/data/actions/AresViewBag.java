package com.github.tgiachi.ares.data.actions;

import com.github.tgiachi.ares.data.template.DataModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Bag di ritorno di una view
 */
@Data
public class AresViewBag implements Serializable {

    private DataModel model;

    private String viewPage;


    public AresViewBag(String viewPage, DataModel model)
    {
        this.model = model;
        this.viewPage = viewPage;
    }

    public AresViewBag(DataModel model)
    {
        this.model = model;
    }

    public AresViewBag()
    {

    }

}


