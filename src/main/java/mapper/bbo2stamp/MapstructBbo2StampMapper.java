package mapper.bbo2stamp;

import com.google.common.base.Predicates;
import mapper.OntologyMapstructMapper;
import model.bbo.model.*;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TExpression;
import model.stamp.model.Thing;
import model.stamp.model.*;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import utils.MappingUtils;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public abstract class MapstructBbo2StampMapper extends OntologyMapstructMapper<Thing> {

    private final Bbo2StampMappingResult result;

    public MapstructBbo2StampMapper() {
        this.result = new Bbo2StampMappingResult(getMappedObjectsById());
    }

    public Bbo2StampMappingResult process(model.bbo.model.Process process) {
        ControlledProcess controlledProcess = processToControlledProcess(process);
        for (FlowElement flowElement : process.getHas_flowElements()) {
            Thing element = (Thing) mapNext(flowElement);
        }
        result.getControlledProcesses().add(controlledProcess);
        return result;
    }

    public Bbo2StampMappingResult organization(Collection<Group> groups) {
        // TODO
        return result;
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract ControlledProcess processToControlledProcess(model.bbo.model.Process process);
    @AfterMapping
    public void processControlledProcessProperties(model.bbo.model.Process process, @MappingTarget ControlledProcess controlledProcessResult) {
        getAfterMapping().add(() -> {
            if (controlledProcessResult.getHas_control_structure_element_part() == null) controlledProcessResult.setHas_control_structure_element_part(new HashSet<>());
            Set<Thing> structureElementParts = process.getHas_flowElements().stream()
                    .map(FlowElement::getId)
                    .map(e -> getMappedObjectsById().get(e))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            controlledProcessResult.setHas_control_structure_element_part(structureElementParts);
        });
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract Controller roleToController(Role role);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract Capability startEventToProcess(StartEvent startEvent);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract Capability endEventToProcess(EndEvent endEvent);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract Capability userTaskToProcessAndCapability(UserTask userTask);
    @AfterMapping
    public void processUserTaskProperties(Task anyTask, @MappingTarget Capability anyCapabilityResult) {
        getAfterMapping().add(() -> {
            if (anyCapabilityResult.getTypes() == null) anyCapabilityResult.setTypes(new HashSet<>());
            anyCapabilityResult.getTypes().add(
                    MappingUtils.getClassIRI(model.stamp.model.Process.class));
        });
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract Structure groupToStructure(Group group);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract StructureComponent groupToStructureComponent(Group group);

//    @Mappings({
//            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
//            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
//    })
//    public abstract ControlStructureElement processToControlledProcess(FlowElement process);

    // ----------------------------------- BEFORE MAPPING -----------------------------------

    /** <ul>
     * <li>TBaseElement - runs before any mapping, where source is any bpmn xml element.</li>
     * <li>TBaseElementWithMixedContent - runs before any mapping, where source is any bpmn xml element (specific for elements with mixed content, like {@link TExpression expressions}.</li>
     * </ul> */
//    @BeforeMapping
//    public void beforeAnyMapping(Object anySource) {
//        if (anySource instanceof TBaseElement) {
//            String id = MappingUtils.checkAndRepairId(extractSourceId(anySource));
//            ((TBaseElement) anySource).setId(id);
//        } else if (anySource instanceof TBaseElementWithMixedContent) {
//            String id = MappingUtils.checkAndRepairId(extractSourceId(anySource));
//            ((TBaseElementWithMixedContent) anySource).setId(id);
//        }
//    }

    // ----------------------------------- AFTER MAPPING -----------------------------------

    // nothing specific yet

    // -----------------------------------  -----------------------------------

    @Named("processId")
    @Override
    protected String processId(String id) {
        return MappingUtils.transformToUriCompliant(id);
    }

    @Override
    protected String getId(Thing individual) {
        return individual.getId();
    }

    @Named("nullifyEmpty")
    protected String nullifyEmpty(String name) {
        if (StringUtils.isEmpty(name))
            return null;
        return name;
    }
}
