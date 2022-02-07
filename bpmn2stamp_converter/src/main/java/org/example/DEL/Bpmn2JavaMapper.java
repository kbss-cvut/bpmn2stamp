//package bpmn2java;
//
//import common.MapstructPreStampMapping;
//import model.bpmn.org.omg.spec.bpmn._20100524.model.TCollaboration;
//import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
//import model.bpmn.org.omg.spec.bpmn._20100524.model.TFlowElement;
//import model.bpmn.org.omg.spec.bpmn._20100524.model.TProcess;
//import model.bpmn.org.omg.spec.bpmn._20100524.model.TRootElement;
//import model.stamp.model.EventType;
//import model.stamp.model.Next;
//import model.stamp.model.ProcessConnection;
//import org.mapstruct.factory.Mappers;
//
//import javax.xml.bind.JAXBElement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class Bpmn2JavaMapper {
//
//    private final MapstructPreStampMapping mapper;
//
//    public Bpmn2JavaMapper() {
//        this.mapper = Mappers.getMapper(MapstructPreStampMapping.class);
//    }
//
//
//    public void transform(TDefinitions bpmn) {
//        List<TRootElement> rootElement = bpmn.getRootElement()
//                .stream().map(JAXBElement::getValue).collect(Collectors.toList());
//
//        List<TCollaboration> collaborations = new ArrayList<>();
//        List<TProcess> pools = new ArrayList<>();
//        for (TRootElement element : rootElement) {
//            if (element instanceof TCollaboration) {
//                collaborations.add((TCollaboration) element);
//            } else if (element instanceof TProcess) {
//                pools.add((TProcess) element);
//            }
//        }
//
//        for (TProcess pool : pools) {
////            EventType poolEventType = mapper.processToEventType(pool);
//            for (JAXBElement<? extends TFlowElement> jaxbElement : pool.getFlowElement()) {
//                TFlowElement flowElement = jaxbElement.getValue();
//
//                EventType eventType = mapToEvent(flowElement);
//                if (eventType == null) {
//                    Next next = mapToNext(flowElement);
//                }
//
//                ProcessConnection processConnection = new ProcessConnection();
////                processConnection.setProcess_connection_from(Collections.singleton(poolEventType));
////                processConnection.setProcess_connection_to(Collections.singleton(flowElement));
//
//            }
//        }
//    }
//
//    private EventType mapToEvent(TFlowElement flowElement) {
////        if (flowElement instanceof TStartEvent) {
////            return mapper.startEventToEventType((TStartEvent) flowElement);
////        } else if (flowElement instanceof TEndEvent) {
////            return mapper.endEventToEventType((TEndEvent) flowElement);
////        } else if (flowElement instanceof TUserTask) {
////            return mapper.userTaskToEventType((TUserTask) flowElement);
////        }
//        return null;
//    }
//
//    private Next mapToNext(TFlowElement flowElement) {
////        if (flowElement instanceof TSequenceFlow) {
////            Next sequenceFlowToNext = mapper.sequenceFlowToNext((TSequenceFlow) flowElement);
////            Object sourceRef = ((TSequenceFlow) flowElement).getSourceRef();
////            sequenceFlowToNext.setProcess_connection_from();
////            Object targetRef = ((TSequenceFlow) flowElement).getTargetRef();
////            sequenceFlowToNext.setProcess_connection_to((ControlEventType)targetRef);
////            return sequenceFlowToNext;
////        }
//        return null;
//    }
//
//
//
//}
