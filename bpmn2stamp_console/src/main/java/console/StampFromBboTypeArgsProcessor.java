package console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;

import static console.CommandLineUtils.optSynopsisString;

public class StampFromBboTypeArgsProcessor implements ArgsProcessor<StampFromBboTypeArgsProcessor.ParsingResult> {
	private final Options options = new Options();
	// TODO this property is present in multiple places - rework somehow
	private final Option inputBaseIriOpt = Option.builder().hasArg().option("iri").longOpt("baseIri").required()
			.argName("BASE_IRI").desc("base iri for the output ontology. The prefixes will be added to the result files automatically.").build();
	private final Option outputFileNameOpt = Option.builder().hasArg().option("out").longOpt("output-file").required()
			.argName("OUTPUT_STAMP_FILE").desc("output file").build();
	private final Option inputBboFileNameOpt = Option.builder().hasArg().option("ibbo").longOpt("input-bbo-file").required()
			.argName("BBO_FILE").desc("input *.ttl file for the converter, containing individuals of the BBO ontology (typically having the bpmn and organization structure data).").build();

	public StampFromBboTypeArgsProcessor() {
		this.options.addOption(inputBaseIriOpt);
		// input options
		this.options.addOption(inputBboFileNameOpt);
		// output options
		this.options.addOption(outputFileNameOpt);
	}

	@Override
	public ParsingResult processArgs(String[] args) throws ParseException {
		CommandLine cmd = CommandLineUtils.parseArgs(options, args);

		String baseIri = cmd.getOptionValue(inputBaseIriOpt);
		String bboFileArg = cmd.getOptionValue(inputBboFileNameOpt);
		String outputStampFile = cmd.getOptionValue(outputFileNameOpt);

		return new ParsingResult(
				baseIri, new File(bboFileArg),
				new File(outputStampFile));
	}

	@Override
	public Options getOptions() {
		return options;
	}

	@Override
	public String getSynopsis() {
		return "\tbpmn2stamp -t stampFromBbo " + optSynopsisString(inputBaseIriOpt) +
				" " + optSynopsisString(inputBboFileNameOpt) +
				" " + optSynopsisString(outputFileNameOpt);
	}

	public static class ParsingResult {
		private final String baseIri;
		private final File inputBboFile;
		private final File outputStampFile;

		public ParsingResult(String baseIri, File inputBboFile, File outputStampFile) {
			this.baseIri = baseIri;
			this.inputBboFile = inputBboFile;
			this.outputStampFile = outputStampFile;
		}

		public File getInputBboFile() {
			return inputBboFile;
		}

		public File getOutputStampFile() {
			return outputStampFile;
		}

		public String getBaseIri() {
			return baseIri;
		}
	}
}