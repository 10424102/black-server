package org.team10424102.blackserver.services;

import java.net.InetAddress;

public interface NetworkService {
    String getLocationFromInetAddress(InetAddress address);
}
