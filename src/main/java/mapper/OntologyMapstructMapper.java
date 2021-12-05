package mapper;

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

    @AfterMapping
    public void afterAnyThingMapping(@MappingTarget T target) {
        mappedObjects.put(getId(target), target);
    }

    protected abstract String getId(T obj);

    public Map<String, T> getMappedObjects() {
        return mappedObjects;
    }

    public List<Runnable> getAfterMapping() {
        return afterMapping;
    }

}
