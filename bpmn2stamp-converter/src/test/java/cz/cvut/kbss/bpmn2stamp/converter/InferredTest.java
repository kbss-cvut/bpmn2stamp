package cz.cvut.kbss.bpmn2stamp.converter;

import com.google.common.collect.Sets;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.FlowElement;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.FlowElementsContainer;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.StartEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.bpmn2stamp.converter.persistance.BboRdfRepositoryReader;
import cz.cvut.kbss.bpmn2stamp.converter.persistance.RdfRepositoryWriter;
import cz.cvut.kbss.jopa.model.annotations.Inferred;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InferredTest {
	
	private static final String TEST_IRI = "http://test-iri.cz";
	
	public void verifyPreconditions(FlowElementsContainer elementContainer, Collection<FlowElement> flowElements) throws NoSuchFieldException {
		// container has elements
		Assertions.assertThat(elementContainer.getHas_flowElements()).isNotEmpty();
		// all elements do not have explicitly defined container
		for (FlowElement flowElement : elementContainer.getHas_flowElements()) {
			Assertions.assertThat(flowElement.getHas_container()).isNullOrEmpty();
		}
		// field with restriction has @Inferred annotation
		Assertions.assertThat(FlowElement.class).hasDeclaredFields("has_container");
		Assertions.assertThat(
				FlowElement.class.getDeclaredField("has_container").getDeclaredAnnotation(Inferred.class)
		).isNotNull();
	}
	
	@Test
	public void test() throws OWLOntologyStorageException, IOException, OWLOntologyCreationException, NoSuchFieldException {
		List<FlowElement> elements = List.of(
				new StartEvent()
		);
		FlowElementsContainer container = createElementContainer(elements);

		verifyPreconditions(container, elements);

		// write ontology without errors
		File tempFile = createTempFile("test-inferred");
		RdfRepositoryWriter ontoWriter = new RdfRepositoryWriter(
				tempFile.getAbsolutePath(),
				TEST_IRI,
				Sets.newHashSet(Vocabulary.ONTOLOGY_IRI_bbo_extension)
		);
		ontoWriter.write(Collections.singleton(container));

		// read ontology without errors
		BboRdfRepositoryReader bboReader = new BboRdfRepositoryReader(
				tempFile.getAbsolutePath(),
				TEST_IRI
		);
		List<Thing> things = bboReader.readAll();
		System.out.println(things);
	}
	
	private File createTempFile(String name) throws IOException {
		return Files.createTempFile(name, ".ttl").toFile();
	}
	
	private FlowElementsContainer createElementContainer(List<FlowElement> elements) {
		FlowElementsContainer flowElementsContainer = new FlowElementsContainer();
		Set<FlowElement> containerElements = new HashSet<>(elements);
		flowElementsContainer.setHas_flowElements(containerElements);
		return flowElementsContainer;
	}
	
}
