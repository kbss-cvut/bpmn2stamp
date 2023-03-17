package cz.cvut.kbss.bpmn2stamp.converter.mapper.bpmn2bbo;

import cz.cvut.kbss.bpmn2stamp.converter.mapper.OntologyMapstructMapper;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Activity;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.NormalSequenceFlow;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.TimeExpression;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.TimerEventDefinition;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.UserTask;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TProcess;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TRootElement;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import cz.cvut.kbss.bpmn2stamp.converter.service.BpmnAsBbo;
import cz.cvut.kbss.bpmn2stamp.converter.utils.ConverterMappingUtils;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.EndEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.EventDefinition;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.FlowElement;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.FlowNode;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.InterruptingBoundaryEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Process;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.StartEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TBaseElement;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TBoundaryEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TCollaboration;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TEndEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TEventDefinition;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TExpression;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TFlowElement;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TParticipant;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TStartEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TTimerEventDefinition;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TUserTask;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import javax.xml.bind.JAXBElement;
import java.io.Serializable;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.isEmpty;
import static cz.cvut.kbss.bpmn2stamp.converter.common.ApplicationConstants.COMPOSITE_ID_DELIMITER;

@Mapper
public abstract class MapstructBpmn2BboMapper extends OntologyMapstructMapper<TDefinitions, Thing, Bpmn2BboMappingResult> {

    private MappingContext mappingContext;

    private final Map<String, String> sourceToTargetIds;
    private final Bpmn2BboMappingResult result;

    public MapstructBpmn2BboMapper() {
        this.sourceToTargetIds = new HashMap<>();
        this.result = new Bpmn2BboMappingResult(getMappedObjectsById());
    }

    @Override
    protected Bpmn2BboMappingResult doMapping(TDefinitions source) {
        List<JAXBElement<? extends TRootElement>> rootElement = source.getRootElement();
        if (isEmpty(rootElement)) return null;

        for (JAXBElement<? extends TRootElement> root : rootElement) {
            if (root.getValue() instanceof TCollaboration) {
                TCollaboration value = (TCollaboration) root.getValue();
                List<TParticipant> participants = value.getParticipant();
                for (TParticipant participant : participants) {
                    if (!isProcess(participant)) {
                        result.getRoles().put(participant.getId(), participantToRole(participant));
                    }
                }
            }
            // TODO rework code below
            if (root.getValue() instanceof TProcess) {
                TProcess tProcess = (TProcess) root.getValue();
                
                //init context
                mappingContext = new MappingContext(tProcess.getId(), tProcess.getName());
                
                Process process = processToProcess(tProcess);

                mappingContext.setProcess(process);
                
                result.getProcesses().put(tProcess.getId(), process);
                for (JAXBElement<? extends TFlowElement> flow : tProcess.getFlowElement()) {
                    TFlowElement flowElement = flow.getValue();
                    FlowElement resFlowElement = (FlowElement) mapNextUnchecked(flowElement);
                    if (resFlowElement == null)
                        continue;
                    result.getFlowElements().put(resFlowElement.getId(), resFlowElement);
                }
            }
        }
        result.setElementsIdMapping(sourceToTargetIds);
        BpmnAsBbo bpmnAsBbo = new BpmnAsBbo();
        bpmnAsBbo.setFlowElements(result.getFlowElements());
        bpmnAsBbo.setPerformers(result.getRoles());
        bpmnAsBbo.setProcesses(result.getProcesses());
        bpmnAsBbo.setAllObjects(result.getMappedObjects());
        result.setBpmnAsBbo(bpmnAsBbo);
        return result;
    }

