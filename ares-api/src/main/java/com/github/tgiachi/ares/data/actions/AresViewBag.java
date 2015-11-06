package com.github.tgiachi.ares.data.actions;

import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.base.BaseResult;
import lombok.Data;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bag di ritorno di una view
 */
@Data
public class AresViewBag  extends BaseResult {

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


