package mapper.bpmn2bbo;

import mapper.OntologyMapstructMapper;
import model.bbo.model.Process;
import model.bbo.model.*;
import model.bpmn.org.omg.spec.bpmn._20100524.model.*;
import org.apache.commons.compress.utils.Sets;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.isEmpty;

@Mapper
public abstract class MapstructBpmnMapper extends OntologyMapstructMapper<Thing> {

    public Bpmn2BboMappingResult processDefinitions(TDefinitions definitions) {
        List<JAXBElement<? extends TRootElement>> rootElement = definitions.getRootElement();
        if (isEmpty(rootElement)) return null;

        Bpmn2BboMappingResult result = new Bpmn2BboMappingResult();

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
                    FlowElement resFlowElement = (FlowElement) map(flowElement);
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
        return result;
    }

    //FIXME component model

    public abstract EndEvent endEventToEndEvent(TEndEvent endEvent);

    public abstract StartEvent startEventToStartEvent(TStartEvent startEvent);

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
                Role role = (Role) getMappedObjects().get(resourceId);
                if (role.getIs_responsibleFor() == null) {
                    role.setIs_responsibleFor(new HashSet<>());
                }
                role.getIs_responsibleFor().add(userTaskResult);
            }
        });
    }

    public abstract NormalSequenceFlow sequenceFlowToNormalSequenceFlow(TSequenceFlow sequenceFlow);
    @AfterMapping
    public void processNormalSequenceFlowObjectProperties(TSequenceFlow sequenceFlow, @MappingTarget NormalSequenceFlow normalSequenceFlowResult) {
        getAfterMapping().add(() -> {
            TBaseElement targetRef = (TBaseElement) sequenceFlow.getTargetRef();
            normalSequenceFlowResult.setHas_targetRef(Sets.newHashSet(
                    (Thing) getMappedObjects().get(targetRef.getId()))
            );
            TBaseElement sourceRef = (TBaseElement) sequenceFlow.getSourceRef();
            normalSequenceFlowResult.setHas_sourceRef(
                    (FlowNode) getMappedObjects().get(sourceRef.getId())
            );
        });
    }

    public abstract Role participantToRole(TParticipant participant);

    public abstract Process processToProcess(TProcess process);

//    @Mapping(source = "eventDefinition", target = "a", qualifiedByName = "asd")
    public abstract InterruptingBoundaryEvent boundaryEventToInterruptingBoundaryEvent(TBoundaryEvent tBoundaryEvent);
    @AfterMapping
    public void processInterruptingBoundaryEventProperties(TBoundaryEvent tBoundaryEvent, @MappingTarget InterruptingBoundaryEvent eventResult) {
        getAfterMapping().add(() -> {
            String activityId = tBoundaryEvent.getAttachedToRef().getLocalPart();
            Activity activity = (Activity) getMappedObjects().get(activityId);
            if (activity.getHas_boundaryEventRef() == null) activity.setHas_boundaryEventRef(new HashSet<>());
            activity.getHas_boundaryEventRef().add(eventResult);

            tBoundaryEvent.getEventDefinition().stream().map(JAXBElement::getValue).forEach(definition -> {
                EventDefinition eventDefinition = (EventDefinition) getMappedObjects().get(definition.getId());
                if (eventResult.getHas_eventDefinition() == null) eventResult.setHas_eventDefinition(new HashSet<>());
                eventResult.getHas_eventDefinition().add(eventDefinition);
            });
        });
    }

    public List<EventDefinition> asd(List<JAXBElement<? extends TEventDefinition>> eventDefinitions) {
        return eventDefinitions.stream().map(JAXBElement::getValue).map(e -> (EventDefinition) map(e)).collect(Collectors.toList());
    }

//        BoundaryEvent boundaryEvent = new BoundaryEvent();
//        boundaryEvent.setHas_eventDefinition();
//
//        List<TEventDefinition> eventDefinitions = tBoundaryEvent.getEventDefinition().stream()
//                .map(JAXBElement::getValue)
//                .collect(Collectors.toList());
//
//        for (TEventDefinition eventDef : eventDefinitions) {
//            Event mappedEvent = null;
//            if (eventDef instanceof TTimerEventDefinition) {
//                mappedEvent = timerEventDefinitionToTimerEvent((TTimerEventDefinition) eventDef);
//            } else {
//                LOG.warn("Unknown event definition, mapping skipped for {} ({})}", eventDef, eventDef.getClass());
//            }
//
//            if (mappedEvent != null) {
//                String attachedToRef = tBoundaryEvent.getAttachedToRef().getLocalPart();
//                Activity activity = (Activity) getMappedObjects().get(attachedToRef);
//                activity.setHas_boundaryEventRef(mappedEvent);
//            }
//        }
//        return eventResult;

    public abstract TimerEventDefinition timerEventDefinitionToTimerEventDefinition(TTimerEventDefinition timerEventDefinition);
    @AfterMapping
    public void processTimerEventDefinitionProperties(TTimerEventDefinition timerEventDefinition, @MappingTarget TimerEventDefinition eventDefResult) {
        getAfterMapping().add(() -> {
            TExpression timeDuration = timerEventDefinition.getTimeDuration();
            Expression durationExpression = (Expression) getMappedObjects().get(timeDuration.getId());
            eventDefResult.setHas_timeDuration(durationExpression);
        });
    }

    public abstract TimeExpression timeExpressionToTimeExpression(TExpression timeDuration);
    @AfterMapping
    public void processTimeExpressionProperties(TExpression timeDuration, @MappingTarget TimeExpression timeExpression) {
        getAfterMapping().add(() -> {
            System.out.println(timeExpression);
        });
    }

    // ---------------------------------------------------------------------

    // if participant has process ref, then it is a process; otherwise it is an actor
    private boolean isProcess(TParticipant participant) {
        return participant.getProcessRef() != null;
    }

    @Override
    protected String getId(Thing obj) {
        return obj.getId();
    }

}
