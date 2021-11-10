package common;

import com.google.common.collect.Iterables;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TCollaboration;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TEndEvent;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TProcess;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TRootElement;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TStartEvent;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TUserTask;
import model.rdf.model.ActionCapability;
import model.rdf.model.ControlledProcess;
import model.rdf.model.EventType;
import model.rdf.model.Next;
import model.rdf.model.Thing;
import org.apache.commons.lang3.ArrayUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.xml.bind.JAXBElement;
import java.util.List;

import static com.google.common.collect.Iterables.*;

@Mapper
public interface MapstructBpmnMapping {

    @Mapping(source = "rootElement", target = "object", resultType = ControlledProcess.class)
    @Mapping(source = "rootElement", target = "object2", resultType = Thing.class)
    Def definitions(TDefinitions definitions);

    default ControlledProcess rootElementToControlledProcess(List<JAXBElement<? extends TRootElement>> rootElement) {
        if (isEmpty(rootElement)) return null;
        return rootElement.stream()
                .filter(e -> e.getValue() instanceof TProcess)
                .map(e -> processToControlledProcess((TProcess) e.getValue()))
                .findFirst()
                .orElse(null);
    }

    default Thing rootElementToCollaboration(List<JAXBElement<? extends TRootElement>> rootElement) {
        if (isEmpty(rootElement)) return null;
        return rootElement.stream()
                .filter(e -> e.getValue() instanceof TCollaboration)
                .map(e -> collaborationToThing((TCollaboration) e.getValue()))
                .findFirst()
                .orElse(null);
    }

    ControlledProcess processToControlledProcess(TProcess process);

    EventType collaborationToThing(TCollaboration collaboration);

    ActionCapability userTaskToEventType(TUserTask userTask);

    EventType endEventToEventType(TEndEvent endEvent);

    EventType startEventToEventType(TStartEvent startEvent);

//    @Mapping(source = "workingEnvironmentModels", target = "subGroups")
    Next sequenceFlowToNext(TSequenceFlow sequenceFlow);

    public static class Def {
        public ControlledProcess object;
        public Thing object2;
    }

}
