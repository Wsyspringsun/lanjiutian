package com.wyw.ljtds.utils;

import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.SqlFavoritesModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class SqlUtils {
    /**
     * 数据库操作的DaoConfig
     * @return
     */
    public static DbManager.DaoConfig getDaoConfig() {
        //本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName( "lanjiutian.db" )//数据库名字
                .setDbVersion( 1 )//设置数据库的版本
                .setAllowTransaction( true )//设置允许开启事务
                .setDbDir( new File( AppConfig.CACHE_ROOT_NAME, AppConfig.CACHE_DB_ROOT_NAME ) )//数据库路径
                .setDbOpenListener( new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                } )
                .setDbUpgradeListener( new DbManager.DbUpgradeListener() {//设置一个版本升级的监听方法
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                } );

        return daoConfig;
    }


    /**
     * 删除
     * @param id
     */
    public static void delete(String id){
        DbManager dbManager = x.getDb( SqlUtils.getDaoConfig() );
        try {
            dbManager.deleteById( SqlFavoritesModel.class,id );
        } catch (DbException e) {
            e.printStackTrace();
        }
    }



}
