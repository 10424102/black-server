package org.team10424102.blackserver.config.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Converter(autoApply = true)
public class InetAddressConverter implements AttributeConverter<InetAddress, String> {
    @Override
    public String convertToDatabaseColumn(InetAddress attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getHostAddress();
    }

    @Override
    public InetAddress convertToEntityAttribute(String dbData) {
        // If a literal IP address is supplied, only the validity
        // of the address format is checked. (without DNS lookup)
        try {
            return InetAddress.getByName(dbData);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
