package cn.yuyunsoft.redis.controller;

import cn.yuyunsoft.redis.component.IdGenerator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;


/**
 *
 * @功能: id自增长生成器Controller
 * @作者: 黄小云
 * @版本: v1.0.0
 * @时间: 2016年12月10日
 * @描述: id自增长生成器Controller
 *
 */
@RestController
public class IdGeneratorController {

    private final Logger logger = Logger.getLogger(this.getClass().toString());

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 根据表名获取最大ID的接口
     * @param tableName
     * @return
     */
    @RequestMapping(value = "/getLastMaxId" ,method = RequestMethod.GET ,produces="text/html;charset=UTF-8")
    @ApiOperation(notes = "getLastMaxId", httpMethod = "GET", value = "根据表名获取最大ID的接口")
    public String getLastMaxId(@ApiParam(required = true, name = "tableName", value = "表名") @RequestParam String tableName) {
        String lastMaxIdValue =   idGenerator.getTableLastMaxIdValue(tableName);
        return lastMaxIdValue;
    }

    /**
     * 根据表名存储队列长度大小的自增长ID值到redis，从而提升性能，防止每次都生成自增长，避免请求效率太低
     * @param tableName
     * @return
     */
    @RequestMapping(value = "/setLastMaxId" ,method = RequestMethod.GET ,produces="text/html;charset=UTF-8")
    @ApiOperation(notes = "setLastMaxId", httpMethod = "GET", value = "根据表名存储自增长ID到redis")
    public String setLastMaxId(@ApiParam(required = true, name = "tableName", value = "表名") @RequestParam String tableName) {
        idGenerator.setTableLastMaxIdValue(tableName);
        return "set setLastMaxId ok";
    }

    /**
     * 开启100个线程来根据表名获取最大ID的接口,仅用于测试
     * @param tableName
     * @return
     */
    @RequestMapping(value = "/setMaxIds" ,method = RequestMethod.GET ,produces="text/html;charset=UTF-8")
    @ApiOperation(notes = "setMaxIds", httpMethod = "GET", value = "开启100个线程来根据表名获取最大ID的接口(test)")
    public String setMaxIds(@ApiParam(required = true, name = "tableName", value = "表名") @RequestParam String tableName) {
        for(int i = 0;i< 100;i++) {
            new Runnable() {
                @Override
                public void run() {
                    String lastMaxIdValue =   idGenerator.getTableLastMaxIdValue(tableName);
                    logger.info("最后自增长的id值为:" + lastMaxIdValue);
                }
            }.run();
        }
        return "ok";
    }

}