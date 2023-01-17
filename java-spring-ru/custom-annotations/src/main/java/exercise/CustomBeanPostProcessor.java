package exercise;

import java.lang.reflect.Proxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// BEGIN
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Class> inspectingBeans = new HashMap<>();
    private Map<String, String> logLevels = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(Object.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Inspect.class)) {
            inspectingBeans.put(name, bean.getClass());
            String level = bean.getClass().getAnnotation(Inspect.class).level();
            logLevels.put(name, level);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        Class beanClass = inspectingBeans.get(name);
        if (beanClass != null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(),
                    (proxy, method, args) -> {
                        String message = String.format(
                                "Was called method: %s() with arguments: %s",
                                method.getName(),
                                Arrays.toString(args)
                        );
                        String logLevel = logLevels.get(name);
                        if (logLevel.equals("info")) {
                            LOGGER.info(message);
                        } else {
                            LOGGER.debug(message);
                        }
                        return method.invoke(bean, args);
                    }
            );
        }
        return bean;
    }
}
// END
