package mapper.bbo2stamp;

import mapper.OntologyMapstructMapper;
import mapper.PrivateMapping;
import model.bbo.model.*;
import model.bbo.model.Process;
import model.stamp.model.*;
import model.stamp.model.Thing;
import model.bbo.model.*;
import model.stamp.Vocabulary;
import model.stamp.model.*;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import utils.MappingUtils;

import java.util.*;
import java.util.stream.Collectors;

import static utils.MappingUtils.ensurePropertyValue;

@Mapper
public abstract class MapstructBbo2StampMapper extends OntologyMapstructMapper<Thing> {

    private final Bbo2StampMappingResult result;

    public MapstructBbo2StampMapper() {
        this.result = new Bbo2StampMappingResult(getMappedObjectsById());
    }
//    getPersistenceContext
    public Bbo2StampMappingResult convert(Collection<Thing> objects) {
        List<Thing> result = new ArrayList<>();
        System.out.println(objects);
        for (Thing object : objects) {
            Thing o = (Thing) mapNextUnchecked(object);
            if (o != null)
                result.add(o);
        }
        return this.result;
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

    public Thing roleToThing(Role role) {
        Thing result;
        // if role has responsibilities, then it is a controller
        if (role.getIs_responsibleFor() != null && !role.getIs_responsibleFor().isEmpty()) {
            Controller controller = roleToController(role);
            processStructureComponentProperties(role, controller);
            result = controller;
        } else {
            result = roleToStructureComponent(role);
        }
        return result;
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    @PrivateMapping
    abstract Controller roleToController(Role role);
    @AfterMapping
    public void processControllerProperties(Role role, @MappingTarget Controller controllerResult) {
        getAfterMapping().add(() -> {
            // resolve capabilities
            Set<String> responsibilities = new HashSet<>();
            // TODO role s_p_is_role_partOf => that role is an actor, but for now it's assumed that only actors can have responsibilities
            for (model.bbo.model.Thing thing : role.getIs_responsibleFor()) {
                Thing responsibility = getMappedObjectsById().get(thing.getId());
                responsibilities.add(responsibility.getId());
            }
            Set<String> iris = ensurePropertyValue(Vocabulary.s_p_has_capability, controllerResult::getProperties, controllerResult::setProperties);
            iris.addAll(responsibilities);

            // resolve parent roles
            Set<String> roles = new HashSet<>();
            for (model.bbo.model.Thing thing : role.getIs_role_partOf()) {
                Thing roleToWhichActorBelongs = getMappedObjectsById().get(thing.getId());
                roles.add(roleToWhichActorBelongs.getId());
            }
            Set<String> iris2 = ensurePropertyValue(Vocabulary.s_p_is_part_of_control_structure, controllerResult::getProperties, controllerResult::setProperties);
            iris2.addAll(roles);
        });
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    @PrivateMapping
    abstract StructureComponent roleToStructureComponent(Role role);
    @AfterMapping
    public void processStructureComponentProperties(Role role, @MappingTarget StructureComponent structureResult) {
        getAfterMapping().add(() -> {
            if (role.getHas_role_part() != null && !role.getHas_role_part().isEmpty()) {
                Set<String> iris = ensurePropertyValue(Vocabulary.s_p_has_control_structure_element_part, structureResult::getProperties, structureResult::setProperties);
                for (Role rolePart : role.getHas_role_part()) {
                    iris.add(rolePart.getId());
                }
            }
        });
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract Process startEventToProcess(StartEvent startEvent);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract Process endEventToProcess(EndEvent endEvent);

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
            addTypesToIndividual(anyCapabilityResult::getTypes, anyCapabilityResult::setTypes, Process.class);
        });
    }

    public Thing groupToThing(Group group) {
        Thing result;
        // if group is a part of any other group, then it is structure component
        if (group.getIs_partOf() != null && !group.getIs_partOf().isEmpty()) {
            result = groupToStructureComponent(group);
        } else {
            result = groupToStructure(group);
        }
        return result;
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    @PrivateMapping
    public abstract StructureComponent groupToStructureComponent(Group group);
    @AfterMapping
    public void processStructureComponentProperties(Group group, @MappingTarget StructureComponent structureComponentResult) {
        getAfterMapping().add(() -> {
            Set<String> groups = new HashSet<>();
            if (group.getHas_part() != null && !group.getHas_part().isEmpty()) {
                Set<String> iris = ensurePropertyValue(Vocabulary.s_p_has_control_structure_element_part, structureComponentResult::getProperties, structureComponentResult::setProperties);
                for (model.bbo.model.Thing groupGroup : group.getHas_part()) {
                    groups.add(groupGroup.getId());
                }
                iris.addAll(groups);
            }
        });
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    @PrivateMapping
    public abstract Structure groupToStructure(Group group);
    @AfterMapping
    public void processStructureProperties(Group group, @MappingTarget Structure structureResult) {
        getAfterMapping().add(() -> {
            Set<String> groups = new HashSet<>();
            if (group.getHas_part() != null && !group.getHas_part().isEmpty()) {
                Set<String> iris = ensurePropertyValue(Vocabulary.s_p_has_control_structure_element_part, structureResult::getProperties, structureResult::setProperties);
                for (model.bbo.model.Thing groupGroup : group.getHas_part()) {
                    groups.add(groupGroup.getId());
                }
                iris.addAll(groups);
            }
        });
    }

    // TODO interrupting boundary events such as timer

//    @AfterMapping
//    public void processStructureProperties(Group group, @MappingTarget Structure structureResult) {
//        getAfterMapping().add(() -> {
//            Set<String> groups = new HashSet<>();
//            if (group.getHas_part() != null && !group.getHas_part().isEmpty()) {
//                Set<String> iris = ensurePropertyValue(Vocabulary.s_p_has_control_structure_element_part, structureResult::getProperties, structureResult::setProperties);
//                for (model.bbo.model.Thing groupGroup : group.getHas_part()) {
//                    groups.add(groupGroup.getId());
//                }
//                iris.addAll(groups);
//            }
//        });
//    }

    // ----------------------------------- BEFORE MAPPING -----------------------------------

    // nothing specific yet

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
