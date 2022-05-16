package cz.cvut.kbss.bpmn2stamp.console;

import com.google.common.collect.Sets;
import org.apache.commons.io.FilenameUtils;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.bbo2stamp.Bbo2StampMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.bpmn2bbo.Bpmn2BboMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.org2bbo.Org2BboMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.model.actor.ActorMappings;
import cz.cvut.kbss.bpmn2stamp.converter.persistance.RdfRepositoryWriter;
import cz.cvut.kbss.bpmn2stamp.converter.service.Bbo;
import cz.cvut.kbss.bpmn2stamp.converter.service.ConverterMappingService;

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

	public Bpmn2StampConverterService() {
	}

	public void init(String baseBpmnAsBboOntologyIri, String baseOrganizationStructureAsBboOntologyIri, String baseStampOntologyIri) {
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

	public void convertToStampFromBbo(File bboFile, File outputFile) {
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
		doConversionWithUsingReasoner(bpmnFile, orgFile, actorMappingFiles, outputFile);
	}

	public void convertToStampAndBbo(File bpmnFile, File orgFile, List<File> actorMappingFiles, File outputBboFile, File outputStampFile) {
		doConversionWithUsingReasoner(bpmnFile, orgFile, actorMappingFiles, outputBboFile, outputStampFile);
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


	private void doConversionWithUsingReasoner(File bpmnFile, File orgFile, List<File> actorMappingFile, File outputBboFile, File outputStampFile) {
		// creates file, so it can be read
		convertToBbo(bpmnFile, orgFile, actorMappingFile, outputBboFile);
		// read the file, so the reasoner is used
		convertToStampFromBbo(outputBboFile, outputStampFile);
	}

	private void doConversionWithUsingReasoner(File bpmnFile, File orgFile, List<File> actorMappingFile, File outputFile) {
		try {
			File bboFile = File.createTempFile("bbo-temp-", ".ttl");
//            File stampFile = addSuffix(outputFile, "-prestamp");
			doConversionWithUsingReasoner(bpmnFile, orgFile, actorMappingFile, bboFile, outputFile);
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

	private void saveToRdf(File targetFile, String targetBaseIri, Set<String> imports, Iterable<?> objectsToSave, Map<String, File> additionalImports) {
		RdfRepositoryWriter rdfRepositoryWriter = new RdfRepositoryWriter(
				targetFile.getAbsolutePath(),
				targetBaseIri,
				imports,
				additionalImports
		);
		rdfRepositoryWriter.write(objectsToSave);
	}

	private void saveToRdf(File targetFile, String targetBaseIri, Set<String> imports, Iterable<?> objectsToSave) {
		saveToRdf(targetFile, targetBaseIri, imports, objectsToSave, Map.of());
	}
}
