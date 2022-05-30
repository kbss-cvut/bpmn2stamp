package cz.cvut.kbss.bpmn2stamp.console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cz.cvut.kbss.bpmn2stamp.console.CommandLineUtils.*;


public class NoneTypeArgsProcessor implements ArgsProcessor<NoneTypeArgsProcessor.ParsingResult> {
	private final Options options = new Options();
	// TODO this property is present in multiple places - rework somehow
	private final Option inputBaseIriOpt = Option.builder().hasArg().option("iri").longOpt("baseIri").required()
			.argName("BASE_IRI").desc("base iri for the output ontology. The prefixes will be added to the result files automatically.").build();
	private final Option outputBboFileNameOpt = Option.builder().hasArg().option("obbo").longOpt("output-bbo-file")
			.argName("OUTPUT_BBO_FILE").desc("output bbo file").build();
	private final Option outputStampFileNameOpt = Option.builder().hasArg().option("ostamp").longOpt("output-stamp-file")
			.argName("OUTPUT_STAMP_FILE").desc("output stamp file").build();
	private final Option outputFilesPrefixOpt = Option.builder().hasArg().option("oprefix").longOpt("output-files-prefix")
			.argName("OUTPUT_FILES_PREFIX").desc("prefix for all output files. This argument can be used as an alternative to all other output arguments. Name for every output file will be constructed automatically.").build();
	private final Option inputBpmnFileNameOpt = Option.builder().hasArg().option("ibpmn").longOpt("input-bpmn-file").required()
			.argName("BPMN_FILE").desc("input *.bpmn file for the converter, containing process diagram.").build();
	private final Option inputOrgFileNameOpt = Option.builder().hasArg().option("iorg").longOpt("input-org-structure-file").required()
			.argName("ORG_STRUCTURE_FILE").desc("input *.xml file for the converter, containing organization structure definition").build();
	private final Option inputActorMappingFileNameOpt = Option.builder().hasArg().option("iam").longOpt("input-actor-mapping-file").required()
			.argName("ACTOR_MAPPING_FILE").desc("input *.xml files for the converter, containing actor mapping definitions for every process in the provided bpmn diagram").build();

	public NoneTypeArgsProcessor() {
		this.options.addOption(inputBaseIriOpt);
		// input options
		this.options.addOption(inputBpmnFileNameOpt);
		this.options.addOption(inputOrgFileNameOpt);
		this.options.addOption(inputActorMappingFileNameOpt);
		// output options
		this.options.addOption(outputBboFileNameOpt);
		this.options.addOption(outputStampFileNameOpt);
		this.options.addOption(outputFilesPrefixOpt);
	}

	@Override
	public ParsingResult processArgs(String[] args) throws ParseException {
		CommandLine cmd = parseArgs(options, args);

		String outputErrorMessage = String.format(
				"Both arguments -%s(--%s) and -%s(--%s) required. Alternatively use argument -%s(--%s)",
				outputBboFileNameOpt.getOpt(), outputBboFileNameOpt.getLongOpt(),
				outputStampFileNameOpt.getOpt(), outputStampFileNameOpt.getLongOpt(),
				outputFilesPrefixOpt.getOpt(), outputFilesPrefixOpt.getLongOpt());

		String outputBboFile = null;
		String outputStampFile = null;
		if (cmd.hasOption(outputFilesPrefixOpt)) {
			// resolve by prefix
			String baseName = cmd.getOptionValue(outputFilesPrefixOpt);
			outputBboFile = baseName + "-bpmn.ttl";
			outputStampFile = baseName + "-pre-stamp.ttl";
		}
		if (cmd.hasOption(outputBboFileNameOpt)) {
			// resolve by specific bbo argument
			outputBboFile = cmd.getOptionValue(outputBboFileNameOpt);
		}
		if (cmd.hasOption(outputStampFileNameOpt)) {
			// resolve by specific stamp argument
			outputStampFile = cmd.getOptionValue(outputStampFileNameOpt);
		}
		if (outputBboFile == null || outputStampFile == null) {
			// error : some argument is missing
			throw new ParseException(outputErrorMessage);
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

	@Override
	public Options getOptions() {
		return options;
	}

	@Override
	public String getSynopsis() {
		return "\tbpmn2stamp " + optSynopsisString(inputBaseIriOpt) +
				" " + optSynopsisString(inputBpmnFileNameOpt) +
				" " + optSynopsisString(inputActorMappingFileNameOpt, true) +
				" " + optSynopsisString(inputOrgFileNameOpt) +
				" " + optSynopsisString(outputBboFileNameOpt) +
				" " + optSynopsisString(outputStampFileNameOpt);
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