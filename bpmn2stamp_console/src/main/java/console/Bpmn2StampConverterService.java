package console;

import com.google.common.collect.Sets;
import org.apache.commons.io.FilenameUtils;
import org.example.mapper.bbo2stamp.Bbo2StampMappingResult;
import org.example.mapper.bpmn2bbo.Bpmn2BboMappingResult;
import org.example.mapper.org2bbo.Org2BboMappingResult;
import org.example.model.actor.ActorMappings;
import org.example.persistance.RdfRepositoryWriter;
import org.example.service.Bbo;
import org.example.service.ConverterMappingService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Bpmn2StampConverterService {

    private ConverterMappingService service;

    public Bpmn2StampConverterService(String baseBpmnAsBboOntologyIri, String baseOrganizationStructureAsBboOntologyIri, String baseStampOntologyIri) {
        this.service = new ConverterMappingService(baseBpmnAsBboOntologyIri, baseOrganizationStructureAsBboOntologyIri, baseStampOntologyIri);
    }

    public void convertToBbo(File bpmnFile, File orgFile, List<File> actorMappingFiles, File outputFile) {
        Bbo bbo = performConversionToBbo(bpmnFile, orgFile, actorMappingFiles);
        saveToRdf(outputFile,
                service.getBaseBpmnAsBboOntologyIri(),
                Sets.newHashSet(service.getBboOntologyIri()),
                bbo.getAllObjects()
        );
    }

    public void convertToStamp(File bboFile, File outputFile) {
        Bbo2StampMappingResult result2 = service.transformBboToStamp(bboFile);
        HashMap<String, File> map = new HashMap<>();
        map.put(service.getBboOntologyIri(), bboFile);
        saveToRdf(outputFile,
                service.getBaseStampOntologyIri(),
                Sets.newHashSet(service.getBboOntologyIri(), service.getStampOntologyIri(), service.getBaseBpmnAsBboOntologyIri()),
                result2.getMappedObjects().values(),
                map
        );
    }

    public void convertToStamp(File bpmnFile, File orgFile, List<File> actorMappingFiles, File outputFile) {
        doConversionWithUsingReasoner(bpmnFile, orgFile, actorMappingFiles, outputFile, true);
    }

    public void convertToStampAndBbo(File bpmnFile, File orgFile, List<File> actorMappingFiles, File outputFile) {
        doConversionWithUsingReasoner(bpmnFile, orgFile, actorMappingFiles, outputFile, false);
    }

    private Bbo performConversionToBbo(File bpmnFile, File orgFile, List<File> actorMappingFile) {
        Bpmn2BboMappingResult result = service.transformBpmnToBbo(bpmnFile);
        Org2BboMappingResult result1 = service.transformOrganizationStructureToBbo(orgFile);

        List<ActorMappings> actorMappingsList = new ArrayList<>();
        for (File amFile : actorMappingFile) {
            ActorMappings actorMappings = service.readActorMappingFile(amFile);
            actorMappingsList.add(actorMappings);
        }
        service.connectRolesToGroups(result1.getOrganizationBbo(), actorMappingsList);

        return service.mergeBboOntologies(result1.getOrganizationBbo(), result.getBpmnAsBbo(), actorMappingsList);
    }


    private void doConversionWithUsingReasoner(File bpmnFile, File orgFile, List<File> actorMappingFile, File outputFile, boolean bboIsTemp) {
        try {
            File bboFile;
            File stampFile = addSuffix(outputFile, "-prestamp");
            if (bboIsTemp)
                bboFile = File.createTempFile("bbo-temp-", ".ttl");
            else {
                bboFile = addSuffix(outputFile, "-bbo");
            }
            // creates file so it can be read
            convertToBbo(bpmnFile, orgFile, actorMappingFile, bboFile);
            // read the file so the reasoner is used
            convertToStamp(bboFile, stampFile);
        } catch (IOException e) {
            System.err.println("Could not create reasoned BBO file.");
        }
    }

    private File addSuffix(File file, String suffix) {
        String extension = FilenameUtils.getExtension(file.getAbsolutePath());
        String fileName = FilenameUtils.getBaseName(file.getAbsolutePath());
        String filePath = FilenameUtils.getFullPath(file.getAbsolutePath());
        return Path.of(filePath, fileName, "-bbo", extension).toFile();
    }

    private void saveToRdf(File targetFile, String targetBaseIri, Set<String> imports, Iterable<?> objectsToSave, Map<String, File>... additionalImports) {
        RdfRepositoryWriter rdfRepositoryWriter = new RdfRepositoryWriter(
                targetFile.getAbsolutePath(),
                targetBaseIri,
                imports,
                additionalImports
        );
        rdfRepositoryWriter.write(objectsToSave);
    }

//    public void runBpmn2Stamp(File bpmnFile, File orgFile, File actorMappingFile, String baseIri, File outputDir) {
//        ConverterXmlFileReader converterXmlFileReader = new ConverterXmlFileReader();
//
//        String pre = baseIri;
//        if (baseIri.endsWith("/"))
//            pre = baseIri.substring(0, baseIri.length()-1);
//        String bpmnOntologyIri = pre + "-bpmn";
//        String organizationStructureOntologyIri = pre + "-organization-structure";
//        String preStampOntologyIri = pre + "-prestamp";
//
//        File bpmnOntoFile = outputDir.toPath().resolve("bpmn.owl").toFile();
//        File orgOntoFile = outputDir.toPath().resolve("org.owl").toFile();
//        File prestampOntoFile = outputDir.toPath().resolve("prestamp.owl").toFile();
//
//        TDefinitions tDefinitions = converterXmlFileReader.readBpmn(bpmnFile.getAbsolutePath());
//        Bpmn2BboMappingService bpmnMapper = new Bpmn2BboMappingService();
//        Bpmn2BboMappingResult bpmnResult = bpmnMapper.transform(
//                tDefinitions,
//                bpmnOntologyIri
//        );
//        new RdfRepositoryWriter(
//                bpmnOntoFile.getAbsolutePath(),
//                bpmnOntologyIri,
//                Sets.newHashSet(organizationStructureOntologyIri)
//        ).write(bpmnResult.getMappedObjects().values());
//
//        ActorMappings actorMappings = converterXmlFileReader.readActorMappings(actorMappingFile.getAbsolutePath());
//
//        Organization organization = converterXmlFileReader.readOrganizationStructure(orgFile.getAbsolutePath());
//        Organization2BboMappingService organization2BboMappingService = new Organization2BboMappingService();
//        Org2BboMappingResult organizationResult = organization2BboMappingService.transform(
//                organization,
//                organizationStructureOntologyIri
//        );
//        OrganizationAsBbo organizationAsBbo = organization2BboMappingService.extendOrganizationHierarchy(organizationResult.getOrganizationBbo(), actorMappings);
//        RdfRepositoryWriter organizationRepoWriter = new RdfRepositoryWriter(
//                orgOntoFile.getAbsolutePath(),
//                organizationStructureOntologyIri,
//                Sets.newHashSet("http://BPMNbasedOntology")
//        );
//        organizationRepoWriter.write(organizationAsBbo.getAllObjects().values());






//        BboRdfRepositoryReader rdfRepositoryReader = new BboRdfRepositoryReader(
//                bpmnOntoFile.getAbsolutePath(),
//                bpmnOntologyIri
//        );
//        List<Thing> list = rdfRepositoryReader.readAll();
//
//        BboRdfRepositoryReader rdfRepositoryReader2 = new BboRdfRepositoryReader(
//                orgOntoFile.getAbsolutePath(),
//                organizationStructureOntologyIri
//        );
//        List<Thing> list2 = rdfRepositoryReader2.readAll();
//        list.addAll(list2);
//        RdfRepositoryWriter preStampRepoWriter = new RdfRepositoryWriter(
//                prestampOntoFile.getAbsolutePath(),
//                preStampOntologyIri,
//                Sets.newHashSet("http://onto.fel.cvut.cz/ontologies/stamp", bpmnOntologyIri)
//        );
//        Bbo2StampMappingService bbo2StampMappingService = new Bbo2StampMappingService();
//        Bbo2StampMappingResult preStampResult = bbo2StampMappingService.transform(
//                list.stream().filter(Objects::nonNull).collect(Collectors.toList()),
//                preStampOntologyIri
//        );
//        preStampRepoWriter.write(preStampResult.getMappedObjects().values());
//        createFileIfMissing(outputDir);
//    }

}