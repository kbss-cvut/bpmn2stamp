package console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NoneTypeArgsProcessor {
	private final Options options = new Options();
	// TODO this property is present in multiple places - rework somehow
	private final Option inputBaseIriOpt = Option.builder().hasArg().option("iri").longOpt("baseIri").required()
			.desc("base iri for the output ontology. The prefixes will be added to the result files automatically.").build();
	private final Option outputBboFileNameOpt = Option.builder().hasArg().option("obbo").longOpt("output-bbo-file")
			.desc("output bbo file").build();
	private final Option outputStampFileNameOpt = Option.builder().hasArg().option("ostamp").longOpt("output-stamp-file")
			.desc("output stamp file").build();
	private final Option outputFileNameOpt = Option.builder().hasArg().option("out").longOpt("output-file")
			.desc("output file. This argument can be used as an alternative to all other output arguments. For every output file an appropriate suffix will be added.").build();
	private final Option inputBpmnFileNameOpt = Option.builder().hasArg().option("ibpmn").longOpt("input-bpmn-file").required()
			.desc("input *.bpmn file for the converter, containing process diagram.").build();
	private final Option inputOrgFileNameOpt = Option.builder().hasArg().option("iorg").longOpt("input-org-structure-file").required()
			.desc("input *.xml file for the converter, containing organization structure definition").build();
	private final Option inputActorMappingFileNameOpt = Option.builder().hasArg().option("iam").longOpt("input-actor-mapping-file").required()
			.desc("input *.xml files for the converter, containing actor mapping definitions for every process in the provided bpmn diagram").build();

	public NoneTypeArgsProcessor() {
		this.options.addOption(inputBaseIriOpt);
		// input options
		this.options.addOption(inputBpmnFileNameOpt);
		this.options.addOption(inputOrgFileNameOpt);
		this.options.addOption(inputActorMappingFileNameOpt);
		// output options
		this.options.addOption(outputBboFileNameOpt);
		this.options.addOption(outputStampFileNameOpt);
		this.options.addOption(outputFileNameOpt);
	}

	public ParsingResult processArgs(String[] args) throws ParseException {
		CommandLine cmd = CommandLineUtils.parseArgs(options, args);

		String outputErrorMessage = String.format(
				"Both arguments -%s(--%s) and -%s(--%s) required. Alternatively use argument -%s(--%s)",
				outputBboFileNameOpt.getOpt(), outputBboFileNameOpt.getLongOpt(),
				outputStampFileNameOpt.getOpt(), outputStampFileNameOpt.getLongOpt(),
				outputFileNameOpt.getOpt(), outputFileNameOpt.getLongOpt());

		String outputBboFile;
		String outputStampFile;
		if (cmd.hasOption(outputBboFileNameOpt) || cmd.hasOption(outputStampFileNameOpt)) {
			if (!cmd.hasOption(outputBboFileNameOpt) || !cmd.hasOption(outputStampFileNameOpt)) {
				// error : both required
				throw new ParseException(outputErrorMessage);
			} else {
				// OK both
				outputBboFile = cmd.getOptionValue(outputBboFileNameOpt);
				outputStampFile = cmd.getOptionValue(outputStampFileNameOpt);
			}
		} else if (!cmd.hasOption(outputFileNameOpt)) {
			// error : one or both required
			throw new ParseException(outputErrorMessage);
		} else {
			// OK one
			String baseName = cmd.getOptionValue(outputFileNameOpt);
			outputBboFile = baseName + "-bbo.ttl";
			outputStampFile = baseName + "-prestamp.ttl";
		}
		String baseIri = cmd.getOptionValue(inputBaseIriOpt);
		String bpmnFileArg = cmd.getOptionValue(inputBpmnFileNameOpt);
		String orgFileArg = cmd.getOptionValue(inputOrgFileNameOpt);
		List<File> actorMappingFilesList = Arrays.stream(cmd.getOptionValues(inputActorMappingFileNameOpt))
				.map(File::new)
				.collect(Collectors.toList());

		return new ParsingResult(
				baseIri, new File(bpmnFileArg),
				new File(orgFileArg),
				actorMappingFilesList,
				new File(outputBboFile),
				new File(outputStampFile));
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
		private final File outputStampFile;

		public ParsingResult(String baseIri, File inputBpmnFile, File inputOrgFile, List<File> inputActorMappingFile, File outputBboFile, File outputStampFile) {
			this.baseIri = baseIri;
			this.inputBpmnFile = inputBpmnFile;
			this.inputOrgFile = inputOrgFile;
			this.inputActorMappingFile = inputActorMappingFile;
			this.outputBboFile = outputBboFile;
			this.outputStampFile = outputStampFile;
		}

		public String getBaseIri() {
			return baseIri;
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

		public File getOutputStampFile() {
			return outputStampFile;
		}

		@Override
		public String toString() {
			return "ParsingResult{" +
					"baseIri=" + baseIri +
					"inputBpmnFile=" + inputBpmnFile +
					", inputOrgFile=" + inputOrgFile +
					", inputActorMappingFile=" + inputActorMappingFile +
					", outputBboFile=" + outputBboFile +
					", outputStampFile=" + outputStampFile +
					'}';
		}
	}
}