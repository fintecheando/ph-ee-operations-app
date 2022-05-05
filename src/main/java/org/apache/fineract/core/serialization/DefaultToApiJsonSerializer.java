package org.apache.fineract.core.serialization;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * An abstract helper implementation of {@link ToApiJsonSerializer} for resources to serialize their Java data objects
 * into JSON.
 */
@Component
public final class DefaultToApiJsonSerializer<T> implements ToApiJsonSerializer<T> {
    @Override
    public String serialize(final Object object) {
        final Gson gson = new Gson();
        return gson.toJson(object);
    }
}
