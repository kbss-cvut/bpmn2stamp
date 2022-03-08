package org.example.mapper.bbo2stamp;

import org.example.mapper.OntologyMapstructMapper;
import org.example.mapper.PrivateMapping;
import org.example.model.bbo.model.Role;
import org.example.model.bbo.model.StartEvent;
import org.example.model.bbo.model.EndEvent;
import org.example.model.bbo.model.UserTask;
import org.example.model.bbo.model.Task;
import org.example.model.bbo.model.Group;
import org.example.model.stamp.Vocabulary;
import org.example.model.stamp.model.Capability;
import org.example.model.stamp.model.ControlledProcess;
import org.example.model.stamp.model.Controller;
import org.example.model.stamp.model.Process;
import org.example.model.stamp.model.Structure;
import org.example.model.stamp.model.StructureComponent;
import org.example.model.stamp.model.Thing;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.example.utils.ConverterMappingUtils;

import java.util.*;

import static org.example.utils.ConverterMappingUtils.ensurePropertyValue;

@Mapper
public abstract class MapstructBbo2StampMapper extends OntologyMapstructMapper<Iterable<org.example.model.bbo.model.Thing>, Thing, Bbo2StampMappingResult> {

    private final Bbo2StampMappingResult result;

    public MapstructBbo2StampMapper() {
        this.result = new Bbo2StampMappingResult(getMappedObjectsById());
        getConfiguration().setIriMappingFunction(ConverterMappingUtils::transformToUriCompliant);
    }

    @Override
    protected Bbo2StampMappingResult doMapping(Iterable<org.example.model.bbo.model.Thing> source) {
        for (org.example.model.bbo.model.Thing object : source) {
            mapNextUnchecked(object);
        }
        return this.result;
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract ControlledProcess processToControlledProcess(org.example.model.bbo.model.Process process);

    public Thing roleToThing(Role role) {
        if (role.getIs_responsibleFor() != null && !role.getIs_responsibleFor().isEmpty()) {
            // if role has responsibilities, then it is a controller and a structure component
            Controller controller = roleToController(role);
            processStructureComponentProperties(role, controller);
            return controller;
        }
        // if otherwise it is just a structure component
        return roleToStructureComponent(role);
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
            // TODO should be like: if role s_p_is_role_partOf, then that role is an actor,
            // TODO but for now it's assumed that only actors can have responsibilities
            for (org.example.model.bbo.model.Thing thing : role.getIs_responsibleFor()) {
                Thing responsibility = getMappedObjectsById().get(thing.getId());
                // TODO temp solution, somehow it throws NPE (responsibility is null)
                if (responsibility != null)
                    responsibilities.add(responsibility.getId());
            }
            Set<String> iris = ensurePropertyValue(Vocabulary.s_p_has_capability, controllerResult::getProperties, controllerResult::setProperties);
            iris.addAll(responsibilities);

            if (role.getIs_role_partOf() != null) {
                // resolve parent roles
                Set<String> roles = new HashSet<>();
                for (org.example.model.bbo.model.Thing thing : role.getIs_role_partOf()) {
                    Thing roleToWhichActorBelongs = getMappedObjectsById().get(thing.getId());
                    // TODO temp solution, somehow it throws NPE (responsibility is null)
                    if (roleToWhichActorBelongs != null)
                        roles.add(roleToWhichActorBelongs.getId());
                }
                Set<String> iris2 = ensurePropertyValue(Vocabulary.s_p_is_part_of_control_structure, controllerResult::getProperties, controllerResult::setProperties);
                iris2.addAll(roles);
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
    //TODO and capability
    public abstract Process startEventToProcess(StartEvent startEvent);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    //TODO and capability
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
            ConverterMappingUtils.addTypesToIndividual(anyCapabilityResult::getTypes, anyCapabilityResult::setTypes, Process.class);
        });
    }

    public Thing groupToThing(Group group) {
        // if group is a part of any other group, then it is structure component
        if (group.getIs_partOf() != null && !group.getIs_partOf().isEmpty()) {
            return groupToStructureComponent(group);
        }
        return groupToStructure(group);
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
    //TODO the same as processStructureProperties
    public void processStructureComponentProperties(Group group, @MappingTarget StructureComponent structureComponentResult) {
        getAfterMapping().add(() -> {
            Set<String> groups = new HashSet<>();
            if (group.getHas_part() != null && !group.getHas_part().isEmpty()) {
                Set<String> iris = ensurePropertyValue(Vocabulary.s_p_has_control_structure_element_part, structureComponentResult::getProperties, structureComponentResult::setProperties);
                for (org.example.model.bbo.model.Thing groupGroup : group.getHas_part()) {
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
                for (org.example.model.bbo.model.Thing groupGroup : group.getHas_part()) {
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
    protected String processId(String id) {
        return super.processId(id);
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
