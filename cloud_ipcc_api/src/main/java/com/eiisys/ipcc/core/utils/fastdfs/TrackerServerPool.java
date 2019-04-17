package com.eiisys.ipcc.core.utils.fastdfs;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eiisys.ipcc.exception.IpccException;



@Component
public class TrackerServerPool {
    @Autowired
    private GenericObjectPool<TrackerServer> trackerServerPool;



    /**
     * 获取 TrackerServer
     * @return TrackerServer
     * @throws SmsException
     */
    public TrackerServer borrowObject() throws IpccException {
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerServerPool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof IpccException){
                throw (IpccException) e;
            }
        }
        return trackerServer;
    }

    /**
     * 回收 TrackerServer
     * @param trackerServer 需要回收的 TrackerServer
     */
    public void returnObject(TrackerServer trackerServer){

        trackerServerPool.returnObject(trackerServer);
    }


}
