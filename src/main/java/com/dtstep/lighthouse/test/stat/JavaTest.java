package com.dtstep.lighthouse.test.stat;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.JsonUtil;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  使用Java上报统计消息，Web端显示统计结果约有1分钟延迟
 */
public class JavaTest {

    public static void main(String[] args) throws Exception {
        //LightHouse.init("10.206.6.47:4061,10.206.6.12:4061");//集群模式初始化，默认为部署集群的前两个节点
        LightHouse.init("10.206.6.47:4061");//单机模式初始化
        long t = System.currentTimeMillis();
        for(int i = 0;i<26032;i++){
            //修改统计组参数值、Token和秘钥
            HashMap<String,Object> paramMap = new HashMap<>();
            paramMap.put("order_id", RandomID.id(6));
            paramMap.put("biz", RandomID.id(2));
            paramMap.put("user_id", RandomID.id(6));
            Double d = ThreadLocalRandom.current().nextDouble(1000);
            paramMap.put("amount",String.format("%.3f", d));//防止上面随机数出现科学计数法
            System.out.println("send message:" + JsonUtil.toJSONString(paramMap));
            LightHouse.stat("N4C:order_stat","AMV9PYUAQ29aPdN78Nl2dpkhcH4YsA87hvDzZI8t",paramMap,t);
        }
        System.out.println("send ok.");
        Thread.sleep(30000);//rpc接口使用异步发送，为避免内存中消息还没有发送出去，而进程终止，导致统计不准确，对于进程执行完立即退出的情况(比如junit单元测试)需要加一个sleep。
        System.exit(0);
    }
}
