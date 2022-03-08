package console;

import org.apache.commons.cli.ParseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConsoleRunnerTest {


	// TODO use real files or use mockito
	@Test
	public void mainTest() {
		Assertions.assertThatThrownBy(() -> {
			ConsoleRunner.main(new String[]{"-t", "stamp", "-iri", "testBaseIri", "-ibpmn", "testBpmnInput", "-iorg", "testOrgInput", "-iam", "testActorInput", "-out", "testOutput"});
		}).isNotInstanceOf(ParseException.class);

		Assertions.assertThatThrownBy(() -> {
			ConsoleRunner.main(new String[]{"-t", "bbo", "-iri", "testBaseIri", "-ibpmn", "testBpmnInput", "-iorg", "testOrgInput", "-iam", "testActorInput", "-out", "testOutput"});
		}).isNotInstanceOf(ParseException.class);

		Assertions.assertThatThrownBy(() -> {
			ConsoleRunner.main(new String[]{"-t", "stampFromBbo", "-iri", "testBaseIri", "-ibbo", "testBboInput", "-out", "testOutput"});
		}).isNotInstanceOf(ParseException.class);

		Assertions.assertThatThrownBy(() -> {
			ConsoleRunner.main(new String[]{"-iri", "testBaseIri", "-ibpmn", "testBpmnInput", "-iorg", "testOrgInput", "-iam", "testActorInput", "-out", "testOutput"});
		}).isNotInstanceOf(ParseException.class);
	}

	static class ConverterTypeTest {

		@Test
		public void resolveTest() {
			Assertions.assertThat(
					ConsoleRunner.ConverterType.resolve(null)
			).isEqualTo(ConsoleRunner.ConverterType.NONE);

			Assertions.assertThat(
					ConsoleRunner.ConverterType.resolve("unknown")
			).isNull();

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