package org.projw.blackserver.services;

public interface TokenService {
    String generateToken(Object obj);
    Object getObjectFromToken(String token);
    boolean isTokenValid(String token);
}
