package mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SmartMapstructMapper implements MapstructMapper{

    private static final Logger LOG = LoggerFactory.getLogger(SmartMapstructMapper.class);

    private final List<Runnable> afterMapping;

    public SmartMapstructMapper() {
        this.afterMapping = new ArrayList<>();
    }

    /**
     * Method tries to find suitable method for mapping, based on argument types. Searches in current and all super classes.
     * @return result of the found mapping method. If method was not found (or wrongly found) returns null
     */
    public final Object mapNext(Object... args) {
        List<Class<?>> argsTypes = Arrays.stream(args)
                .map(Object::getClass).collect(Collectors.toList());
        Method suitableMethod = findSuitableMethod(argsTypes);
        if (suitableMethod == null) {
            LOG.warn("Could not find suitable method for {}}", argsTypes);
            return null;
        }
        Class<?> returnType = suitableMethod.getReturnType();
        try {
            return returnType.cast(suitableMethod.invoke(this, args));
        } catch (Exception e) {
            LOG.warn("Found method, considered as suitable, has wrong return type: {} for {}", returnType, argsTypes);
            return null;
        }
    }

    private Method findSuitableMethod(List<Class<?>> parametersTypes) {
        Method[] implementationMethods = this.getClass().getDeclaredMethods();
        Method[] superClassMethods = this.getClass().getSuperclass().getDeclaredMethods();

        Method[] allMethods = Stream.concat(Arrays.stream(implementationMethods), Arrays.stream(superClassMethods))
                .toArray(size -> (Method[]) Array.newInstance(implementationMethods.getClass().getComponentType(), size));

        for (Method method : allMethods) {
            List<Class<?>> params = Arrays.stream(method.getParameters()).map(Parameter::getType).collect(Collectors.toList());
            if (parametersTypes.containsAll(params))
                return method;
        }
        return null;
    }

    public List<Runnable> getAfterMapping() {
        return afterMapping;
    }
}
