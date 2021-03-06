package cn.uncode.session.data;

import cn.uncode.session.data.memcached.MemcachedPool;
import cn.uncode.session.data.memcached.MemcachedSessionCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.uncode.session.data.redis.RedisSessionCache;

public class SessionCacheManager implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	private static final SessionCacheManager instance = new SessionCacheManager();
	
	private SessionCache sessionCache;
	
	private String beanName;
	
    public static SessionCache getSessionCache() {
    	if(instance.sessionCache != null){
    		return instance.sessionCache;
    	}else{
    		if(StringUtils.isNotEmpty(instance.beanName)){
    			instance.sessionCache = (SessionCache) applicationContext.getBean(instance.beanName);
    		}else{
				if (applicationContext.isTypeMatch("cachePool", MemcachedPool.class)){
					instance.sessionCache = new MemcachedSessionCache();
				} else {
					instance.sessionCache = new RedisSessionCache();
				}
    		}
    	}
        return instance.sessionCache;
    }
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SessionCacheManager.applicationContext = applicationContext;
	}

	public void setSessionCache(SessionCache sessionCache) {
		instance.sessionCache = sessionCache;
	}

	public void setBeanName(String beanName) {
		instance.beanName = beanName;
	}
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
}
