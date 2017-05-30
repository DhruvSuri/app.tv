package com.app.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by dhruv.suri on 30/05/17.
 */
@Component
public class Scheduler {
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    SocketService socketService;

    public void proxyListCleaner() {
        socketService.cleanConnectionPool();
    }


    @Scheduled(fixedRate = 2000)
    public void monitorThreadPool(){
        socketService.checkPoolHealth();
    }
}
