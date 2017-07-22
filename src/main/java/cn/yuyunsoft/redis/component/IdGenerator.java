package cn.yuyunsoft.redis.component;

import cn.yuyunsoft.redis.util.PropertyFileHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 *
 * @功能: ID自增长生成器
 * @作者: 黄小云
 * @版本: v1.0.0
 * @时间: 2016年12月10日
 * @描述: 当初始化bean组件时，加载并读取配置文件产生一段ID_QUEUE_SIZE这么长的自增长ID值，并存储到队列（长度为ID_QUEUE_SIZE）中，一旦被客户端消费完后，则又重新生成.
 * 表名 +_+ maxid+_+ queue表示存放在redis中对应表的队列名，表名 +_+ maxid 表示存放在redis中对应表的maxid
 *
 */
@Component("idGenerator")
public class IdGenerator {
    private String tableName;
    private static int ID_QUEUE_SIZE = Integer.parseInt(PropertyFileHelper.getInstance().getSysconfigConfig("ID_QUEUE_SIZE"));

    @Autowired
    @Qualifier("objectRedisTemplate")
    private RedisTemplate objectRedisTemplate;

    /**
     * 根据表名生成表的主键自增长ID值，然后入队列到redis
     * @param tableName
     */
    public  void setTableLastMaxIdValue(String tableName) {
        String tableMaxId = PropertyFileHelper.getInstance().getSysconfigConfig(tableName + ".init.maxid");
        String sMaxId =  StringUtils.isEmpty(tableMaxId)?(String)objectRedisTemplate.opsForValue().get(tableName +"_maxid"):tableMaxId;
        long maxId = 0L;
        long maxRunMaxCount =  ID_QUEUE_SIZE;
        long size =  objectRedisTemplate.opsForList().size(tableName + "_maxid_queue") ;
        if (size != 0) {
            return;
        }

        LinkedList<String> maxidList = new LinkedList<String>();
        if (!StringUtils.isEmpty(sMaxId)) {
            maxId =  Long.valueOf(sMaxId);
            maxRunMaxCount += maxId;
        }

        while(maxId  < maxRunMaxCount) {
            maxId++;
            maxidList.add(String.valueOf(maxId));
        }
        objectRedisTemplate.opsForList().leftPushAll(tableName + "_maxid_queue",maxidList);

    }

    /**
     * 根据表名获取主键自增长ID值，主要是从redis队列中出队列
     * @param tableName
     * @return
     */
    public  String getTableLastMaxIdValue(String tableName) {
        String maxId =  (String) objectRedisTemplate.opsForList().rightPop(tableName + "_maxid_queue");
        if (StringUtils.isEmpty(maxId)) {
            setTableLastMaxIdValue(tableName);
            maxId = (String) objectRedisTemplate.opsForList().rightPop(tableName + "_maxid_queue");
        }
        objectRedisTemplate.opsForValue().set(tableName +"_maxid",maxId);

        return maxId;
    }

}
