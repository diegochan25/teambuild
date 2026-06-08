package org.typecrafters.teambuild.converter;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InetAddressConverter implements AttributeConverter<InetAddress, String> {

    @Override
    public String convertToDatabaseColumn(InetAddress attr) {
        return attr == null ? null : attr.getHostAddress();
    }

    @Override
    public InetAddress convertToEntityAttribute(String attr) {
        try {
            return attr == null ? null : InetAddress.getByName(attr);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
