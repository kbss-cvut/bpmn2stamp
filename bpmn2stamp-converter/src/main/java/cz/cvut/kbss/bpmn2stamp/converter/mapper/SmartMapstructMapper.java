package cz.cvut.kbss.bpmn2stamp.converter.mapper;

import cz.cvut.kbss.bpmn2stamp.converter.mapper.bpmn2bbo.MapstructBpmn2BboMapper.AfterMappingAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SmartMapstructMapper implements MapstructMapper {

    private static final Logger LOG = LoggerFactory.getLogger(SmartMapstructMapper.class);

    private final List<AfterMappingAction> afterMapping;

    public SmartMapstructMapper() {
        this.afterMapping = new ArrayList<>();
    }

    /**
     * Method tries to find suitable method for mapping, based on argument types. Searches in current and all super classes.
     * @return result of the found mapping method. If method was not found (or wrongly found) returns null
     */
    protected final Object mapNext(Object... args) throws IllegalArgumentException {
        List<Class<?>> argsTypes = Arrays.stream(args)
                .map(Object::getClass).collect(Collectors.toList());
        List<Method> suitableMethods = findSuitableMethods(argsTypes);
        for (Method method : suitableMethods) {
            if (isAnnotationPresentSupers(PrivateMapping.class, method)) {
                continue;
            }
            Class<?> returnType = method.getReturnType();
            try {
                return returnType.cast(method.invoke(this, args));
            } catch (Exception e) {
                LOG.debug("Found method, considered as suitable, has wrong return type: {} for {}. Skipping...", returnType, argsTypes);
            }
        }
        throw new IllegalArgumentException(String.format("No mapping method was found for arguments %s", argsTypes));
    }

    /**
     * @see SmartMapstructMapper#mapNext(Object...)
     * @return result, if {@link SmartMapstructMapper#mapNext(Object...)} throws exception returns null
     */
    protected final Object mapNextUnchecked(Object... args) {
        try {
            return mapNext(args);
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    public List<AfterMappingAction> getAfterMapping() {
        return afterMapping;
    }

    private List<Method> findSuitableMethods(List<Class<?>> parametersTypes) {
        Method[] implementationMethods = this.getClass().getDeclaredMethods();
        Method[] superClassMethods = this.getClass().getSuperclass().getDeclaredMethods();

        Method[] allMethods = Stream.concat(Arrays.stream(implementationMethods), Arrays.stream(superClassMethods))
                .toArray(size -> (Method[]) Array.newInstance(implementationMethods.getClass().getComponentType(), size));

        List<Method> suitableMethods = new ArrayList<>();
        for (Method method : allMethods) {
            List<Class<?>> params = Arrays.stream(method.getParameters()).map(Parameter::getType).collect(Collectors.toList());
            if (parametersTypes.containsAll(params))
                suitableMethods.add(method);
        }
        return suitableMethods;
    }

    /** Similar to {@link Method#isAnnotationPresent(Class)}, but also check all superclass methods (until {@link SmartMapstructMapper} superclass). */
    private boolean isAnnotationPresentSupers(Class<? extends Annotation> annotation, Method method) {
        // try to find annotation
        boolean found = method.isAnnotationPresent(annotation);
        if (found) return true;
        // try to find this method in the super class
        try {
            Method superMethod = method.getDeclaringClass().getSuperclass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            return superMethod.isAnnotationPresent(annotation);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @Deprecated
    /** Similar to {@link Method#isAnnotationPresent(Class)}, but also check all superclass methods (until {@link SmartMapstructMapper} superclass). */
    private boolean isAnnotationPresentSuper(Class<? extends Annotation> annotation, Method method) {
        // caller method's declaring class
        Class<?> declaringClass = method.getDeclaringClass();
        // traverse superclasses until SmartMapstructMapper.class
        while (!declaringClass.equals(SmartMapstructMapper.class)) {
            // try to find annotation
            boolean found = method.isAnnotationPresent(annotation);
            if (found) return true;
            try {
                // try to find this method in the super class
                method = declaringClass.getSuperclass().getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
