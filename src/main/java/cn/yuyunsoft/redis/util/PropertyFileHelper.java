package cn.yuyunsoft.redis.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @功能: 读取配置文件工具类
 * @作者: 黄小云
 * @版本: v1.0.0
 * @时间: 2016年12月10日
 * @描述: 提供一个读取配置文件的工具类
 *
 */
public class PropertyFileHelper {
	private static PropertyFileHelper instance =  null;
	private static final String SYSCONFIG_PATH =   "/conf/sys-conf.properties";

	private Properties sysConfigProp = new Properties();
	private PropertyFileHelper(){

	}

	public static PropertyFileHelper getInstance() {
		if (instance == null){
			instance = new PropertyFileHelper();
			instance.reload();
		}
		return instance;
	}

	public String getSysconfigConfig(String key){
		return sysConfigProp.getProperty(key);
	}

	/**
	 * 重新加载配置资源
	 */
	private void reload() {
		this.loadProperties(sysConfigProp,SYSCONFIG_PATH);
	}

	/**
	 * 加载资源文件loadProperties
	 * @param prop
	 * @param path
	 */
	private void loadProperties(Properties prop, String path) {
		InputStream is = null;
		try {
			is = PropertyFileHelper.class.getResourceAsStream(path);
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if (is != null) is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(PropertyFileHelper.getInstance().getSysconfigConfig("ID_QUEUE_SIZE"));
	}

}

