package common;

import model.bpmn.org.omg.spec.bpmn._20100524.model.TCollaboration;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TEndEvent;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TProcess;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TRootElement;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TStartEvent;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TUserTask;
import model.stamp.model.ActionCapability;
import model.stamp.model.ActionControlConnection;
import model.stamp.model.ControlledProcess;
import model.stamp.model.EventType;
import model.stamp.model.Next;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.*;
import static utils.StreamHelper.*;

@Mapper
public interface MapstructPreStampMapping {

//    @Mapping(source = "rootElement", target = "object", qualified = )
//    @Mapping(source = "rootElement", target = "object2", resultType = Thing.class)
//    Def definitions(TDefinitions definitions);

    default ControlledProcess rootElementToControlledProcess(List<JAXBElement<? extends TRootElement>> rootElement) {
        if (isEmpty(rootElement)) return null;
        return rootElement.stream()
                .filter(e -> e.getValue() instanceof TProcess)
                .map(e -> processToControlledProcess((TProcess) e.getValue()))
                .findFirst()
                .orElse(null);
    }

    @Named("")
    default List<ActionControlConnection> rootElementToActionControlConnections(List<JAXBElement<? extends TRootElement>> rootElement) {
        if (isEmpty(rootElement)) return null;
//        return rootElement.stream()
//                .filter(e -> e.getValue() instanceof TCollaboration)
        return filterAndMapValue(rootElement.stream(), TCollaboration.class)
                .filter(this::isAnActor)
                .map(this::collaborationToActionControlConnection)
                .collect(Collectors.toList());
    }

    ControlledProcess processToControlledProcess(TProcess process);

    ActionControlConnection collaborationToActionControlConnection(TCollaboration collaboration);

    ActionCapability userTaskToEventType(TUserTask userTask);

    EventType endEventToEventType(TEndEvent endEvent);

    EventType startEventToEventType(TStartEvent startEvent);

//    @Mapping(source = "workingEnvironmentModels", target = "subGroups")
    Next sequenceFlowToNext(TSequenceFlow sequenceFlow);

    private boolean isAnActor(TCollaboration collaboration) {
//        return collaboration.
        return true;
    }

}
