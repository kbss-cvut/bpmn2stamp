package cz.cvut.kbss.bpmn2stamp.converter.service;

import cz.cvut.kbss.bpmn2stamp.converter.model.actor.ActorMappings;
import cz.cvut.kbss.bpmn2stamp.converter.model.actorConfig.Configuration;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Class provides methods for reading various types of ontology files, useful in the bpmn2stamp converter:
 * <ul>
 *     TODO
 * </ul>
 */
public class ConverterOntologyFileReader {

    private static final Logger LOG = LoggerFactory.getLogger(ConverterOntologyFileReader.class.getSimpleName());

    public TDefinitions readBpmn(String filePath) {
        JAXBElement<TDefinitions> jaxbElement = readFromXml(filePath, TDefinitions.class);
        return jaxbElement.getValue();
    }

    public Organization readOrganizationStructure(String filePath) {
        JAXBElement<Organization> jaxbElement = readFromXml(filePath, Organization.class);
        return jaxbElement.getValue();
    }

    public ActorMappings readActorMappings(String filePath) {
        JAXBElement<ActorMappings> jaxbElement = readFromXml(filePath, ActorMappings.class);
        return jaxbElement.getValue();
    }

    public Configuration readActorMappingConfig(String filePath) {
        JAXBElement<Configuration> jaxbElement = readFromXml(filePath, Configuration.class);
        return jaxbElement.getValue();
    }

    private <T> JAXBElement<T> readFromXml(String xmlFilePath, Class<T> resultType) {
        LOG.debug("Reading .xml file from: {}", xmlFilePath);
        File file = new File(xmlFilePath);
        try {
            validateFile(file);
        } catch (IOException e) {
            LOG.error("Input file validation failed.", e);
        }

        JAXBElement<T> bpmn = null;
        try {
            bpmn = parseFile(file, resultType);
        } catch (JAXBException | IOException | XMLStreamException e) {
            e.printStackTrace();
        }
        LOG.debug("Reading finished.");

        return bpmn;

//        TCollaboration collab = (TCollaboration) o.getValue().getRootElement().get(0).getValue();
//        TProcess process = (TProcess) o.getValue().getRootElement().get(1).getValue();

//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JsonLdModule());
//
//        String s1 = objectMapper.writeValueAsString(collab);
//        String s2 = objectMapper.writeValueAsString(process);
    }

//    private <T> JAXBElement<T> parseFile(File file, Class<T> resultType) throws JAXBException {
//        JAXBContext jaxbContext = JAXBContext.newInstance(resultType);
//
//        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//
//        return (JAXBElement<T>) jaxbUnmarshaller.unmarshal(file);
//    }

    private <T> JAXBElement<T> parseFile(File file, Class<T> resultType) throws JAXBException, IOException, XMLStreamException {
        JAXBContext jaxbContext2 = JAXBContext.newInstance(resultType);
        Unmarshaller jaxbUnmarshaller2 = jaxbContext2.createUnmarshaller();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader someSource = factory.createXMLEventReader(Files.newInputStream(file.toPath()));
        return jaxbUnmarshaller2.unmarshal(someSource, resultType);
    }
    private <T> JAXBElement<T> validateAndParseXml(File file, File xsdFile, Class<T> resultType) throws JAXBException, IOException, XMLStreamException {
        JAXBContext jaxbContext2 = JAXBContext.newInstance(resultType);
        Unmarshaller jaxbUnmarshaller2 = jaxbContext2.createUnmarshaller();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader someSource = factory.createXMLEventReader(Files.newInputStream(file.toPath()));
        return jaxbUnmarshaller2.unmarshal(someSource, resultType);
    }

    private <T> void validateFile(File xmlFile) throws IOException {
        if (!xmlFile.exists()) {
            throw new IOException(
                    String.format("File %s doesn't exist", xmlFile.getAbsolutePath()));
        }
    }

}
