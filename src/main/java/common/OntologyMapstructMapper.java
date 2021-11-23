package common;

import model.bbo.model.FlowElement;
import model.bbo.model.SequenceFlow;
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

abstract public class OntologyMapstructMapper implements MapstructMapper {

//    private final List<MappingMethod> declaredMappingMethods;
    private final Map<String, Object> mappedObjects;
    private final List<Runnable> afterMapping;

    public OntologyMapstructMapper() {
//        declaredMappingMethods = findAllDeclaredMethods();
        mappedObjects = new HashMap<>();
        afterMapping = new ArrayList<>();
    }

    protected <S> Object map(Object... args) {
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
    public void afterAnyThingMapping(@MappingTarget Thing target) {
        mappedObjects.put(target.getId(), target);
    }

    @AfterMapping
    public void afterAnyFlowElementMapping(@MappingTarget FlowElement target) {
        mappedObjects.put(target.getId(), target);
    }

//    private List<MappingMethod> findAllDeclaredMethods() {
////        return Arrays.stream(this.getClass().getMethod().getDeclaredMethods()).map(MappingMethod::of).collect(Collectors.toList());
//        return null;
//    }

    private Method findSuitableMethod(List<Class<?>> parametersTypes) {
        for (Method method : this.getClass().getDeclaredMethods()) {
            List<Class<?>> params = Arrays.stream(method.getParameters()).map(e -> e.getType()).collect(Collectors.toList());
            if (parametersTypes.containsAll(params))
                return method;
        }
        return null;
    }

    public Map<String, Object> getMappedObjects() {
        return mappedObjects;
    }

    public List<Runnable> getAfterMapping() {
        return afterMapping;
    }

}
