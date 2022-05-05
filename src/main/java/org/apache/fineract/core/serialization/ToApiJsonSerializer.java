package org.apache.fineract.core.serialization;


public interface ToApiJsonSerializer<T> {

    String serialize(Object object);
}
