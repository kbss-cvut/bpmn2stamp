package mapper;

import common.Constants;
import model.bbo.model.Thing;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import utils.MappingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class OntologyMapstructMapper extends SmartMapstructMapper {

    private final Map<String, Thing> mappedObjectsById;
    private final List<Runnable> afterMapping;

    public OntologyMapstructMapper() {
        mappedObjectsById = new HashMap<>();
        afterMapping = new ArrayList<>();
    }

    @AfterMapping
    public void afterAnyThingMapping(@MappingTarget Thing anyResult) {
        mappedObjectsById.put(anyResult.getId(), anyResult);
    }

    public Map<String, Thing> getMappedObjectsById() {
        return mappedObjectsById;
    }

    protected abstract String transformToUriCompliant(String id);

}
