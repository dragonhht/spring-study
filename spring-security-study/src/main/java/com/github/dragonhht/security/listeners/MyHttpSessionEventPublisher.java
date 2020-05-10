package com.github.dragonhht.security.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * session监听.
 *
 * @author: dragonhht
 * @Date: 2019-11-20
 */
//@Component
public class MyHttpSessionEventPublisher implements HttpSessionListener {

    //@Autowired
    private ApplicationContext applicationContext;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("---------sessionCreated-----------");
        HttpSessionCreatedEvent createdEvent = new HttpSessionCreatedEvent(se.getSession());
        // 重新发布session发布事件
        applicationContext.publishEvent(createdEvent);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("---------sessionDestroyed-----------");
        HttpSessionDestroyedEvent destroyedEvent = new HttpSessionDestroyedEvent(se.getSession());
        // 重新发布session销毁事件
        applicationContext.publishEvent(destroyedEvent);
    }
}
