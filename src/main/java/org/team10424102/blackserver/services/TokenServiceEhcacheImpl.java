package org.team10424102.blackserver.services;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.team10424102.blackserver.config.SpringSecurityUserAdapter;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

@Service
public class TokenServiceEhcacheImpl implements TokenService {
    public static final int TOKEN_EVICT_PERIOD = 30 * 60 * 1000; // half an hour
    private static final Cache tokenCache = CacheManager.getInstance().getCache("tokenCache");

    public String generateToken(Object obj) {
        // http://stackoverflow.com/questions/2513573/how-good-is-javas-uuid-randomuuid
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        // url safe base64
        String token = Base64.getUrlEncoder().encodeToString(bb.array());
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
