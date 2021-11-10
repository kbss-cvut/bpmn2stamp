package bpmn2java;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.jsonld.jackson.JsonLdModule;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TCollaboration;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

/**
 * TODO provide more details
 * Provides methods for reading bpmn files and mapping them to BPMN model in java.
 */
public class BpmnReaderService {

    public static final Logger LOG = LoggerFactory.getLogger(BpmnReaderService.class.getName());

    public TDefinitions readFromXml(String xmlFilePath) throws JAXBException {
        LOG.info("Reading .xml file from: {}", xmlFilePath);
        File file = new File(xmlFilePath);
        try {
            validateFile(file);
        } catch (IOException e) {
            LOG.error("Input file validation failed.", e);
        }

        TDefinitions bpmn = parseFile(file).getValue();
        LOG.error("Reading finished.");

        return bpmn;

//        TCollaboration collab = (TCollaboration) o.getValue().getRootElement().get(0).getValue();
//        TProcess process = (TProcess) o.getValue().getRootElement().get(1).getValue();

//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JsonLdModule());
//
//        String s1 = objectMapper.writeValueAsString(collab);
//        String s2 = objectMapper.writeValueAsString(process);
    }

    private JAXBElement<TDefinitions> parseFile(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TDefinitions.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        return (JAXBElement<TDefinitions>) jaxbUnmarshaller.unmarshal(file);
    }

    private void validateFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException(
                    String.format("File %s doesn't exist", file.getAbsolutePath()));
        }
    }

}
