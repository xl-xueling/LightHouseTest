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
 * 使用java模拟Http接口调用查询统计结果数据
 *
 * 参考文档：https://dtstep.com/docs/110040/
 */
public class HttpDataQuery {

    private static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws Exception {
        testDataQuery();
    }

    private static final String callerName = "caller:app_waimai_order";

    private static final String callerKey = "6a7lVhHULhOydaaNeNrE852SVUmlPBePaXORf1V0";//注意：此处为当前调用方的秘钥，不是统计组的秘钥

    public static void testDataQuery() throws Exception {
        String apiUrl = "http://10.206.6.47:18101/api/rpc/v1/dataQuery";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("statId","1100610");
        List<Long> batchList = new ArrayList<>();
        batchList.add(DateUtil.parseDate("2024-09-05 09:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 08:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 07:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 06:00:00","yyyy-MM-dd HH:mm:ss"));
        batchList.add(DateUtil.parseDate("2024-09-05 05:00:00","yyyy-MM-dd HH:mm:ss"));
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
