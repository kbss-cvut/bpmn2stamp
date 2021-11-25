package common;

import model.bbo.model.FlowElement;
import model.bbo.model.Thing;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract public class OntologyMapstructMapper<T> implements MapstructMapper {

    private final Map<String, T> mappedObjects;
    private final List<Runnable> afterMapping;

    public OntologyMapstructMapper() {
        mappedObjects = new HashMap<>();
        afterMapping = new ArrayList<>();
    }

    protected Object map(Object... args) {
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

    @AfterMapping
    public void afterAnyThingMapping(@MappingTarget T target) {
        mappedObjects.put(getId(target), target);
    }

    protected abstract String getId(T obj);

    private Method findSuitableMethod(List<Class<?>> parametersTypes) {
        for (Method method : this.getClass().getDeclaredMethods()) {
            List<Class<?>> params = Arrays.stream(method.getParameters()).map(e -> e.getType()).collect(Collectors.toList());
            if (parametersTypes.containsAll(params))
                return method;
        }
        return null;
    }

    public Map<String, T> getMappedObjects() {
        return mappedObjects;
    }

    public List<Runnable> getAfterMapping() {
        return afterMapping;
    }

}
