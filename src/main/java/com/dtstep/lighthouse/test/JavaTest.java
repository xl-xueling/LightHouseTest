package com.dtstep.lighthouse.test;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class JavaTest {

    public static void main(String[] args) throws Exception {
        LightHouse.init("10.206.6.47:4061");//单机模式初始化
        long t = System.currentTimeMillis();
        for(int i = 0;i<6032;i++){
            //修改统计组参数值、Token和秘钥
            Map<String,Object> map = new HashMap<>();
            map.put("product","abc");
            map.put("thread","abc");
            map.put("scene","abc");
            Double d = ThreadLocalRandom.current().nextDouble(1000);
            map.put("tps",String.format("%.3f", d));//防止上面随机数出现科学计数法
            System.out.println("send info:" + JsonUtil.toJSONString(map));
            LightHouse.stat("_demo_feed_behavior_stat","AMV9PYUAQ29aPdN78Nl2dpkhcH4YsA87hvDzZI8t",map,t);
            if(i / 1000 == 0){
                System.out.println("----test");
            }
        }
        System.out.println("send ok.");
        Thread.sleep(3000000);//client为异步发送，防止进程结束时内存中部分消息没有发送出去
    }
}
