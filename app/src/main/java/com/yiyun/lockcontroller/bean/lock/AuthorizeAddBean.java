package com.yiyun.lockcontroller.bean.lock;

import java.io.Serializable;
import java.util.Date;

import static com.yiyun.lockcontroller.utils.DateUtil.str2Date;

/**
 * 授权人的信息
 * Created by Layo on 2017-7-13.
 */
public class AuthorizeAddBean implements Serializable {
    private long startTime;    //允许开锁起始时间
    private long endTime;     //允许开锁结束时间
    private int autoCount;      //允许开锁次数
    private String autoUsername;   //被授权人学号
    private String lockNo;   //锁编号
    private int state;   //授权类型

    public static final int AUTO_STATE_TIME = 1;    //时间段授权
    public static final int AUTO_STATE_COUNT = 2;   //次数授权
    public static final int AUTO_STATE_TIME_COUNT = 3;  //时间段与次数授权

    /**
     * 学生用户
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param autoCount    授权次数
     * @param autoUsername 授权学号
     */
    public AuthorizeAddBean(String startTime, String endTime, String autoCount, String autoUsername, String lockNo, int state) {
        this.autoUsername = autoUsername;
        this.state = state;
        this.lockNo = lockNo;
        initParam(startTime, endTime, autoCount);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAutoUsername() {
        return autoUsername;
    }

    public void setAutoUsername(String autoUsername) {
        this.autoUsername = autoUsername;
    }

    public int getAutoCount() {
        return autoCount;
    }

    public void setAutoCount(int autoCount) {
        this.autoCount = autoCount;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getLockNo() {
        return lockNo;
    }

    public void setLockNo(String lockNo) {
        this.lockNo = lockNo;
    }

    private void initParam(String startTime, String endTime, String autoCount) {
        //这里判断是哪种状态
        if (state == AUTO_STATE_TIME || state == AUTO_STATE_TIME_COUNT) {
            Date startDate = str2Date(startTime);
            Date endDate = str2Date(endTime);
            this.startTime = startDate.getTime();
            this.endTime = endDate.getTime();
        }
        if (state == AUTO_STATE_COUNT || state == AUTO_STATE_TIME_COUNT)
            this.autoCount = Integer.parseInt(autoCount);


    }


}
