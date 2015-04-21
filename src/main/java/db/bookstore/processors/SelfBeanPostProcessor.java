package db.bookstore.processors;

import db.bookstore.annotations.Self;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by StudentDB on 16.04.2015.
 */

@Component
public class SelfBeanPostProcessor implements BeanPostProcessor, Ordered {

    private Map<String, Object> nameToBeanMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Self.class)) {
                nameToBeanMap.put(beanName, bean);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        Object originalBean = nameToBeanMap.get(beanName);
        if (originalBean != null) {
            for (Field field : originalBean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Self.class)) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, originalBean, bean);
                }
            }
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
