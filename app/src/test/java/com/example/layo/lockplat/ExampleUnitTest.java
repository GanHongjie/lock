package com.example.layo.lockplat;

import com.yiyun.lockcontroller.utils.bluetoothlock.BlueReciveMsgBody;
import com.yiyun.lockcontroller.utils.bluetoothlock.SendAgreeClass;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testCheckInfo() {
        assertEquals(4, 2 + 2);
        System.out.println("hello test!");
        // SendAgreeClass: 手机发送的数据：2a000200022d6fa07e
        String data = "2a000200022d6fa07e";
        SendAgreeClass sendAgreeClass = new SendAgreeClass(data);
        BlueReciveMsgBody reciveMsgBody = new BlueReciveMsgBody();
        reciveMsgBody.initData(sendAgreeClass.getBody());
        boolean judge = reciveMsgBody.judgeCheckSum();
        System.out.println("judge = " + judge);
        // checkSum=a0, calc result=a0
    }
}