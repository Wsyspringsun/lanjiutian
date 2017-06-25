package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class CommodityTypeSecondModel extends BaseModel {
    //商品id
    private String commodityTypeId;
    //商品名字
    private String name;
    //商品类型等级id
    private String parentMenuId;
    //商品分类等级
    private String menuRank;
    //商品图
    private String imgPath;
    //3级分类
    private List<Children> children;

    public String getCommodityTypeId() {
        return commodityTypeId;
    }

    public void setCommodityTypeId(String commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public String getMenuRank() {
        return menuRank;
    }

    public void setMenuRank(String menuRank) {
        this.menuRank = menuRank;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public List<Children> getChildren() {
        return children;
    }

    public void setChildren(List<Children> children) {
        this.children = children;
    }

    public class Children extends BaseModel{
        //商品id
        private String commodityTypeId;
        //商品名字
        private String name;
        //商品类型等级id
        private String parentMenuId;
        //商品分类等级
        private String menuRank;
        //商品图
        private String imgPath;

        public String getCommodityTypeId() {
            return commodityTypeId;
        }

        public void setCommodityTypeId(String commodityTypeId) {
            this.commodityTypeId = commodityTypeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParentMenuId() {
            return parentMenuId;
        }

        public void setParentMenuId(String parentMenuId) {
            this.parentMenuId = parentMenuId;
        }

        public String getMenuRank() {
            return menuRank;
        }

        public void setMenuRank(String menuRank) {
            this.menuRank = menuRank;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }
    }
}
