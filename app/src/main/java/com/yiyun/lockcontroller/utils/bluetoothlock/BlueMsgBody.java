package com.yiyun.lockcontroller.utils.bluetoothlock;

/**
 * 蓝牙收发的参数bean
 * Created by Layo on 2018-1-15.
 */

abstract public class BlueMsgBody {
    public static final String BT_SEND_TYPE = "00"; //指令代码
    public static final String BT_RECEIVE_TYPE = "01"; //指令代码

    public static final String BT_COMMAND_GET = "01"; //获取设备信息
    public static final String BT_COMMAND_OPEN = "02"; //开锁
    public static final String BT_LOCK_DEFAULT= "00"; //开锁
    public static final String BT_LOCK_OPEN = "21"; //开锁
    public static final String BT_LOCK_CLOSE = "22"; //开锁
    public static final String BT_COMMAND_INIT = "03"; //密钥
    public static final String BT_COMMAND_ERROR = "11"; //错误
    public static final String BT_COMMAND_RESPONSE = "0f"; //应答

    public static final String BT_ERR_LENGTH = "e1";//长度错误
    public static final String BT_ERR_LENGTH1 = "e2";//长度与实际长度不一致
    public static final String BT_ERR_CHECK = "e3";//数据包校验错误
    public static final String BT_ERR_RANDOM = "e4";//随机数检验错误

    protected String type; //指令代码
    protected String command; //指令操作
    protected String pl;  //参数长度，十六进制
    protected String parameter;
    protected String checkSum ;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
