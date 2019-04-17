package com.eiisys.ipcc.core.utils.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.stereotype.Component;

import com.eiisys.ipcc.core.utils.fastdfs.TrackerServerFactory;


/*******************************
 * * 版权所有：快服科技
 * * 创建日期: 2018/4/9 10:51
 * * 创建作者: Kevin_Ge
 * * 版本:  1.0
 * * 功能:
 * * 最后修改时间:
 * * 修改记录:
 ********************************/
@Component
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@PropertySource("classpath:config/${spring.profiles.active}/fastDFS.properties")
public class FastDFSConfig {

    private static Logger logger = LoggerFactory.getLogger(FastDFSConfig.class);

    public static int MAX_STORAGE_CONNECTION;
    /**
     * 文件服务器地址
     */
    public static String FILE_SERVER_ADDR;

    /**
     * FastDFS秘钥
     */
    public static String FAST_DFS_HTTP_SECRET_KEY;

    @Value("${fastdfs.connect_timeout_in_seconds}")
    private Integer connect_timeout_in_seconds;
    @Value("${fastdfs.network_timeout_in_seconds}")
    private Integer network_timeout_in_seconds;
    @Value("${fastdfs.charset}")
    private String charset;
    @Value("${fastdfs.http_anti_steal_token}")
    private boolean http_anti_steal_token;
    @Value("${fastdfs.http_secret_key}")
    private String http_secret_key;
    @Value("${fastdfs.http_tracker_http_port}")
    private Integer http_tracker_http_port;
    @Value("${fastdfs.tracker_servers}")
    private String tracker_servers;
    @Value("${max_storage_connection}")
    public void setMaxStorageConnection(int maxStorageConnection) {
        MAX_STORAGE_CONNECTION = maxStorageConnection;
    }
    @Value("${file_server_addr}")
    public void setFileServerAddr(String fileServerAddr) {
        FILE_SERVER_ADDR = fileServerAddr;
    }
    @Value("${fastdfs.http_secret_key}")
    public void setFastDfsHttpSecretKey(String fastDfsHttpSecretKey) {
        FAST_DFS_HTTP_SECRET_KEY = fastDfsHttpSecretKey;
    }

    @Bean("trackerServer")
    public GenericObjectPool<TrackerServer> trackerServerGenericObjectPool(){
        Properties properties = new Properties();
        properties.put("fastdfs.http_secret_key", http_secret_key);
        properties.put("fastdfs.connect_timeout_in_seconds", connect_timeout_in_seconds);
        properties.put("fastdfs.network_timeout_in_seconds", network_timeout_in_seconds);
        properties.put("fastdfs.charset", charset);
        properties.put("fastdfs.http_anti_steal_token", http_anti_steal_token);
        properties.put("fastdfs.http_secret_key", http_secret_key);
        properties.put("fastdfs.http_tracker_http_port", http_tracker_http_port);
        properties.put("fastdfs.tracker_servers", tracker_servers);

            try {
                // 加载配置文件
                ClientGlobal.initByProperties(properties);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MyException e) {
                e.printStackTrace();
            }

            if(logger.isDebugEnabled()){
                logger.debug("ClientGlobal configInfo: {}", ClientGlobal.configInfo());
            }

            // Pool配置
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMinIdle(2);
            if(MAX_STORAGE_CONNECTION > 0){
                poolConfig.setMaxTotal(MAX_STORAGE_CONNECTION);
            }
        return new GenericObjectPool<>(new TrackerServerFactory(), poolConfig);
    }
}
