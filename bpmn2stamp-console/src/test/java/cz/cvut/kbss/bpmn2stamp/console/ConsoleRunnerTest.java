package cz.cvut.kbss.bpmn2stamp.console;

import org.apache.commons.cli.ParseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

public class ConsoleRunnerTest {

	private Configuration config;
	private NoneTypeArgsProcessor noneTypeArgsProcessor;
	private StampTypeArgsProcessor stampTypeArgsProcessor;
	private BboTypeArgsProcessor bboTypeArgsProcessor;
	private StampFromBboTypeArgsProcessor stampFromBboTypeArgsProcessor;
	private Bpmn2StampConverterService converterService;
	private ConsoleRunner app;

	@BeforeEach
	public void setUp() throws IOException {
		this.config = Configuration.getInstance();
		this.noneTypeArgsProcessor = new NoneTypeArgsProcessor();
		this.stampTypeArgsProcessor = new StampTypeArgsProcessor();
		this.bboTypeArgsProcessor = new BboTypeArgsProcessor();
		this.stampFromBboTypeArgsProcessor = new StampFromBboTypeArgsProcessor();
		this.converterService = Mockito.mock(Bpmn2StampConverterService.class);
		this.app = new ConsoleRunner(
				config,
				noneTypeArgsProcessor,
				stampTypeArgsProcessor,
				bboTypeArgsProcessor,
				stampFromBboTypeArgsProcessor,
				converterService
		);
	}

	@Test
	public void mainTest_noneConversionType_withCorrectArguments_shouldCallConversionServiceCorrectly() {
		// mock behaviours to test
		Mockito.doNothing().when(converterService).init(anyString(), anyString(), anyString());
		Mockito.doNothing().when(converterService).convertToStampAndBbo(any(File.class), any(File.class), anyList(), any(File.class), any(File.class));

		// tun method to test
		app.run(
				new String[]{
						"--baseIri", "testBaseIri",
						"--input-bpmn-file", "testBpmnInput",
						"--input-org-structure-file", "testOrgInput",
						"--input-actor-mapping-file", "testActorInput",
						"-output-files-prefix", "testOutputFilePrefix"}
		);

		// verify order and arguments
		InOrder order = Mockito.inOrder(converterService);
		order.verify(converterService).init("testBaseIri-bpmn",
				"testBaseIri-organization-structure",
				"testBaseIri-pre-stamp");
		order.verify(converterService).convertToStampAndBbo(
				new File("testBpmnInput"),
				new File("testOrgInput"),
				List.of(new File("testActorInput")),
				new File("testOutputFilePrefix-bbo.ttl"),
				new File("testOutputFilePrefix-pre-stamp.ttl")
		);
	}

	@Test
	public void mainTest_bboConversionType_withCorrectArguments_shouldCallConversionServiceCorrectly() {
		// mock behaviours to test
		Mockito.doNothing().when(converterService).init(anyString(), anyString(), anyString());
		Mockito.doNothing().when(converterService).convertToBbo(any(File.class), any(File.class), anyList(), any(File.class));

		// tun method to test
		app.run(new String[]{
				"--type", "bbo",
				"--baseIri", "testBaseIri",
				"--input-bpmn-file", "testBpmnInput",
				"--input-org-structure-file", "testOrgInput",
				"--input-actor-mapping-file", "testActorInput",
				"--out", "testOutputFile.ttl"}
		);

		// verify order and arguments
		InOrder order = Mockito.inOrder(converterService);
		order.verify(converterService).init("testBaseIri-bpmn",
				"testBaseIri-organization-structure",
				"testBaseIri-pre-stamp");
		order.verify(converterService).convertToBbo(
				new File("testBpmnInput"),
				new File("testOrgInput"),
				List.of(new File("testActorInput")),
				new File("testOutputFile.ttl")
		);
	}

	@Test
	public void mainTest_stampConversionType_withCorrectArguments_shouldCallConversionServiceCorrectly() {
		// mock behaviours to test
		Mockito.doNothing().when(converterService).init(anyString(), anyString(), anyString());
		Mockito.doNothing().when(converterService).convertToStamp(any(File.class), any(File.class), anyList(), any(File.class));

		// tun method to test
		app.run(new String[]{
				"--type", "stamp",
				"--baseIri", "testBaseIri",
				"--input-bpmn-file", "testBpmnInput",
				"--input-org-structure-file", "testOrgInput",
				"--input-actor-mapping-file", "testActorInput",
				"--out", "testOutputFile.ttl"}
		);

		// verify order and arguments
		InOrder order = Mockito.inOrder(converterService);
		order.verify(converterService).init("testBaseIri-bpmn",
				"testBaseIri-organization-structure",
				"testBaseIri-pre-stamp");
		order.verify(converterService).convertToStamp(
				new File("testBpmnInput"),
				new File("testOrgInput"),
				List.of(new File("testActorInput")),
				new File("testOutputFile.ttl")
		);
	}

	@Test
	public void mainTest_stampFromBboConversionType_withCorrectArguments_shouldCallConversionServiceCorrectly() {
		// mock behaviours to test
		Mockito.doNothing().when(converterService).init(anyString(), anyString(), anyString());
		Mockito.doNothing().when(converterService).convertToStampFromBbo(any(File.class), any(File.class));

		// tun method to test
		app.run(new String[]{
				"--type", "stampFromBbo",
				"--baseIri", "testBaseIri",
				"--input-bbo-file", "testBboInput",
				"--out", "testOutputFile.ttl"}
		);

		// verify order and arguments
		InOrder order = Mockito.inOrder(converterService);
		order.verify(converterService).init("testBaseIri-bpmn",
				"testBaseIri-organization-structure",
				"testBaseIri-pre-stamp");
		order.verify(converterService).convertToStampFromBbo(
				new File("testBboInput"),
				new File("testOutputFile.ttl")
		);
	}

	static class ConverterTypeTest {

		@Test
		public void resolveTest() throws ParseException {
			Assertions.assertThat(
					ConsoleRunner.ConverterType.resolve(null)
			).isEqualTo(ConsoleRunner.ConverterType.NONE);

			Assertions.assertThatThrownBy(() ->
					ConsoleRunner.ConverterType.resolve("unknown")
			).isInstanceOf(ParseException.class);

			Assertions.assertThat(
					ConsoleRunner.ConverterType.resolve("bbo")
			).isEqualTo(ConsoleRunner.ConverterType.BBO);

			Assertions.assertThat(
					ConsoleRunner.ConverterType.resolve("stamp")
			).isEqualTo(ConsoleRunner.ConverterType.STAMP);

			Assertions.assertThat(
					ConsoleRunner.ConverterType.resolve("stampFromBbo")
			).isEqualTo(ConsoleRunner.ConverterType.STAMP_FROM_BBO);
		}
	}
}