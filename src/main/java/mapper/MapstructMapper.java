package mapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface MapstructMapper {

    default Object map(Object... args) {
        List<Class<?>> argsTypes = Arrays.stream(args)
                .map(Object::getClass).collect(Collectors.toList());
        Method suitableMethod = findSuitableMethod(argsTypes);
        Class<?> returnType = suitableMethod.getReturnType();
        try {
            return returnType.cast(suitableMethod.invoke(this, args));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Method findSuitableMethod(List<Class<?>> parametersTypes) {
        for (Method method : this.getClass().getDeclaredMethods()) {
            List<Class<?>> params = Arrays.stream(method.getParameters()).map(e -> e.getType()).collect(Collectors.toList());
            if (parametersTypes.containsAll(params))
                return method;
        }
        return null;
    }
}
