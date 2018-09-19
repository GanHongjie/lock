package com.yiyun.lockcontroller.bean.lock;

/**
 * 绑定信息要输入的内容
 * Created by Layo on 2017-7-19.
 */
public class BindStudentBean {
    private String idCard;//身份证号
    private String studNo;//学号
    private String stuName;//学生姓名

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idcard) {
        this.idCard = idcard;
    }

    public String getStudNo() {
        return studNo;
    }

    public void setStudNo(String studno) {
        this.studNo = studno;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuname) {
        this.stuName = stuname;
    }
}
