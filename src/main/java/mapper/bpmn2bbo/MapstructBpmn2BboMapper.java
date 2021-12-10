package mapper.bpmn2bbo;

import common.Constants;
import mapper.OntologyMapstructMapper;
import model.bbo.Vocabulary;
import model.bbo.model.Process;
import model.bbo.model.*;
import model.bpmn.org.omg.spec.bpmn._20100524.model.*;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import utils.MappingUtils;

import javax.xml.bind.JAXBElement;
import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.isEmpty;

@Mapper
public abstract class MapstructBpmn2BboMapper extends OntologyMapstructMapper {

    private final Map<String, String> sourceToTargetIds;
    private final Bpmn2BboMappingResult result;

    public MapstructBpmn2BboMapper() {
        this.sourceToTargetIds = new HashMap<>();
        this.result = new Bpmn2BboMappingResult(getMappedObjectsById());
    }

    public Bpmn2BboMappingResult processDefinitions(TDefinitions definitions) {
        List<JAXBElement<? extends TRootElement>> rootElement = definitions.getRootElement();
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
                Process process = processToProcess(tProcess);
                result.getProcesses().put(tProcess.getId(), process);
                for (JAXBElement<? extends TFlowElement> flow : tProcess.getFlowElement()) {
                    TFlowElement flowElement = flow.getValue();
                    FlowElement resFlowElement = (FlowElement) mapNext(flowElement);
                    result.getFlowElements().put(resFlowElement.getId(), resFlowElement);
                    getAfterMapping().add(() -> {
                        if (process.getHas_flowElements() == null) {
                            process.setHas_flowElements(new HashSet<>());
                        }
                        process.getHas_flowElements().add(resFlowElement);
                    });
                }
            }
        }
        result.setElementsIdMapping(sourceToTargetIds);
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
    public void processUserTaskObjectProperties(TUserTask userTask, @MappingTarget UserTask userTaskResult) {
        getAfterMapping().add(() -> {
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
        });
    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
    })
    public abstract NormalSequenceFlow sequenceFlowToNormalSequenceFlow(TSequenceFlow sequenceFlow);
    @AfterMapping
    public void processNormalSequenceFlowObjectProperties(TSequenceFlow sequenceFlow, @MappingTarget NormalSequenceFlow normalSequenceFlowResult) {
        getAfterMapping().add(() -> {
            TBaseElement targetRef = (TBaseElement) sequenceFlow.getTargetRef();
            String targetRefTargetId = sourceToTargetIds.get(targetRef.getId());
            normalSequenceFlowResult.setHas_targetRef(Sets.newHashSet(
                    getMappedObjectsById().get(targetRefTargetId))
            );
            TBaseElement sourceRef = (TBaseElement) sequenceFlow.getSourceRef();
            String sourceRefTargetId = sourceToTargetIds.get(sourceRef.getId());
            normalSequenceFlowResult.setHas_sourceRef(
                    (FlowNode) getMappedObjectsById().get(sourceRefTargetId)
            );
        });
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
            @Mapping(source = "eventDefinition", target = "has_eventDefinition", qualifiedByName = "thiMethod"),
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
            @Mapping(target = "name", source = "name", qualifiedByName = "nullifyEmpty")
    })
    public abstract InterruptingBoundaryEvent boundaryEventToInterruptingBoundaryEvent(TBoundaryEvent tBoundaryEvent);
    @AfterMapping
    public void processInterruptingBoundaryEventProperties(TBoundaryEvent tBoundaryEvent, @MappingTarget InterruptingBoundaryEvent eventResult) {
        getAfterMapping().add(() -> {
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
        });
    }


    @Named("thiMethod")
    public Set<EventDefinition> thiMethod(List<JAXBElement<? extends TEventDefinition>> eventDefinitions) {
        return eventDefinitions.stream()
                .map(JAXBElement::getValue)
                .map(e -> (EventDefinition) mapNext(e))
                .collect(Collectors.toSet());
    }

    @Mappings({
            @Mapping(source = "timeCycle", target = "has_timeCycle"),
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
    })
    public abstract TimerEventDefinition timerEventDefinitionToTimerEventDefinition(TTimerEventDefinition timerEventDefinition);
//    @AfterMapping
//    public void processTimerEventDefinitionProperties(TTimerEventDefinition timerEventDefinition, @MappingTarget TimerEventDefinition eventDefResult) {
//        getAfterMapping().add(() -> {
//             FIXME suppose every time definition in Bonita is cycle
//            TExpression timeDuration = timerEventDefinition.getTimeCycle();
//            String timeTargetId = sourceToTargetIds.get(timeDuration.getId());
//            Expression durationExpression = (Expression) getMappedObjectsById().get(timeTargetId);
//            eventDefResult.setHas_timeDuration(durationExpression);
//        });
//    }

    @Mappings({
            @Mapping(target = "id", source = "id", qualifiedByName = "processId"),
    })
    public abstract TimeExpression timeExpressionToTimeExpression(TExpression timeDuration);
    @AfterMapping
    public void processTimeExpressionProperties(TExpression timeDuration, @MappingTarget TimeExpression timeExpression) {
        getAfterMapping().add(() -> {
            if (timeExpression.getProperties() == null) {
                timeExpression.setProperties(new HashMap<>());
            }
            List<Serializable> content = timeDuration.getContent();
            String collect = content.stream().map(String.class::cast).collect(Collectors.joining());
            Long aLong = Long.valueOf(collect.replaceAll("\\D", ""));
            timeExpression.getProperties().put(
                    Vocabulary.s_p_value, Collections.singleton(Duration.ofMillis(aLong).toString())
            );
        });
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
        mappingTarget.setId(id);
        sourceToTargetIds.put(MappingUtils.extractSourceId(mappingSource), id);
    }

    // -----------------------------------  -----------------------------------

    // if participant has process ref, then it is a process; otherwise it is an actor
    private boolean isProcess(TParticipant participant) {
        return participant.getProcessRef() != null;
    }

    @Named("processId")
    @Override
    protected String processId(String id) {
        return MappingUtils.transformToUriCompliant(getTargetIdBase(), id);
    }

    @Named("nullifyEmpty")
    protected String nullifyEmpty(String name) {
        if (StringUtils.isEmpty(name))
            return null;
        return name;
    }
}
