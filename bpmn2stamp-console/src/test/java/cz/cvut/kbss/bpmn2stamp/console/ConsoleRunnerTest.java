package cz.cvut.kbss.bpmn2stamp.console;

import org.apache.commons.cli.ParseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Matchers.*;

public class ConsoleRunnerTest {

	private Configuration config;
	private NoneTypeArgsProcessor noneTypeArgsProcessor;
	private StampTypeArgsProcessor stampTypeArgsProcessor;
	private BboTypeArgsProcessor bboTypeArgsProcessor;
	private StampFromBboTypeArgsProcessor stampFromBboTypeArgsProcessor;
	private Bpmn2StampConverterService converterService;
	
	@BeforeEach
	public void setUp() throws IOException {
		this.config = Configuration.getInstance();
//		this.noneTypeArgsProcessor = Mockito.mock(NoneTypeArgsProcessor.class);
		this.noneTypeArgsProcessor = new NoneTypeArgsProcessor();
		this.stampTypeArgsProcessor = Mockito.mock(StampTypeArgsProcessor.class);
		this.bboTypeArgsProcessor = Mockito.mock(BboTypeArgsProcessor.class);
		this.stampFromBboTypeArgsProcessor = Mockito.mock(StampFromBboTypeArgsProcessor.class);
		this.converterService = Mockito.mock(Bpmn2StampConverterService.class);
	}

	@Test
	public void mainTest() throws ParseException {
		ConsoleRunner app = new ConsoleRunner(
				config,
				noneTypeArgsProcessor,
				stampTypeArgsProcessor,
				bboTypeArgsProcessor,
				stampFromBboTypeArgsProcessor,
				converterService
		);

//		Mockito.doCallRealMethod().when(noneTypeArgsProcessor).processArgs(anyVararg());
		Mockito.doNothing().when(converterService).init(anyString(), anyString(), anyString());
//		--output-bbo-file dozor-nad-provozovateli-letist-bpmn.ttl
//		--output-stamp-file dozor-nad-provozovateli-letist-pre-stamp.ttl
		app.run(
				new String[]{
//						"-t", "stamp",
						"--baseIri", "testBaseIri",
//						"-ibpmn", "testBpmnInput",
						"--input-org-structure-file", "testOrgInput",
						"--input-actor-mapping-file", "testActorInput", 
						"-out", "testOutput"}
		);
//		Mockito.verify(converterService).init("testBaseIri",
//				"",
//				"");

//		converterService.convertToBbo(
//				res.getInputBpmnFile(),
//				res.getInputOrgFile(),
//				res.getInputActorMappingFile(),
//				res.getOutputBboFile()
//		);
	}

	private static String appendSuffix(String base, String suffix) {
		String pre = base;
		if (base.endsWith("/"))
			pre = base.substring(0, base.length() - 1);
		return pre + suffix;
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