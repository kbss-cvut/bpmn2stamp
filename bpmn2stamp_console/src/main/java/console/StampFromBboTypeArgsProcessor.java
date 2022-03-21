package console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;

public class StampFromBboTypeArgsProcessor {
	private final Options options = new Options();
	private final Option outputFileNameOpt = Option.builder().hasArg().option("out").longOpt("output-file").required()
			.desc("output file").build();
	private final Option inputBboFileNameOpt = Option.builder().hasArg().option("ibbo").longOpt("input-bbo-file").required()
			.desc("input *.ttl file for the converter, containing individuals of the BBO ontology (typically having the bpmn and organization structure data).").build();

	public StampFromBboTypeArgsProcessor() {
		// input options
		this.options.addOption(inputBboFileNameOpt);
		// output options
		this.options.addOption(outputFileNameOpt);
	}

	public ParsingResult processArgs(String[] args) throws ParseException {
		CommandLine cmd = CommandLineUtils.parseArgs(options, args);

		String bboFileArg = cmd.getOptionValue(inputBboFileNameOpt);
		String outputStampFile = cmd.getOptionValue(outputFileNameOpt);

		return new ParsingResult(
				new File(bboFileArg),
				new File(outputStampFile));
	}

	public Options getOptions() {
		return options;
	}

	public static class ParsingResult {
		private final File inputBboFile;
		private final File outputStampFile;

		public ParsingResult(File inputBboFile, File outputStampFile) {
			this.inputBboFile = inputBboFile;
			this.outputStampFile = outputStampFile;
		}

		public File getInputBboFile() {
			return inputBboFile;
		}

		public File getOutputStampFile() {
			return outputStampFile;
		}
	}
}