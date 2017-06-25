package com.wyw.ljtds.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class HomePageModel1 extends BaseModel {
    private String[] headImgs;
    private List<NewsModel> news;
    private List<RecommendComms> recommendComms;

    public String[] getHeadImgs() {
        return headImgs;
    }

    public void setHeadImgs(String[] headImgs) {
        this.headImgs = headImgs;
    }

    public List<NewsModel> getNews() {
        return news;
    }

    public void setNews(List<NewsModel> news) {
        this.news = news;
    }

    public List<RecommendComms> getRecommendComms() {
        return recommendComms;
    }

    public void setRecommendComms(List<RecommendComms> recommendComms) {
        this.recommendComms = recommendComms;
    }

    public class RecommendComms {
        private String imgPath;
        private String name;
        private String commodityTypeId;
        private String menuRank;
        private List<goods> commodityList;
        private boolean flg;

        public boolean isFlg() {
            return flg;
        }

        public void setFlg(boolean flg) {
            this.flg = flg;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCommodityTypeId() {
            return commodityTypeId;
        }

        public void setCommodityTypeId(String commodityTypeId) {
            this.commodityTypeId = commodityTypeId;
        }

        public String getMenuRank() {
            return menuRank;
        }

        public void setMenuRank(String menuRank) {
            this.menuRank = menuRank;
        }

        public List<goods> getCommodityList() {
            return commodityList;
        }

        public void setCommodityList(List<goods> commodityList) {
            this.commodityList = commodityList;
        }
    }

    public class goods {
        private String commodityBrand;
        private String commodityId;
        private BigDecimal costMoney;
        private String groupName;
        private String imgPath;
        private String oidGroupId;
        private String title;

        public String getCommodityBrand() {
            return commodityBrand;
        }

        public void setCommodityBrand(String commodityBrand) {
            this.commodityBrand = commodityBrand;
        }

        public String getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(String commodityId) {
            this.commodityId = commodityId;
        }

        public BigDecimal getCostMoney() {
            return costMoney;
        }

        public void setCostMoney(BigDecimal costMoney) {
            this.costMoney = costMoney;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getOidGroupId() {
            return oidGroupId;
        }

        public void setOidGroupId(String oidGroupId) {
            this.oidGroupId = oidGroupId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
