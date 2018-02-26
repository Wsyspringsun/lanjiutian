package com.wyw.ljtds.model;

import com.baidu.trace.model.OnTraceListener;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2017/5/1 0001.
 */

public class HomePageModel extends BaseModel {
    //单独 的 广告 图片
    private String[] advImgs;
    private List<Map<String, Object>> advImgsList;
    //轮播 图片
    private List<Map<String, String>> headImgsList;
    private String[] headImgs;
    //活动 图片
    private List<DETAILS> activeComms;
    private List<NewsModel> news;
    //推荐 图片
    private List<CLASS> recommendComms;


    public String[] getAdvImgs() {
        return advImgs;
    }

    public void setAdvImgs(String[] advImgs) {
        this.advImgs = advImgs;
    }

    public List<NewsModel> getNews() {
        return news;
    }

    public void setNews(List<NewsModel> news) {
        this.news = news;
    }

    public List<HomePageModel.DETAILS> getActiveComms() {
        return activeComms;
    }

    public void setActiveComms(List<HomePageModel.DETAILS> activeComms) {
        this.activeComms = activeComms;
    }

    public List<CLASS> getRecommendComms() {
        return recommendComms;
    }

    public void setRecommendComms(List<CLASS> recommendComms) {
        this.recommendComms = recommendComms;
    }

    public List<Map<String, String>> getHeadImgsList() {
        return headImgsList;
    }

    public void setHeadImgsList(List<Map<String, String>> headImgsList) {
        this.headImgsList = headImgsList;
    }

    public String[] getHeadImgs() {
        return headImgs;
    }

    public void setHeadImgs(String[] headImgs) {
        this.headImgs = headImgs;
    }

    public List<Map<String, Object>> getAdvImgsList() {
        return advImgsList;
    }

    public void setAdvImgsList(List<Map<String, Object>> advImgsList) {
        this.advImgsList = advImgsList;
    }

//    public class activeComms extends HomePageModel.DETAILS {
//
//    }


    public class CLASS {
        private String CLASSCODE;
        private String CLASSNAME;
        private List<DETAILS> DETAILS;

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

        public List<HomePageModel.DETAILS> getDETAILS() {
            return DETAILS;
        }

        public void setDETAILS(List<HomePageModel.DETAILS> DETAILS) {
            this.DETAILS = DETAILS;
        }
    }

    public class DETAILS {
        private String IMG_PATH;
        private String WARENAME;
        private String WAREID;
        private BigDecimal SALEPRICE;
        private String LOGISTICS_COMPANY_ID;
        private String LOGISTICS_COMPANY;

        public String getLOGISTICS_COMPANY_ID() {
            return LOGISTICS_COMPANY_ID;
        }

        public void setLOGISTICS_COMPANY_ID(String LOGISTICS_COMPANY_ID) {
            this.LOGISTICS_COMPANY_ID = LOGISTICS_COMPANY_ID;
        }

        public String getLOGISTICS_COMPANY() {
            return LOGISTICS_COMPANY;
        }

        public void setLOGISTICS_COMPANY(String LOGISTICS_COMPANY) {
            this.LOGISTICS_COMPANY = LOGISTICS_COMPANY;
        }

        public String getIMG_PATH() {
            return IMG_PATH;
        }

        public void setIMG_PATH(String IMG_PATH) {
            this.IMG_PATH = IMG_PATH;
        }

        public String getWARENAME() {
            return WARENAME;
        }

        public void setWARENAME(String WARENAME) {
            this.WARENAME = WARENAME;
        }

        public String getWAREID() {
            return WAREID;
        }

        public void setWAREID(String WAREID) {
            this.WAREID = WAREID;
        }

        public BigDecimal getSALEPRICE() {
            return SALEPRICE;
        }

        public void setSALEPRICE(BigDecimal SALEPRICE) {
            this.SALEPRICE = SALEPRICE;
        }
    }
}
