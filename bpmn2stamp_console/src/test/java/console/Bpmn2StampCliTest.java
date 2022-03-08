package console;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Bpmn2StampCliTest {

	// TODO split testing methods

	@Test
	public void parseTypeArguments() throws ParseException {
		assertThat(
				Bpmn2StampCli.parseTypeArguments(new String[]{"-t", "stampFromBbo"})
		).isEqualTo(ConsoleRunner.ConverterType.STAMP_FROM_BBO);

		assertThat(
				Bpmn2StampCli.parseTypeArguments(new String[]{})
		).isEqualTo(ConsoleRunner.ConverterType.NONE);
	}

	@Test
	public void parseBaseIriArguments() throws ParseException {
		assertThat(
				Bpmn2StampCli.parseBaseIriArguments(new String[]{"-iri", "baseIriTest"})
		).isEqualTo("baseIriTest");

//		assertThatThrownBy(() ->
//				Bpmn2StampCli.parseBaseIriArguments(new String[]{})
//		).isInstanceOf(MissingOptionException.class);
	}

	@Test
	public void testParseOutputFileArguments() throws ParseException {
		Bpmn2StampCli.OutputArguments expected = new Bpmn2StampCli.OutputArguments(
				"test/defaultPath-out.ttl",
				"test/defaultPath-bbo.ttl",
				"test/defaultPath-prestamp.ttl"
		);
		assertThat(
				Bpmn2StampCli.parseOutputFileArguments(new String[]{}, ConsoleRunner.ConverterType.BBO, "test/defaultPath.ttl")
		).usingRecursiveComparison().isEqualTo(expected);

		Bpmn2StampCli.OutputArguments expected2 = new Bpmn2StampCli.OutputArguments(
				"test/output.ttl",
				"test/defaultPath-bbo.ttl",
				"test/defaultPath-prestamp.ttl"
		);
		assertThat(
				Bpmn2StampCli.parseOutputFileArguments(new String[]{"-out", "test/output.ttl"}, ConsoleRunner.ConverterType.BBO, "test/defaultPath.ttl")
		).usingRecursiveComparison().isEqualTo(expected2);

		Bpmn2StampCli.OutputArguments expected3 = new Bpmn2StampCli.OutputArguments(
				"test/defaultPath-out.ttl",
				"test/output-bbo.ttl",
				"test/output-prestamp.ttl"
		);
		assertThat(
				Bpmn2StampCli.parseOutputFileArguments(new String[]{"-obbo", "test/output-bbo.ttl", "-ostamp", "test/output-prestamp.ttl"}, ConsoleRunner.ConverterType.NONE, "test/defaultPath.ttl")
		).usingRecursiveComparison().isEqualTo(expected3);
	}

	@Test
	public void parseBboArguments() throws ParseException {
		Bpmn2StampCli.StampFromBboArguments expected = new Bpmn2StampCli.StampFromBboArguments(
				"testBboInput");
		Bpmn2StampCli.StampFromBboArguments actual = Bpmn2StampCli.parseBboArguments(new String[]{"-ibbo", "testBboInput", "-out", "testOutput"});
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

		Bpmn2StampCli.StampFromBboArguments expected2 = new Bpmn2StampCli.StampFromBboArguments(
				"testBboInput");
		Bpmn2StampCli.StampFromBboArguments actual2 = Bpmn2StampCli.parseBboArguments(new String[]{"-ibbo", "testBboInput"});
		assertThat(actual2).usingRecursiveComparison().isEqualTo(expected2);

		assertThatThrownBy(() ->
				Bpmn2StampCli.parseBboArguments(new String[]{"-out", "testOutput"})
		).isInstanceOf(MissingOptionException.class);
	}

	@Test
	public void parseBpmnOrgAmArguments() throws ParseException {
		Bpmn2StampCli.BpmnOrgAmArguments expected = new Bpmn2StampCli.BpmnOrgAmArguments(
				"testBpmnInput",
				"testOrgInput",
				List.of(new File("testActorInput")));
		Bpmn2StampCli.BpmnOrgAmArguments actual = Bpmn2StampCli.parseBpmnOrgAmArguments(new String[]{"-ibpmn", "testBpmnInput", "-iorg", "testOrgInput", "-iam", "testActorInput", "-out", "testOutput"});
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

		assertThatThrownBy(() -> {
			Bpmn2StampCli.parseBpmnOrgAmArguments(new String[]{"-t", "stampFromBbo", "-iorg", "testOrgInput", "-iam", "testActorInput", "-out", "testOutput"});
		}).isInstanceOf(MissingOptionException.class);

		assertThatThrownBy(() -> {
			Bpmn2StampCli.parseBpmnOrgAmArguments(new String[]{"-t", "stampFromBbo", "-ibpmn", "testBpmnInput", "-iam", "testActorInput", "-out", "testOutput"});
		}).isInstanceOf(MissingOptionException.class);
	}
}