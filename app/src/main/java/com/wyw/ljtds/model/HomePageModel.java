package com.wyw.ljtds.model;

import com.baidu.trace.api.bos.OnBosListener;
import com.baidu.trace.model.OnTraceListener;
import com.google.gson.internal.ObjectConstructor;

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
    private List<Map<String, String>> advImgsList;
    //轮播 图片
    private List<Map> headImgsList;
    private String[] headImgs;
    //活动 图片
    // 抢好货活动
    private List<DETAILS> activeComms;
    // 公告
    private List<NewsModel> news;
    //推荐 图片
    private List<CLASS> recommendComms;
    //功能图标
    private List<Map<String, String>> iconImgsList;
    // 氛围装饰图 0 显示 1 不显示
	// 氛围装饰图
	private Map<String,Object> atmosphereImg;
    // 功能图标背景图
    private Map<String, Object> iconBackImg;
    // 活动结束倒计时背景图
    private Map<String, Object> activeEndImg;
    // 特价满赠等活动版块图
    private List<Map<String, Object>> activeListImg;
    // 公告背景图
    private Map<String, Object> newsBackImg;
    // 新人专享图
    private Map<String, Object> newlywedsImg;
    // 活动版块图2
    private List<Map<String, Object>> activeListTwoImg;
    // 底部分类或详情展示图
    private List<Map<String, Object>> endGoodsImgsList;


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

    public List<Map> getHeadImgsList() {
        return headImgsList;
    }

    public void setHeadImgsList(List<Map> headImgsList) {
        this.headImgsList = headImgsList;
    }

    public String[] getHeadImgs() {
        return headImgs;
    }

    public void setHeadImgs(String[] headImgs) {
        this.headImgs = headImgs;
    }

    public List<Map<String, String>> getAdvImgsList() {
        return advImgsList;
    }

    public void setAdvImgsList(List<Map<String, String>> advImgsList) {
        this.advImgsList = advImgsList;
    }

    public List<Map<String, String>> getIconImgsList() {
        return iconImgsList;
    }

    public void setIconImgsList(List<Map<String, String>> iconImgsList) {
        this.iconImgsList = iconImgsList;
    }

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
        private String TOP_FLG;
        private BigDecimal SALEPRICE;
        private String COST_POINT;
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

        public String getTOP_FLG() {
            return TOP_FLG;
        }

        public void setTOP_FLG(String TOP_FLG) {
            this.TOP_FLG = TOP_FLG;
        }

        public String getCOST_POINT() {
            return COST_POINT;
        }

        public void setCOST_POINT(String COST_POINT) {
            this.COST_POINT = COST_POINT;
        }
    }

    public Map<String, Object> getAtmosphereImg() {
        return atmosphereImg;
    }

    public void setAtmosphereImg(Map<String, Object> atmosphereImg) {
        this.atmosphereImg = atmosphereImg;
    }

    public Map<String, Object> getIconBackImg() {
        return iconBackImg;
    }

    public void setIconBackImg(Map<String, Object> iconBackImg) {
        this.iconBackImg = iconBackImg;
    }

    public Map<String, Object> getActiveEndImg() {
        return activeEndImg;
    }

    public void setActiveEndImg(Map<String, Object> activeEndImg) {
        this.activeEndImg = activeEndImg;
    }

    public List<Map<String, Object>> getActiveListImg() {
        return activeListImg;
    }

    public void setActiveListImg(List<Map<String, Object>> activeListImg) {
        this.activeListImg = activeListImg;
    }

    public Map<String, Object> getNewsBackImg() {
        return newsBackImg;
    }

    public void setNewsBackImg(Map<String, Object> newsBackImg) {
        this.newsBackImg = newsBackImg;
    }

    public Map<String, Object> getNewlywedsImg() {
        return newlywedsImg;
    }

    public void setNewlywedsImg(Map<String, Object> newlywedsImg) {
        this.newlywedsImg = newlywedsImg;
    }

    public List<Map<String, Object>> getActiveListTwoImg() {
        return activeListTwoImg;
    }

    public void setActiveListTwoImg(List<Map<String, Object>> activeListTwoImg) {
        this.activeListTwoImg = activeListTwoImg;
    }

    public List<Map<String, Object>> getEndGoodsImgsList() {
        return endGoodsImgsList;
    }

    public void setEndGoodsImgsList(List<Map<String, Object>> endGoodsImgsList) {
        this.endGoodsImgsList = endGoodsImgsList;
    }

}
