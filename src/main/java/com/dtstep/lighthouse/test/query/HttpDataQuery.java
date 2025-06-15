package com.dtstep.lighthouse.test.query;

import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用java模拟Http接口查询某个统计项的统计结果数据
 *
 * 参考文档：https://dtstep.com/docs/110040/
 *
 */
public class HttpDataQuery {

    private static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws Exception {
        testDataQuery();
    }

    /**
     * Api查询统计结果，请您首先创建调用方，并且为调用方申请相应统计工程或统计项的访问授权
     */
    private static final String callerName = "caller:api_waimai_order";

    //注意1：为调用方申请相应工程、统计项、数据视图的授权并且审核通过后，系统由于缓存原因约5分钟后生效。
    //注意2：此处为当前调用方的秘钥，不是统计组的秘钥。
    private static final String callerKey = "6a7lVhHULhOydaaNeNrE852SVUmlPBePaXORf1V0";

    public static void testDataQuery() throws Exception {
        //单机模式的http接口地址为当前节点，集群模式默认前三个节点的ip地址都提供查询服务，可以轮询访问。
        String apiUrl = "http://10.206.6.47:18101/api/rpc/v1/dataQuery";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100610");
        List<Long> batchList = new ArrayList<>();
        batchList.add(DateUtil.parseDate("2024-09-05 09:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 08:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 07:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 06:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 05:00:00","yyyy-MM-dd HH:mm:ss"));
        /**
         *  dimensValue传值说明：对于单维度统计，直接传值即可，多维度统计请使用分号分割
         *  示例：<stat-item  title="每小时_各省份_访问uv" stat="bitcount(ime)" dimens="province" />，此时province请传入要查询的省份信息，比如：山东省、广东省。
         *  示例：<stat-item  title="每小时_各省份_各业务_访问uv" stat="bitcount(ime)" dimens="province;biz" />，此时province请传入要查询的省份信息，比如：山东省;手机业务、广东省;家电业务。
         */
        requestMap.put("dimensValue","山东省");
        requestMap.put("batchList",batchList);
        String requestParams = JsonUtil.toJSONString(requestMap);
        System.out.println("Body Params:" + JsonUtil.toJSONString(requestParams));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),requestParams);
        Request request = new Request.Builder()
                .header("Caller-Name",callerName)
                .header("Caller-Key",callerKey)
                .url(apiUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string()) ;
        }
    }
}
