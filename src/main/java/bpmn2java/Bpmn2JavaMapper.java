package bpmn2java;

import common.MapstructBpmnMapping;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TCollaboration;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TEndEvent;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TFlowElement;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TProcess;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TRootElement;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TStartEvent;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TUserTask;
import model.rdf.model.ActionControlConnection;
import model.rdf.model.ControlEventType;
import model.rdf.model.EventType;
import model.rdf.model.Next;
import model.rdf.model.ProcessConnection;
import model.rdf.model.ProcessControlConnection;
import org.mapstruct.factory.Mappers;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Bpmn2JavaMapper {

    private final MapstructBpmnMapping mapper;

    public Bpmn2JavaMapper() {
        this.mapper = Mappers.getMapper(MapstructBpmnMapping.class);
    }


    public void transform(TDefinitions bpmn) {
        List<TRootElement> rootElement = bpmn.getRootElement()
                .stream().map(JAXBElement::getValue).collect(Collectors.toList());

        List<TCollaboration> collaborations = new ArrayList<>();
        List<TProcess> pools = new ArrayList<>();
        for (TRootElement element : rootElement) {
            if (element instanceof TCollaboration) {
                collaborations.add((TCollaboration) element);
            } else if (element instanceof TProcess) {
                pools.add((TProcess) element);
            }
        }

        for (TProcess pool : pools) {
//            EventType poolEventType = mapper.processToEventType(pool);
            for (JAXBElement<? extends TFlowElement> jaxbElement : pool.getFlowElement()) {
                TFlowElement flowElement = jaxbElement.getValue();

                EventType eventType = mapToEvent(flowElement);
                if (eventType == null) {
                    Next next = mapToNext(flowElement);
                }

                ProcessConnection processConnection = new ProcessConnection();
//                processConnection.setProcess_connection_from(Collections.singleton(poolEventType));
//                processConnection.setProcess_connection_to(Collections.singleton(flowElement));

            }
        }
    }

    private EventType mapToEvent(TFlowElement flowElement) {
//        if (flowElement instanceof TStartEvent) {
//            return mapper.startEventToEventType((TStartEvent) flowElement);
//        } else if (flowElement instanceof TEndEvent) {
//            return mapper.endEventToEventType((TEndEvent) flowElement);
//        } else if (flowElement instanceof TUserTask) {
//            return mapper.userTaskToEventType((TUserTask) flowElement);
//        }
        return null;
    }

    private Next mapToNext(TFlowElement flowElement) {
//        if (flowElement instanceof TSequenceFlow) {
//            Next sequenceFlowToNext = mapper.sequenceFlowToNext((TSequenceFlow) flowElement);
//            Object sourceRef = ((TSequenceFlow) flowElement).getSourceRef();
//            sequenceFlowToNext.setProcess_connection_from();
//            Object targetRef = ((TSequenceFlow) flowElement).getTargetRef();
//            sequenceFlowToNext.setProcess_connection_to((ControlEventType)targetRef);
//            return sequenceFlowToNext;
//        }
        return null;
    }



}
