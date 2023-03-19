package cz.cvut.kbss.bpmn2stamp.converter.mapper.bbo2stamp;

import cz.cvut.kbss.bpmn2stamp.converter.mapper.OntologyMapstructMapper;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.PrivateMapping;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.bpmn2bbo.MapstructBpmn2BboMapper.AfterMappingAction;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.CallActivity;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.FlowElement;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.UserTask;
import cz.cvut.kbss.bpmn2stamp.converter.utils.ConverterMappingUtils;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.StartEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.EndEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Task;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Capability;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.ControlledProcess;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Controller;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Process;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Structure;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.StructureComponent;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.util.*;

@Mapper
public abstract class MapstructBbo2StampMapper extends OntologyMapstructMapper<Iterable<Thing>, cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Thing, Bbo2StampMappingResult> {

    private final Bbo2StampMappingResult result;

    public MapstructBbo2StampMapper() {
        this.result = new Bbo2StampMappingResult(getMappedObjectsById());
        getConfiguration().setIriMappingFunction(ConverterMappingUtils::transformToUriCompliant);
    }

    @Override
    protected Bbo2StampMappingResult doMapping(Iterable<Thing> source) {
        for (Thing object : source) {
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
    public abstract ControlledProcess processToControlledProcess(cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Process process);
    @AfterMapping
    public void processControlledProcessProperties(cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Process process, @MappingTarget ControlledProcess controlledProcess) {
        getAfterMapping().add(new AfterMappingAction(() -> {
            for (FlowElement flowElement : process.getHas_flowElements()) {
                String bboIri = flowElement.getId();
                Set<String> iris = ConverterMappingUtils.ensurePropertyValue(cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary.s_p_has_control_structure_element_part, controlledProcess::getProperties, controlledProcess::setProperties);
                iris.add(bboIri);
            }
        }));
    }

    public cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Thing roleToThing(Role role) {
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
        getAfterMapping().add(new AfterMappingAction(() -> {
            // resolve capabilities
            Set<String> responsibilities = new HashSet<>();
            // TODO should be like: if role s_p_is_role_partOf, then that role is an actor,
            // TODO but for now it's assumed that only actors can have responsibilities
            for (Thing thing : role.getIs_responsibleFor()) {
                cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Thing responsibility = getMappedObjectsById().get(thing.getId());
                // TODO temp solution, somehow it throws NPE (responsibility is null)
                if (responsibility != null)
                    responsibilities.add(responsibility.getId());
            }
            Set<String> iris = ConverterMappingUtils.ensurePropertyValue(Vocabulary.s_p_has_capability, controllerResult::getProperties, controllerResult::setProperties);
            iris.addAll(responsibilities);

            if (role.getIs_role_partOf() != null) {
                // resolve parent roles
                Set<String> roles = new HashSet<>();
                for (Thing thing : role.getIs_role_partOf()) {
                    cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Thing roleToWhichActorBelongs = getMappedObjectsById().get(thing.getId());
                    // TODO temp solution, somehow it throws NPE (responsibility is null)
                    if (roleToWhichActorBelongs != null)
                        roles.add(roleToWhichActorBelongs.getId());
                }
                Set<String> iris2 = ConverterMappingUtils.ensurePropertyValue(Vocabulary.s_p_is_part_of_control_structure, controllerResult::getProperties, controllerResult::setProperties);
                iris2.addAll(roles);
            }
        }));
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
        getAfterMapping().add(new AfterMappingAction(() -> {
            if (role.getHas_role_part() != null && !role.getHas_role_part().isEmpty()) {
                Set<String> iris = ConverterMappingUtils.ensurePropertyValue(Vocabulary.s_p_has_control_structure_element_part, structureResult::getProperties, structureResult::setProperties);
                for (Role rolePart : role.getHas_role_part()) {
                    iris.add(rolePart.getId());
                }
            }
        }));
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
    public abstract ControlledProcess callActivityToControlledProcess(CallActivity callActivity);
    @AfterMapping
    public void processControlledProcessProperties(CallActivity callActivity, @MappingTarget ControlledProcess controlledProcess) {
        getAfterMapping().add(new AfterMappingAction(() -> {
            Thing calledElement = callActivity.getHas_calledElement();
            if (calledElement == null)
                return;
            Set<String> iris = ConverterMappingUtils.ensurePropertyValue(Vocabulary.s_p_has_control_structure_element_part, controlledProcess::getProperties, controlledProcess::setProperties);
            iris.add(calledElement.getId());
        }));
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty"),
            @Mapping(target = "types", ignore = true),
            @Mapping(target = "properties", ignore = true)
    })
    public abstract Capability userTaskToProcessAndCapability(UserTask userTask);
    @AfterMapping
    public void processUserTaskProperties(Task anyTask, @MappingTarget Capability anyCapabilityResult) {
        getAfterMapping().add(new AfterMappingAction(() -> {
            ConverterMappingUtils.addTypesToIndividual(anyCapabilityResult::getTypes, anyCapabilityResult::setTypes, Process.class);
        }));
    }

    public cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Thing groupToThing(Group group) {
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
        getAfterMapping().add(new AfterMappingAction(() -> {
            Set<String> groups = new HashSet<>();
            if (group.getHas_part() != null && !group.getHas_part().isEmpty()) {
                Set<String> iris = ConverterMappingUtils.ensurePropertyValue(Vocabulary.s_p_has_control_structure_element_part, structureComponentResult::getProperties, structureComponentResult::setProperties);
                for (Thing groupGroup : group.getHas_part()) {
                    groups.add(groupGroup.getId());
                }
                iris.addAll(groups);
            }
        }));
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
        getAfterMapping().add(new AfterMappingAction(() -> {
            Set<String> groups = new HashSet<>();
            if (group.getHas_part() != null && !group.getHas_part().isEmpty()) {
                Set<String> iris = ConverterMappingUtils.ensurePropertyValue(Vocabulary.s_p_has_control_structure_element_part, structureResult::getProperties, structureResult::setProperties);
                for (Thing groupGroup : group.getHas_part()) {
                    groups.add(groupGroup.getId());
                }
                iris.addAll(groups);
            }
        }));
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
    protected String getId(cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Thing individual) {
        return individual.getId();
    }

    @Named("nullifyEmpty")
    protected String nullifyEmpty(String name) {
        if (StringUtils.isEmpty(name))
            return null;
        return name;
    }
}
