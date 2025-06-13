package com.dtstep.lighthouse.test.query;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;

import java.util.List;

/**
 * 使用java查询某个统计项的统计结果数据
 *
 * 参考文档：https://dtstep.com/docs/110038/
 */
public class JavaDataQuery {

    public static void main(String[] args) throws Exception {
        //修改rpc服务注册中心地址,集群模式为一主一从，默认为部署集群的前两个节点IP,使用逗号分割，单机模式为当前节点IP
        //LightHouse.init("10.206.6.11:4061,10.206.6.12:4061");//集群模式初始化
        LightHouse.init("10.206.6.47:4061");//单机模式初始化
        testDataQuery();
    }

    /**
     * Api查询统计结果，请您首先创建调用方，并且为调用方申请相应统计工程或统计项的访问授权
     */
    private static final String callerName = "caller:app_waimai_order";

    private static final String callerKey = "6a7lVhHULhOydaaNeNrE852SVUmlPBePaXORf1V0";//注意：此处为当前调用方的秘钥，不是统计组的秘钥

    public static void testDataQuery() throws Exception {
        int statId = 1100607;
        String dimensValue = null;
        long t = System.currentTimeMillis();
        long startTime = DateUtil.getDayStartTime(t);
        long endTime = DateUtil.getDayEndTime(t);
        //statId为对应统计项ID，dimensValue为纬度值，startTime和endTime为查询起止时间范围
        List<StatValue> statValues = LightHouse.dataDurationQuery(callerName,callerKey,statId,dimensValue,startTime,endTime);
        for (StatValue statValue : statValues) {
            //返回结果：batchTime为对应批次时间，dimensValue为相应纬度值，value为统计结果,statesValue如果统计项中包含多个统计函数，则按按顺序返回每一个统计函数的结果
            System.out.println("batchTime:" + statValue.getDisplayBatchTime() + ",dimensValue:" + statValue.getDimensValue() + ",value:" + statValue.getValue()
                    + ",statesValue:" + JsonUtil.toJSONString(statValue));
        }
        System.out.println("ok!");
    }
}
