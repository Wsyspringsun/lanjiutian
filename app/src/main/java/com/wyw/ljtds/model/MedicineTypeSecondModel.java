package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class MedicineTypeSecondModel extends BaseModel {
    //id
    private String CLASSCODE;
    //名称
    private String CLASSNAME;
    //级别
    private String LEVELS;
    //图片
    private String IMG_PATH;
    //三级分类
    private List<CHILDREN> CHILDREN;

    public String getCLASSCODE() {
        return CLASSCODE;
    }

    public void setCLASSCODE(String CLASSCODE) {
        this.CLASSCODE = CLASSCODE;
    }

    public String getCLASSNAME() {
        return CLASSNAME;
    }

    public void setCLASSNAME(String CLASSNAME) {
        this.CLASSNAME = CLASSNAME;
    }

    public String getLEVELS() {
        return LEVELS;
    }

    public void setLEVELS(String LEVELS) {
        this.LEVELS = LEVELS;
    }

    public String getIMG_PATH() {
        return IMG_PATH;
    }

    public void setIMG_PATH(String IMG_PATH) {
        this.IMG_PATH = IMG_PATH;
    }

    public List<MedicineTypeSecondModel.CHILDREN> getCHILDREN() {
        return CHILDREN;
    }

    public void setCHILDREN(List<MedicineTypeSecondModel.CHILDREN> CHILDREN) {
        this.CHILDREN = CHILDREN;
    }

    public class CHILDREN extends BaseModel{
        private String CLASSCODE;
        private String CLASSNAME;
        private String LEVELS;
        private String IMG_PATH;

        public String getCLASSCODE() {
            return CLASSCODE;
        }

        public void setCLASSCODE(String CLASSCODE) {
            this.CLASSCODE = CLASSCODE;
        }

        public String getCLASSNAME() {
            return CLASSNAME;
        }

        public void setCLASSNAME(String CLASSNAME) {
            this.CLASSNAME = CLASSNAME;
        }

        public String getLEVELS() {
            return LEVELS;
        }

        public void setLEVELS(String LEVELS) {
            this.LEVELS = LEVELS;
        }

        public String getIMG_PATH() {
            return IMG_PATH;
        }

        public void setIMG_PATH(String IMG_PATH) {
            this.IMG_PATH = IMG_PATH;
        }
    }
}
