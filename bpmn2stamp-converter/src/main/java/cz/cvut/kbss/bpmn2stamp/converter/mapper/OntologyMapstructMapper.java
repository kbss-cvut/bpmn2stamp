package cz.cvut.kbss.bpmn2stamp.converter.mapper;

import cz.cvut.kbss.bpmn2stamp.converter.common.ApplicationConstants;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.bpmn2bbo.MapstructBpmn2BboMapper.AfterMappingAction;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import cz.cvut.kbss.bpmn2stamp.converter.utils.ConverterMappingUtils;

import java.util.*;

abstract public class OntologyMapstructMapper<INPUT, THING, MAPPING_RESULT extends AbstractMappingResult<THING>> extends SmartMapstructMapper {

    private OntologyMappingConfig configuration;

    /**
     * This {@link Map} holds all mapped objects by id.
     */
    private final Map<String, THING> mappedObjectsById;

    public OntologyMapstructMapper() {
        mappedObjectsById = new HashMap<>();
        // default configuration
        configuration = new OntologyMappingConfig(
                "http://bpmn2stamp.org/default/ontology/",
                sourceElementId -> {
                    String compliantId = ConverterMappingUtils.transformToUriCompliant(sourceElementId);
                    if (!configuration.getBaseIri().endsWith(ApplicationConstants.ONTOLOGY_IRI_SUFFIX)) {
                        return getTargetIdBase() + ApplicationConstants.ONTOLOGY_IRI_SUFFIX + compliantId;
                    }
                    return getTargetIdBase() + compliantId;
                }
        );
    }

    /**
     * Provides implementation of how to do mapping of the given source.
     *
     * @param source object to be processed for mapping
     * @return mapping result. Note that the result could be updated later.
     */
    protected abstract MAPPING_RESULT doMapping(INPUT source);

    /**
     * Main mapping method. Processes input of type {@link INPUT} and returns mapping result {@link AbstractMappingResult<THING>}
     *
     * @implNote this method calls internal {@link OntologyMapstructMapper#doMapping(Object) doMapping} method.
     */
    public final MAPPING_RESULT process(INPUT source) {
        MAPPING_RESULT result = doMapping(source);
        getAfterMapping().forEach(AfterMappingAction::run);
        return result;
    }

    /**
     * This method is called after any mapping of the {@link THING} to add mapping result to the mapped objects list.
     *
     * @implNote used by mapstruct generator
     */
    @AfterMapping
    protected void afterAnyThingMapping(@MappingTarget THING anyResult) {
        if (anyResult != null)
            mappedObjectsById.put(getId(anyResult), anyResult);
    }

    protected Map<String, THING> getMappedObjectsById() {
        return mappedObjectsById;
    }

    /**
     * ID extractor of the {@link THING base ontology}.
     *
     * @param individual individual to extract id from
     * @return id of the individual entity
     */
    protected abstract String getId(THING individual);

    protected String processId(String id) {
        return getConfiguration().getIriMappingFunction().apply(id);
    };

    protected String getTargetIdBase() {
        return configuration.getBaseIri();
    }

    public OntologyMappingConfig getConfiguration() {
        return configuration;
    }

    public void setConfiguration(OntologyMappingConfig configuration) {
        this.configuration = configuration;
    }
}
