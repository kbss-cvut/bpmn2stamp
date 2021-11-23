package bpmn2java;

import common.BBOMappingResult;
import common.Bpmn2BBOMapper;
import common.MapstructBpmnMapper;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class BpmnReaderServiceTest {

    private BpmnReaderService bpmnReaderService;

    @BeforeEach
    public void init() {
        this.bpmnReaderService = new BpmnReaderService();
    }

    @Test
    public void readFromXmlTest_resultShouldBePresent() throws IOException, JAXBException {
        String testFileBPMN = "src\\test\\java\\bpmn2java\\data\\Jednani-sag-ONLY.bpmn";
        TDefinitions tDefinitions = bpmnReaderService.readFromXml(testFileBPMN);

        Bpmn2BBOMapper mapper = new Bpmn2BBOMapper();
        BBOMappingResult transform = mapper.transform(tDefinitions);

//        RdfWriterService s = new RdfWriterService();
//        definitions.object.setInstance_of("ASD");
//        s.save(definitions.object);

        assertThat(transform).isNotNull();
        System.out.println(transform);
    }
}