    //FIXME component model

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
    })
    public abstract EndEvent endEventToEndEvent(TEndEvent endEvent);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
    })
    public abstract StartEvent startEventToStartEvent(TStartEvent startEvent);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
    })
    public abstract UserTask userTaskToUserTask(TUserTask userTask);
    @AfterMapping
    public void processUserTaskProperties(TUserTask userTask, @MappingTarget UserTask userTaskResult) {
        getAfterMapping().add(new AfterMappingAction(mappingContext.getProcess(), (p) -> {
            List<String> resourceIds = userTask.getResourceRole()
                    .stream()
                    .map(JAXBElement::getValue)
                    .map(e -> e.getResourceRef().getLocalPart())
                    .collect(Collectors.toList());
            for (String resourceId : resourceIds) {
                String resourceTargetId = sourceToTargetIds.get(resourceId);
                Role role = (Role) getMappedObjectsById().get(resourceTargetId);
                if (role.getIs_responsibleFor() == null) {
                    role.setIs_responsibleFor(new HashSet<>());
                }
                role.getIs_responsibleFor().add(userTaskResult);
            }
        }));
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
    })
    public abstract NormalSequenceFlow sequenceFlowToNormalSequenceFlow(TSequenceFlow sequenceFlow);
    @AfterMapping
    public void processNormalSequenceFlowProperties(TSequenceFlow sequenceFlow, @MappingTarget NormalSequenceFlow normalSequenceFlowResult) {
        getAfterMapping().add(new AfterMappingAction(mappingContext.getProcess(), (p) -> {
            TBaseElement targetRef = (TBaseElement) sequenceFlow.getTargetRef();
            String targetRefTargetId = sourceToTargetIds.get(targetRef.getId());
            Thing targetRefTargetThing = getMappedObjectsById().get(targetRefTargetId);
            
            TBaseElement sourceRef = (TBaseElement) sequenceFlow.getSourceRef();
            String sourceRefTargetId = sourceToTargetIds.get(sourceRef.getId());
            Thing sourceRefTargetThing = getMappedObjectsById().get(sourceRefTargetId);
            
            if (targetRefTargetThing == null || sourceRefTargetThing == null) {
                getMappedObjectsById().remove(getId(normalSequenceFlowResult));
                return;
            }
            
            normalSequenceFlowResult.setHas_targetRef(Sets.newHashSet(targetRefTargetThing));
            normalSequenceFlowResult.setHas_sourceRef((FlowNode) sourceRefTargetThing);

            if (p.getHas_flowElements() == null) {
                p.setHas_flowElements(new HashSet<>());
            }
            p.getHas_flowElements().add(normalSequenceFlowResult);
        }));
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
    })
    public abstract Role participantToRole(TParticipant participant);

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
    })
    public abstract Process processToProcess(TProcess process);

    @Mappings({
            @Mapping(source = ".", target = "has_eventDefinition", qualifiedByName = "unpackEventDefinitions"),
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
    })
    public abstract InterruptingBoundaryEvent boundaryEventToInterruptingBoundaryEvent(TBoundaryEvent tBoundaryEvent);
    @AfterMapping
    public void processInterruptingBoundaryEventProperties(TBoundaryEvent tBoundaryEvent, @MappingTarget InterruptingBoundaryEvent eventResult) {
        getAfterMapping().add(new AfterMappingAction(mappingContext.getProcess(), (p) -> {
            // TODO add mapping for output flow to next activity
            String activityId = tBoundaryEvent.getAttachedToRef().getLocalPart();
            String activityTargetId = sourceToTargetIds.get(activityId);
            Activity activity = (Activity) getMappedObjectsById().get(activityTargetId);
            if (activity.getHas_boundaryEventRef() == null) activity.setHas_boundaryEventRef(new HashSet<>());
            activity.getHas_boundaryEventRef().add(eventResult);

            tBoundaryEvent.getEventDefinition().stream().map(JAXBElement::getValue).forEach(definition -> {
                String definitionTargetId = sourceToTargetIds.get(definition.getId());
                EventDefinition eventDefinition = (EventDefinition) getMappedObjectsById().get(definitionTargetId);
                if (eventResult.getHas_eventDefinition() == null) eventResult.setHas_eventDefinition(new HashSet<>());
                eventResult.getHas_eventDefinition().add(eventDefinition);
            });
        }));
    }


    @Named("unpackEventDefinitions")
    public Set<EventDefinition> unpackEventDefinitions(TBoundaryEvent source) {
        String attachedProcessId = source.getAttachedToRef().toString();
        List<JAXBElement<? extends TEventDefinition>> eventDefinitions = source.getEventDefinition();
        return eventDefinitions.stream()
                .map(JAXBElement::getValue)
                .map(e -> (EventDefinition) mapNext(e, attachedProcessId))
                .collect(Collectors.toSet());
    }

    @Mappings({
            @Mapping(source = "timeCycle", target = "has_timeCycle"),
            @Mapping(target = "id", source = ".", qualifiedByName = "generateIdForTimerEvent"),
    })
    public abstract TimerEventDefinition timerEventDefinitionToTimerEventDefinition(TTimerEventDefinition timerEventDefinition, @Context String attachedProcessId);
    @AfterMapping
    public void processTimerEventDefinitionProperties(TTimerEventDefinition timerEventDefinition, @MappingTarget TimerEventDefinition eventDefResult) {
//        getAfterMapping().add(() -> {
//             FIXME suppose every time definition in Bonita is cycle
//            TExpression timeDuration = timerEventDefinition.getTimeCycle();
//            String timeTargetId = sourceToTargetIds.get(timeDuration.getId());
//            Expression durationExpression = (Expression) getMappedObjectsById().get(timeTargetId);
//            eventDefResult.setHas_timeDuration(durationExpression);
//        });
    }

    @Mappings({
            @Mapping(target = "id", source = "content", qualifiedByName = "generateIdForTimeDuration"),
    })
    public abstract TimeExpression timeExpressionToTimeExpression(TExpression timeDuration, @Context String attachedProcessId);
    @AfterMapping
    public void processTimeExpressionProperties(TExpression timeDuration, @MappingTarget TimeExpression timeExpression) {
        getAfterMapping().add(new AfterMappingAction(mappingContext.getProcess(), (p) -> {
            if (timeExpression.getProperties() == null) {
                timeExpression.setProperties(new HashMap<>());
            }
            List<Serializable> content = timeDuration.getContent();
            Long aLong = convertDurationToLong(content);
            timeExpression.getProperties().put(
                    Vocabulary.s_p_value, Collections.singleton(Duration.ofMillis(aLong).toString())
            );
        }));
    }

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

    @AfterMapping
    public void afterAnyMapping(Object mappingSource, @MappingTarget Thing mappingTarget) {
        String id = mappingTarget.getId();
        sourceToTargetIds.put(ConverterMappingUtils.extractSourceId(mappingSource), id);
    }

    // -----------------------------------  -----------------------------------

    // if participant has process ref, then it is a process; otherwise it is an actor
    private boolean isProcess(TParticipant participant) {
        return participant.getProcessRef() != null;
    }

    @Named("processId")
    @Override
    protected String processId(String id) {
        return getConfiguration().getIriMappingFunction().apply(id);
    }

    @Named("generateIdForTimeDuration")
    protected String generateIdForTimeDuration(List<Serializable> duration, @Context String attachedProcessId) {
        // generate composite id
        Long durationSec = convertDurationToLong(duration);
        String poolName = mappingContext.getPoolId();
        String id = StringUtils.joinWith(COMPOSITE_ID_DELIMITER, poolName, attachedProcessId, durationSec);
        return processId(id);
    }

    @Named("generateIdForTimerEvent")
    protected String generateIdForTimerEvent(TTimerEventDefinition source, @Context String attachedProcessId) {
        // generate composite id
        String timerEventId = source.getId();
        String poolName = mappingContext.getPoolId();
        String id = StringUtils.joinWith(COMPOSITE_ID_DELIMITER, poolName, attachedProcessId, timerEventId);
        return processId(id);
    }

    private Long convertDurationToLong(List<Serializable> duration) {
        String collect = duration.stream().map(String.class::cast).collect(Collectors.joining());
        String longString = collect.replaceAll("\\D", "");
        if (longString.isEmpty())
            return 0L;
        return Long.valueOf(longString);
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

    /**
     * Container for contextual data for the mapping.
     */
    public static class MappingContext {
        private String poolId;
        private String poolName;
        private Process process;

        public MappingContext(String poolId, String poolName) {
            this.poolId = poolId;
            this.poolName = poolName;
        }

        public String getPoolId() {
            return poolId;
        }

        public void setPoolId(String poolId) {
            this.poolId = poolId;
        }

        public String getPoolName() {
            return poolName;
        }

        public void setPoolName(String poolName) {
            this.poolName = poolName;
        }

        public Process getProcess() {
            return process;
        }

        public void setProcess(Process process) {
            this.process = process;
        }
    }
    
    public static class AfterMappingAction {
        private Process process;
        private Consumer<Process> action;

        public AfterMappingAction(Runnable action) {
            this.action = (a) -> action.run();
        }

        public AfterMappingAction(Process process, Consumer<Process> action) {
            this.process = process;
            this.action = action;
        }

        public Process getProcess() {
            return process;
        }

        public void setProcess(Process process) {
            this.process = process;
        }

        public Consumer<Process> getAction() {
            return action;
        }

        public void setAction(Consumer<Process> action) {
            this.action = action;
        }
        
        public void run() {
            if (action == null) return;
            action.accept(process);
        }
    }

    public MappingContext getMappingContext() {
        return mappingContext;
    }

    public void setMappingContext(MappingContext mappingContext) {
        this.mappingContext = mappingContext;
    }
}
