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
    private static final String callerName = "caller:api_waimai_order";

    //注意1：为调用方申请相应工程、统计项、数据视图的授权并且审核通过后，系统由于缓存原因约5分钟后生效。
    //注意2：此处为当前调用方的秘钥，不是统计组的秘钥。
    private static final String callerKey = "J2s7GQr81wYnlyGS4jeBsUGCy3EqoPSWro8xohbh";

    public static void testDataQuery() throws Exception {
        int statId = 1100607;
        String dimensValue = null;
        long t = System.currentTimeMillis();
        long startTime = DateUtil.getDayStartTime(t);
        long endTime = DateUtil.getDayEndTime(t);

        /**
         *  statId为对应统计项ID，dimensValue为纬度值，startTime和endTime为查询起止时间范围。
         *
         *  dimensValue传值说明：对于单维度统计，直接传值即可，多维度统计请使用分号分割。
         *  示例：<stat-item  title="每小时_各省份_访问uv" stat="bitcount(ime)" dimens="province" />，此时province请传入要查询的省份信息，比如：山东省、广东省。
         *  示例：<stat-item  title="每小时_各省份_各业务_访问uv" stat="bitcount(ime)" dimens="province;biz" />，此时province请传入要查询的省份信息，比如：山东省;手机业务、广东省;家电业务。
         */
        List<StatValue> statValues = LightHouse.dataDurationQuery(callerName,callerKey,statId,dimensValue,startTime,endTime);
        for (StatValue statValue : statValues) {
            //返回结果：batchTime为对应批次时间，dimensValue为相应纬度值，value为统计结果,statesValue如果统计项中包含多个统计函数，则按按顺序返回每一个统计函数的结果
            System.out.println("batchTime:" + statValue.getDisplayBatchTime() + ",dimensValue:" + statValue.getDimensValue() + ",value:" + statValue.getValue()
                    + ",statesValue:" + JsonUtil.toJSONString(statValue));
        }
        System.out.println("ok!");
    }
}
