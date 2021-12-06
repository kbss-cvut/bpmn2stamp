package mapper;

import com.google.common.collect.Streams;
import mapper.bpmn2bbo.MapstructBpmnMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface MapstructMapper {

    static final Logger LOG = LoggerFactory.getLogger(MapstructBpmnMapper.class);

    default Object map(Object... args) {
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
            e.printStackTrace();
        }
        return null;
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
}
