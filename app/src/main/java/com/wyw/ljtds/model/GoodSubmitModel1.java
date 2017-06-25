package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class GoodSubmitModel1 extends BaseModel {
    private List<GoodSubmitModel2> DETAILS;

    public List<GoodSubmitModel2> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<GoodSubmitModel2> DETAILS) {
        this.DETAILS = DETAILS;
    }
}
