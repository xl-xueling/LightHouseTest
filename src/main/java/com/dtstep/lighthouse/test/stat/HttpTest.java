package com.dtstep.lighthouse.test.stat;

import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.OkHttpUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  使用Java模拟Http接口调用上报统计消息，Web端显示统计结果约有1分钟延迟
 *
 *  参考文档：https://dtstep.com/docs/110039/
 * */
public class HttpTest {

    private static final String token = "N4C:order_stat";

    private static final String secretKey = "AMV9PYUAQ29aPdN78Nl2dpkhcH4YsA87hvDzZI8t2";

    public static void main(String[] args) throws Exception {
//        send();
        batchSend();
    }

    /**
     * 单条消息上报（不推荐您使用这个接口）
     */
    public static void send() throws Exception {
        //单机模式的http接口地址为当前节点，集群模式默认前三个节点的ip地址都提供查询服务，可以轮询访问。
        String apiUrl = "http://10.206.6.47:18101/api/rpc/v1/stat";
        long t = System.currentTimeMillis();
        for(int i=0;i<1676;i++){
            Map<String,Object> requestMap = new HashMap<>();
            requestMap.put("token",token);
            requestMap.put("secretKey",secretKey);
            requestMap.put("timestamp",t);
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("order_id", RandomID.id(6));
            paramsMap.put("biz", RandomID.id(2));
            paramsMap.put("user_id", RandomID.id(6));
            Double d = ThreadLocalRandom.current().nextDouble(1000);
            paramsMap.put("amount",String.format("%.3f", d));//防止上面随机数出现科学计数法
            requestMap.put("params",paramsMap);
            String requestParams = JsonUtil.toJSONString(requestMap);
            System.out.println("Send Params:" + JsonUtil.toJSONString(requestParams));
            String response = OkHttpUtil.post(apiUrl,requestParams);
            System.out.println("Response:" + response);
        }
        System.out.println("Send OK!");
    }

    /**
     * 批量上报数据
     * @throws Exception
     */
    public static void batchSend() throws Exception {
        //单机模式的http接口地址为当前节点，集群模式默认前三个节点的ip地址都提供查询服务，可以轮询访问。
        String apiUrl = "http://10.206.6.47:18101/api/rpc/v1/stats";
        long t = System.currentTimeMillis();
        for(int m=0;m<100;m++){
            List<Map<String,Object>> requestList = new ArrayList<>();
            for(int n=0;n<300;n++){
                Map<String,Object> requestMap = new HashMap<>();
                requestMap.put("token",token);
                requestMap.put("secretKey",secretKey);
                requestMap.put("timestamp",t);
                Map<String,Object> paramsMap = new HashMap<>();
                paramsMap.put("order_id", RandomID.id(6));
                paramsMap.put("biz", RandomID.id(2));
                paramsMap.put("user_id", RandomID.id(6));
                Double d = ThreadLocalRandom.current().nextDouble(1000);
                paramsMap.put("amount",String.format("%.3f", d));//防止上面随机数出现科学计数法
                requestMap.put("params",paramsMap);
                requestList.add(requestMap);
            }
            String requestParams = JsonUtil.toJSONString(requestList);
            System.out.println("Send Params:" + JsonUtil.toJSONString(requestParams));
            String response = OkHttpUtil.post(apiUrl,requestParams);
            System.out.println("Response:" + response);
        }
        System.out.println("Send OK!");
    }
}
