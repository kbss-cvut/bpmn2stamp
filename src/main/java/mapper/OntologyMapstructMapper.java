package mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import utils.MappingUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

abstract public class OntologyMapstructMapper<THING> extends SmartMapstructMapper {

    private final Map<String, THING> mappedObjectsById;
    private String targetIdBase;

    public OntologyMapstructMapper() {
        mappedObjectsById = new HashMap<>();
    }

    @AfterMapping
    public void afterAnyThingMapping(@MappingTarget THING anyResult) {
        mappedObjectsById.put(getId(anyResult), anyResult);
    }

    public Map<String, THING> getMappedObjectsById() {
        return mappedObjectsById;
    }

    protected abstract String getId(THING individual);

    protected abstract String processId(String id);

    public String getTargetIdBase() {
        return targetIdBase;
    }

    public void setTargetIdBase(String targetIdBase) {
        this.targetIdBase = targetIdBase;
    }

    protected void addTypesToIndividual(Supplier<Set<String>> typesGetter, Consumer<Set<String>> typesSetter, Class... typesToAdd) {
        Set<String> individualTypes = typesGetter.get();
        if (individualTypes == null)
            individualTypes = new HashSet<>();
        typesSetter.accept(individualTypes);
        for (Class type : typesToAdd) {
            String classIRI = MappingUtils.getClassIRI(type);
            if (classIRI != null)
                individualTypes.add(classIRI);
        }
    }

}
