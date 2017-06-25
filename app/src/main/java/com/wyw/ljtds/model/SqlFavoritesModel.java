package com.wyw.ljtds.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

@Table( name = "favorites")
public class SqlFavoritesModel extends BaseModel {
    //商品名字
    @Column( name = "name" )
    private String name;
    //图片路径
    @Column( name = "image" )
    private String image;
    //价格
    @Column( name = "money" )
    private String money;
//    //是否有效
//    @Column( name = "status" )
//    private String status;
    //id
    @Column( name = "_id",isId = true,autoGen = false)
    private String _id;
    //店铺
    @Column( name = "group")
    private String group;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


}
