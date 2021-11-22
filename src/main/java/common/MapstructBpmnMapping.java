package common;

import model.bbo.model.EndEvent;
import model.bbo.model.NormalSequenceFlow;
import model.bbo.model.Process;
import model.bbo.model.Role;
import model.bbo.model.StartEvent;
import model.bbo.model.UserTask;
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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.xml.bind.JAXBElement;
import java.util.List;

import static com.google.common.collect.Iterables.isEmpty;

@Mapper
public interface MapstructBpmnMapping {

    @Mapping(source = "rootElement", target = "result")
    default BBOMappingResult definitions(TDefinitions definitions) {
        BBOMappingResult result = rootElementMapping(definitions.getRootElement());
        return result;
    }

    //FIXME rename methods
    //FIXME component model

    EndEvent endEventToEventType(TEndEvent endEvent);

    StartEvent startEventToEventType(TStartEvent startEvent);

    UserTask userTaskToEventType(TUserTask userTask);

    // TODO timer event

    NormalSequenceFlow sequenceFlowToNext(TSequenceFlow sequenceFlow);

    Role participantToRole(TParticipant participant);

    Process participantToProcess(TParticipant participant);
    // actor to Role

    // ---------------------------------------------------------------------

//    default ControlledProcess rootElementToControlledProcess(List<JAXBElement<? extends TRootElement>> rootElement) {
//        if (isEmpty(rootElement)) return null;
//        return rootElement.stream()
//                .filter(e -> e.getValue() instanceof TProcess)
//                .map(e -> processToControlledProcess((TProcess) e.getValue()))
//                .findFirst()
//                .orElse(null);
//    }
//
//    @Named("")
//    default List<ActionControlConnection> rootElementToActionControlConnections(List<JAXBElement<? extends TRootElement>> rootElement) {
//        if (isEmpty(rootElement)) return null;
////        return rootElement.stream()
////                .filter(e -> e.getValue() instanceof TCollaboration)
//        return filterAndMapValue(rootElement.stream(), TCollaboration.class)
//                .filter(this::isAnActor)
//                .map(this::collaborationToActionControlConnection)
//                .collect(Collectors.toList());
//    }
//
//    ActionControlConnection collaborationToActionControlConnection(TCollaboration collaboration);
//
//    private boolean isAnActor(TCollaboration collaboration) {
////        return collaboration.
//        return true;
//    }

    default BBOMappingResult rootElementMapping(List<JAXBElement<? extends TRootElement>> rootElement) {
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
                    if (flowElement instanceof TStartEvent)
                        result.getFlowElements().add(startEventToEventType((TStartEvent) flowElement));
                    if (flowElement instanceof TEndEvent)
                        result.getFlowElements().add(endEventToEventType((TEndEvent) flowElement));
                    if (flowElement instanceof TUserTask)
                        result.getFlowElements().add(userTaskToEventType((TUserTask) flowElement));
                    if (flowElement instanceof TSequenceFlow) {
                        result.getFlowElements().add(sequenceFlowToNext((TSequenceFlow) flowElement));
                    }
                }
            }
        }
        return result;
    }

    // if participant has process ref, then it is a process; otherwise it is an actor
    private boolean isProcess(TParticipant participant) {
        return participant.getProcessRef() != null;
    }
}
