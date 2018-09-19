package com.yiyun.lockcontroller.net;

/**
 * Created by joker on 2017/7/12.
 */

public class ApiException extends RuntimeException {
    // 公共异常 4000 开头
    // 未知异常
    public static final int UNKNOWN_EXCEPTION = 4000; //"未知异常"
    // 数据库异常
    public static final int DATABASE_ERROR_EXCEPTION = 4001;//后台数据库异常
    public static final int OLD_PASSWORD_ERROR_EXCEPTION = 4002;//原密码错误
    // LockControl 3000 开头
    public static final int LOCK_PKI_EMPTY_EXCEPTION = 10001;//数据库中不存在该用户的对称密钥
    public static final int LOCK_SERVER_ENCRYPT_EXCEPTION = 10002;//服务端加密异常
    public static final int LOCK_SERVER_AES_EMPTY_EXCEPTION = 10003;//数据库中不存在该用户的对称密钥
    public static final int LOCK_SERVER_DENCRYPT_EXCEPTION = 10004;//服务端解密异常
    public static final int LOCK_GET_STUNAME_EXCEPTION = 10005;//获取用户名为空
    public static final int LOCK_SERVER_UNKNOWN_EXCEPTION = 10006;//未知错误
    public static final int LOCK_AES_KEY_EMPTY_EXCEPTION = 30001;//aesKey为空
    public static final int LOCK_STU_NO_EMPTY_EXCEPTION = 30002;//userpwd为空
    public static final int LOCK_STU_NAME_EMPTY_EXCEPTION = 30003;//customerType为空
    public static final int LOCK_ID_CARD_EMPTY_EXCEPTION = 30004;//telPhone为空
    public static final int LOCK_LOCK_NO_EMPTY_EXCEPTION = 30005;//lockNo为空
    public static final int LOCK_CHANGE_DEP_EMPTY_EXCEPTION = 30006;//changeDep为空
    public static final int LOCK_REASON_EMPTY_EXCEPTION = 30007;//reason为空
    public static final int LOCK_START_TIME_EMPTY_EXCEPTION = 30008;//startTime为空
    public static final int LOCK_END_TIME_EMPTY_EXCEPTION = 30009;//endTime为空
    public static final int LOCK_AUTO_STU_NO_EMPTY_EXCEPTION = 30010;//autoStuNo为空
    public static final int LOCK_AUTO_COUNT_EMPTY_EXCEPTION = 30011;//autoCount为空
    public static final int LOCK_LOCK_CIPHER_EMPTY_EXCEPTION = 30012;//cipher为空
    public static final int LOCK_USERNAME_EMPTY_EXCEPTION = 30013;//username为空
    public static final int LOCK_AES_USER_EMPTY_EXCEPTION = 11001;//用户eid不存在
    public static final int LOCK_KEY_STORAGE_EXCEPTION = 11002;//密钥入库失败
    public static final int LOCK_USER_EID_EMPTY_EXCEPTION = 11003;//用户eid不存在
    public static final int LOCK_PASSWORD_ERROR_EXCEPTION = 11004;//账号密码错误
    public static final int LOCK_STU_MISMATCH_EXCEPTION = 12001;//身份证、姓名认证信息不匹配
    public static final int LOCK_STU_BIND_AUTO_EXCEPTION = 12002;//学生信息已被他人认证
    public static final int LOCK_STU_BIND_FAILE_EXCEPTION = 12003;//学生信息绑定失败
    public static final int LOCK_STU_DB_ERROR_EXCEPTION = 12004;//用户信息入库失败
    public static final int LOCK_REGISTER_USERNAME_REPEAT_EXCEPTION = 12005;//用户名已被注册
    public static final int LOCK_REGISTER_PHONE_REPEAT_EXCEPTION = 12006;//电话号码已被注册
    public static final int LOCK_STU_BIND_EMPTY_EXCEPTION = 13001;//未绑定学生信息
    public static final int LOCK_LOCK_NO_AUTO_EXCEPTION = 13002;//没有开锁权限
    public static final int LOCK_REPLACE_REPEAT_EXCEPTION = 14001;//上一个更换寝室申请尚未处理
    public static final int LOCK_REPLACE_SAME_EXCEPTION = 14002;//更换寝室对象与所在寝室相同冲突
    public static final int LOCK_REPLACE_FAILE_EXCEPTION = 14003;//更换寝室请求录入失败
    public static final int LOCK_REPLACE_NO_EXCEPTION = 15001;//没有更换寝室申请记录
    public static final int LOCK_REPLACE_EMPTY_EXCEPTION = 16001;//不存在未审核的更换寝室的请求
    public static final int LOCK_REPLACE_CAL_EXCEPTION = 16002;//取消更换寝室操作失败
    public static final int LOCK_AUTO_NO_AUTO_EXCEPTION = 17002;//无权授予锁具

    public static final int LOCK_AUTO_MAX_EXCEPTION = 14009;//授权次数大于99
    public static final int LOCK_AUTO_REPEAT_EXCEPTION_2 = 14002; // 被授权人已经有了这把锁的权限
    public static final int LOCK_AUTO_REPEAT_EXCEPTION = 14003;//此用户已被授予开此锁的权限

    public static final int LOCK_AUTO_STU_EMPTY_EXCEPTION = 17004;//授予权限的学号不存在或者没有绑定学生信息
    public static final int LOCK_AUTO_ERROR_EXCEPTION = 17005;//授权类型不符合规则
    public static final int LOCK_AUTO_DB_EXCEPTION = 17006;//授予权限的数据库操作失败
    public static final int LOCK_AUTO_OBJECT_EXCEPTION = 17007;//用户给自己授权开锁
    public static final int LOCK_AUTO_NO_EXISTENT_EXCEPTION = 18001;//授权不存在或者已失效
    public static final int LOCK_AUTO_CAL_EXCEPTION = 18002;//取消授权的数据库操作失败
    public static final int LOCK_REPAIR_EXISTENCE_EXCEPTION = 19001;//该寝室锁具已报修
    public static final int LOCK_REPAIR_DB_ERROR_EXCEPTION = 19002;//数据库操作失败
    public static final int LOCK_BIND_EMPTY_EXCEPTION = 21001;//用户未绑定学生信息
    public static final int LOCK_REPAIR_EMPTY_EXCEPTION = 21002;//不存在报修记录
    public static final int LOCK_REPAIR_CANCLE_DB_ERROR_EXCEPTION = 22001;//取消报修时数据库操作失败
    public static final int LOCK_AUTO_PATTERN_EXCEPTION = 23001;//Pattern请求模式不符合规范
    public static final int LOCK_AUTO_START_END_EXCEPTION = 23002;//请求结束条数小于开始条数
    public static final int LOCK_AUTO_LOG_EMPTY_EXCEPTION = 23003;//查询授权记录为空
    public static final int LOCK_OPEN_LOG_UNBIND_EXCEPTION = 24001;//该用户未绑定学生信息
    public static final int LOCK_OPEN_LOG_EMPTY_EXCEPTION = 24002;//查询开锁记录为空
    private final int errCode;

    public ApiException(int errCode) {
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
