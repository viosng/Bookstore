package db.bookstore.ehcache.key.generator;

import org.joda.time.DateTime;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by StudentDB on 21.04.2015.
 */

@Component("keyGenerator")
public class CustomKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        Object newParams[] = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof DateTime) {
                param = ((DateTime) param).withTime(0, 0, 0, 0);
            }
            newParams[i] = param;
        }
        return Objects.hash(method.getName(), Objects.hash(newParams));
    }
}
