package com.jm.authcenter.shiro;


import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


/**
 * Created by Administrator on 2018/1/28.
 */
public class ShiroSessionDAO extends CachingSessionDAO {

    private Logger log = LoggerFactory.getLogger(ShiroSessionDAO.class);

    private Cache cache;
    private Cache getCache(){
        /*if(cache == null)
            cache = getCacheManager().getCache(this.getActiveSessionsCacheName());
        return cache;*/
        return getCacheManager().getCache(this.getActiveSessionsCacheName());
    }


    @Override
    protected Serializable doCreate(Session session){
        log.info("sessiondao doCreate {}", session.getId());
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session , sessionId);
        getCache().put(sessionId , session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        log.info("sessiondao doReadSession {}", serializable);
        Object obj = getCache().get(serializable);
        if(obj instanceof Session)
            return (Session)obj;
        return null;
    }

    @Override
    protected void doUpdate(Session session) {
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return; //如果会话过期/停止 没必要再更新了
        }
        log.info("sessiondao doUpdate {}", session.getId());
        getCache().put(session.getId() , session);
    }

    @Override
    protected void doDelete(Session session) {
        log.info("sessiondao doDelete {}", session.getId());
        getCache().remove(session.getId());
    }


}
