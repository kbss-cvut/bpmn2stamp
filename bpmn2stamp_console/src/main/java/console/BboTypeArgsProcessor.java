package console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BboTypeArgsProcessor {
	private final Options options = new Options();
	// TODO this property is present in multiple places - rework somehow
	private final Option inputBaseIriOpt = Option.builder().hasArg().option("iri").longOpt("baseIri").required()
			.desc("base iri for the output ontology. The prefixes will be added to the result files automatically.").build();
	private final Option outputFileNameOpt = Option.builder().hasArg().option("out").required().longOpt("output-file")
			.desc("output file").build();
	private final Option inputBpmnFileNameOpt = Option.builder().hasArg().option("ibpmn").longOpt("input-bpmn-file").required()
			.desc("input *.bpmn file for the converter, containing process diagram.").build();
	private final Option inputOrgFileNameOpt = Option.builder().hasArg().option("iorg").longOpt("input-org-structure-file").required()
			.desc("input *.xml file for the converter, containing organization structure definition").build();
	private final Option inputActorMappingFileNameOpt = Option.builder().hasArg().option("iam").longOpt("input-actor-mapping-file").required()
			.desc("input *.xml files for the converter, containing actor mapping definitions for every process in the provided bpmn diagram").build();

	public BboTypeArgsProcessor() {
		this.options.addOption(inputBaseIriOpt);
		// input options
		this.options.addOption(inputBpmnFileNameOpt);
		this.options.addOption(inputOrgFileNameOpt);
		this.options.addOption(inputActorMappingFileNameOpt);
		// output options
		this.options.addOption(outputFileNameOpt);
	}

	public ParsingResult processArgs(String[] args) throws ParseException {
		CommandLine cmd = CommandLineUtils.parseArgs(options, args);

		String baseIri = cmd.getOptionValue(inputBaseIriOpt);
		String bpmnFileArg = cmd.getOptionValue(inputBpmnFileNameOpt);
		String orgFileArg = cmd.getOptionValue(inputOrgFileNameOpt);
		String outputBboFile = cmd.getOptionValue(outputFileNameOpt);
		List<File> actorMappingFilesList = Arrays.stream(cmd.getOptionValues(inputActorMappingFileNameOpt))
				.map(File::new)
				.collect(Collectors.toList());

		return new ParsingResult(
				baseIri, new File(bpmnFileArg),
				new File(orgFileArg),
				actorMappingFilesList,
				new File(outputBboFile));
	}

	public Options getOptions() {
		return options;
	}

	public static class ParsingResult {
		private final String baseIri;
		private final File inputBpmnFile;
		private final File inputOrgFile;
		private final List<File> inputActorMappingFile;
		private final File outputBboFile;

		public ParsingResult(String baseIri, File inputBpmnFile, File inputOrgFile, List<File> inputActorMappingFile, File outputBboFile) {
			this.baseIri = baseIri;
			this.inputBpmnFile = inputBpmnFile;
			this.inputOrgFile = inputOrgFile;
			this.inputActorMappingFile = inputActorMappingFile;
			this.outputBboFile = outputBboFile;
		}

		public File getInputBpmnFile() {
			return inputBpmnFile;
		}

		public File getInputOrgFile() {
			return inputOrgFile;
		}

		public List<File> getInputActorMappingFile() {
			return inputActorMappingFile;
		}

		public File getOutputBboFile() {
			return outputBboFile;
		}

		@Override
		public String toString() {
			return "ParsingResult{" +
					"inputBpmnFile=" + inputBpmnFile +
					", inputOrgFile=" + inputOrgFile +
					", inputActorMappingFile=" + inputActorMappingFile +
					", outputBboFile=" + outputBboFile +
					'}';
		}

		public String getBaseIri() {
			return baseIri;
		}
	}
}