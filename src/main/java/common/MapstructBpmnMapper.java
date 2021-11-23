package common;

import model.bbo.model.EndEvent;
import model.bbo.model.FlowElement;
import model.bbo.model.FlowNode;
import model.bbo.model.NormalSequenceFlow;
import model.bbo.model.Process;
import model.bbo.model.Role;
import model.bbo.model.StartEvent;
import model.bbo.model.Thing;
import model.bbo.model.UserTask;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TBaseElement;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TCollaboration;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TEndEvent;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TFlowElement;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TParticipant;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TProcess;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TRootElement;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TStartEvent;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TUserTask;
import org.apache.commons.compress.utils.Sets;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import javax.xml.bind.JAXBElement;
import java.util.List;

import static com.google.common.collect.Iterables.isEmpty;

@Mapper
public abstract class MapstructBpmnMapper extends SmartMapstructMapper {

    @Mapping(source = "rootElement", target = "result")
    public BBOMappingResult definitions(TDefinitions definitions) {
        BBOMappingResult result = rootElementMapping(definitions.getRootElement());
        return result;
    };

    //FIXME rename methods
    //FIXME component model

    abstract EndEvent endEventToEndEvent(TEndEvent endEvent);

    abstract StartEvent startEventToStartEvent(TStartEvent startEvent);

    abstract UserTask userTaskToUserTask(TUserTask userTask);

    // TODO timer event

    abstract NormalSequenceFlow sequenceFlowToNormalSequenceFlow(TSequenceFlow sequenceFlow);
    @AfterMapping
    public void normalSequenceFlowObjectProperties(TSequenceFlow sequenceFlow, @MappingTarget NormalSequenceFlow normalSequenceFlow) {
        getAfterMapping().add(() -> {
            TBaseElement targetRef = (TBaseElement) sequenceFlow.getTargetRef();
            normalSequenceFlow.setHas_targetRef(Sets.newHashSet(
                    (Thing) getMappedObjects().get(targetRef.getId()))
            );
            TBaseElement sourceRef = (TBaseElement) sequenceFlow.getSourceRef();
            normalSequenceFlow.setHas_sourceRef(
                    (FlowNode) getMappedObjects().get(sourceRef.getId())
            );
        });
    }

//    @Named("targetRef")
//    default Set<Thing> targetRef(Object value) {
//        if (value instanceof FlowElement) {
//            return Sets.newHashSet(flowElementToThing((FlowElement) value));
//        }
//        return null;
//    };
//    abstract Thing flowElementToThing(FlowElement value);

    abstract Role participantToRole(TParticipant participant);

    abstract Process participantToProcess(TParticipant participant);
    // actor to Role

    // ---------------------------------------------------------------------

    BBOMappingResult rootElementMapping(List<JAXBElement<? extends TRootElement>> rootElement) {
        if (isEmpty(rootElement)) return null;

        BBOMappingResult result = new BBOMappingResult();

        for (JAXBElement<? extends TRootElement> root : rootElement) {
            if (root.getValue() instanceof TCollaboration) {
                TCollaboration value = (TCollaboration) root.getValue();
                List<TParticipant> participants = value.getParticipant();
                for (TParticipant participant : participants) {
                    if (isProcess(participant)) {
                        result.getProcesses().add(participantToProcess(participant));
                    } else {
                        result.getRoles().add(participantToRole(participant));
                    }
                }
            }
            // TODO rework code below
            if (root.getValue() instanceof TProcess) {
                TProcess value = (TProcess) root.getValue();
                for (JAXBElement<? extends TFlowElement> flow : value.getFlowElement()) {
                    TFlowElement flowElement = flow.getValue();
                    FlowElement res = (FlowElement) map(flowElement);
                    result.getFlowElements().add(res);
                }
            }
        }
        return result;
    }

    // if participant has process ref, then it is a process; otherwise it is an actor
    private boolean isProcess(TParticipant participant) {
        return participant.getProcessRef() != null;
    }

//    private FlowNode mapFlowElement(Object flowElement) {
//        if (flowElement instanceof TStartEvent)
//           return startEventToEventType((TStartEvent) flowElement);
//        if (flowElement instanceof TEndEvent)
//            return endEventToEventType((TEndEvent) flowElement);
//        if (flowElement instanceof TUserTask)
//            return userTaskToEventType((TUserTask) flowElement);
//        return null;
//    }
}
