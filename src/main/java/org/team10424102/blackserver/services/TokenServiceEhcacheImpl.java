package org.team10424102.blackserver.services;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.team10424102.blackserver.config.security.SpringSecurityUserAdapter;
import org.team10424102.blackserver.models.User;

import java.util.Base64;

@Service
public class TokenServiceEhcacheImpl implements TokenService {
    public static final int TOKEN_EVICT_PERIOD = 30 * 60 * 1000; // half an hour
    private static final Cache tokenCache = CacheManager.getInstance().getCache("tokenCache");

    public String generateToken(Object obj) {
        String token = Base64.getEncoder().encodeToString(RandomUtils.nextBytes(16));
        tokenCache.put(new Element(token, obj));
        // TODO 同一个用户可能会产生多个 token 放在缓存当中, 这是一种资源浪费
        // 理想的做法是先查询该 user 是否已经在缓存当中, 如果在则作废以前的 token, 生成一个新的 token
        // 重用那个已经在缓存当中的 user
        return token;
    }

    @Scheduled(fixedRate = TOKEN_EVICT_PERIOD)
    public void evictExpiredTokens() {
        tokenCache.evictExpiredElements();
    }

    public SpringSecurityUserAdapter getSpringSecurityUserFromToken(String token) {
        Element element = tokenCache.get(token);
        if (element == null) return null;
        return (SpringSecurityUserAdapter)element.getObjectValue();
    }

    public Object getObjectFromToken(String token) {
        Element element = tokenCache.get(token);
        if (element == null) return null;
        return element.getObjectValue();
    }

    public boolean isTokenValid(String token) {
        return tokenCache.isKeyInCache(token);
    }

}
