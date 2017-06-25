package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class MessageModel extends BaseModel {
    //id
    private int ID;
    //消息类型
    private String MSG_TYPE;
    //发送人
    private String SENDER;
    //标题
    private String SUBJECT;
    //详细内容
    private String CONTENTS;
    //回复FLG，是否允许回信(0不允许，1允许)
    private String REPLY_FLG;
    //是否开封(0未开封，1开封)
    private String OPEN_FLG;
    //是否删除
    private String DEL_FLG;

   //时间
    private String INS_DATE;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMSG_TYPE() {
        return MSG_TYPE;
    }

    public void setMSG_TYPE(String MSG_TYPE) {
        this.MSG_TYPE = MSG_TYPE;
    }

    public String getSENDER() {
        return SENDER;
    }

    public void setSENDER(String SENDER) {
        this.SENDER = SENDER;
    }

    public String getSUBJECT() {
        return SUBJECT;
    }

    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    public String getCONTENTS() {
        return CONTENTS;
    }

    public void setCONTENTS(String CONTENTS) {
        this.CONTENTS = CONTENTS;
    }

    public String getREPLY_FLG() {
        return REPLY_FLG;
    }

    public void setREPLY_FLG(String REPLY_FLG) {
        this.REPLY_FLG = REPLY_FLG;
    }

    public String getOPEN_FLG() {
        return OPEN_FLG;
    }

    public void setOPEN_FLG(String OPEN_FLG) {
        this.OPEN_FLG = OPEN_FLG;
    }

    public String getDEL_FLG() {
        return DEL_FLG;
    }

    public void setDEL_FLG(String DEL_FLG) {
        this.DEL_FLG = DEL_FLG;
    }
        public String getINS_DATE() {
        return INS_DATE;
    }

    public void setINS_DATE(String INS_DATE) {
        this.INS_DATE = INS_DATE;
    }


}
