package console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Bpmn2StampCli {

	private static final String ARGS_OUTPUT_STAMP_FILE_FULL = "output-stamp-file";
	private static final String ARGS_OUTPUT_STAMP_FILE = "ostamp";
	private static final String ARGS_OUTPUT_BBO_FILE_FULL = "output-bbo-file";
	private static final String ARGS_OUTPUT_BBO_FILE = "obbo";
	private static final String ARGS_OUTPUT_FILE_FULL = "output-file";
	private static final String ARGS_OUTPUT_FILE = "out";

	private static final String ARGS_INPUT_TYPE_FULL = "type";
	private static final String ARGS_INPUT_TYPE = "t";
	private static final String ARGS_INPUT_BBO_FULL = "input-bbo-file";
	private static final String ARGS_INPUT_BBO = "ibbo";
	private static final String ARGS_INPUT_BASE_IRI_FULL = "baseIri";
	private static final String ARGS_INPUT_BASE_IRI = "iri";
	private static final String ARGS_INPUT_BPMN_FILE_FULL = "input-bpmn-file";
	private static final String ARGS_INPUT_BPMN_FILE = "ibpmn";
	private static final String ARGS_INPUT_ORG_FILE_FULL = "input-org-structure-file";
	private static final String ARGS_INPUT_ORG_FILE = "iorg";
	private static final String ARGS_INPUT_ACTOR_MAPPING_FILE_FULL = "input-actor-mapping-file";
	private static final String ARGS_INPUT_ACTOR_MAPPING_FILE = "iam";

	public static ConsoleRunner.ConverterType parseTypeArguments(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption(ARGS_INPUT_TYPE, ARGS_INPUT_TYPE_FULL, true,
				format("type of the conversion. Possible values are: %s", ConsoleRunner.ConverterType.strValues()));

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		try {
			CommandLine cmd = parser.parse(options, args, true);
			return ConsoleRunner.ConverterType.resolve(cmd.getOptionValue(ARGS_INPUT_TYPE));
		} catch (ParseException e) {
			formatter.printHelp("bpmn2stamp", options);
			throw e;
		}
	}

	public static String parseBaseIriArguments(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption(ARGS_INPUT_TYPE, ARGS_INPUT_TYPE_FULL, true,
				format("type of the conversion. Possible values are: %s", ConsoleRunner.ConverterType.strValues()));

		options.addOption(ARGS_INPUT_BASE_IRI, ARGS_INPUT_BASE_IRI_FULL, true,
				"base iri for the output ontology. The prefixes will be added to the result files automatically.");

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		try {
			CommandLine cmd = parser.parse(options, args, true);
			return cmd.getOptionValue(ARGS_INPUT_BASE_IRI);
		} catch (ParseException e) {
			formatter.printHelp("bpmn2stamp", options);
			throw e;
		}
	}

	@Deprecated
	public static StampFromBboArguments parseBboArguments(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption(ARGS_INPUT_TYPE, ARGS_INPUT_TYPE_FULL, true,
				format("type of the conversion. Possible values are: %s", ConsoleRunner.ConverterType.strValues()));

		options.addRequiredOption(ARGS_INPUT_BBO, ARGS_INPUT_BBO_FULL, true,
				"input *.ttl file for the converter, containing individuals of the BBO ontology (typically having the bpmn and organization structure data).");

		options.addOption(ARGS_OUTPUT_FILE, ARGS_OUTPUT_FILE_FULL, true,
				"output file");

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
			return new StampFromBboArguments(cmd);
		} catch (ParseException e) {
			formatter.printHelp("bpmn2stamp", options);
			throw e;
		}
	}

	public static OutputArguments parseOutputFileArguments(String[] args, ConsoleRunner.ConverterType type, String defaultValue) throws ParseException {
		Options options = new Options();

		// FIXME ugly c*ap because of the buggy, sh*tty CLI library...
		optType().getOptions().forEach(e -> {
			e.setRequired(false);
			options.addOption(e);
		});
		optsBaseIri().getOptions().forEach(e -> {
			e.setRequired(false);
			options.addOption(e);
		});
		optsBpmnOrgAm().getOptions().forEach(e -> {
			e.setRequired(false);
			options.addOption(e);
		});

		if (type == ConsoleRunner.ConverterType.NONE) {
			options.addOption(ARGS_OUTPUT_BBO_FILE, ARGS_OUTPUT_BBO_FILE_FULL, true,
					"output bbo file");
			options.addOption(ARGS_OUTPUT_STAMP_FILE, ARGS_OUTPUT_STAMP_FILE_FULL, true,
					"output stamp file");
		} else {
			options.addOption(ARGS_OUTPUT_FILE, ARGS_OUTPUT_FILE_FULL, true,
					"output file");
		}

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args, true);
			String fullPath = FilenameUtils.getFullPath(defaultValue);
			String baseName = FilenameUtils.getBaseName(defaultValue);
			String extension = ".ttl";
			return new OutputArguments(cmd, fullPath + baseName + "-out" + extension, fullPath + baseName + "-bbo" + extension, fullPath + baseName + "-prestamp" + extension);
		} catch (ParseException e) {
			formatter.printHelp("bpmn2stamp", options);
			throw e;
		}
	}

	@Deprecated
	public static BpmnOrgAmArguments parseBpmnOrgAmArguments(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption(ARGS_INPUT_TYPE, ARGS_INPUT_TYPE_FULL, true,
				format("type of the conversion. Possible values are: %s", ConsoleRunner.ConverterType.strValues()));
		options.addRequiredOption(ARGS_INPUT_BPMN_FILE, ARGS_INPUT_BPMN_FILE_FULL, true,
				"input *.bpmn file for the converter, containing process diagram.");
		options.addRequiredOption(ARGS_INPUT_ORG_FILE, ARGS_INPUT_ORG_FILE_FULL, true,
				"input *.xml file for the converter, containing organization structure definition");
		Option optionActorMapping = new Option(ARGS_INPUT_ACTOR_MAPPING_FILE, ARGS_INPUT_ACTOR_MAPPING_FILE_FULL, true,
				"input *.xml files for the converter, containing actor mapping definitions for every process in the provided bpmn diagram");
		optionActorMapping.setArgs(Option.UNLIMITED_VALUES);
		optionActorMapping.setRequired(true);
		options.addOption(optionActorMapping);

		options.addOption(ARGS_OUTPUT_FILE, ARGS_OUTPUT_FILE_FULL, true,
				"output files directory");

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args, true);
			return new BpmnOrgAmArguments(cmd);
		} catch (ParseException e) {
			formatter.printHelp("bpmn2stamp", options);
			throw e;
		}
	}

	public static Options optType() {
		Options options = new Options();

		options.addOption(ARGS_INPUT_TYPE, ARGS_INPUT_TYPE_FULL, true,
				format("type of the conversion. Possible values are: %s", ConsoleRunner.ConverterType.strValues()));

		return options;
	}

	public static Options optsBaseIri() {
		Options options = new Options();

		options.addRequiredOption(ARGS_INPUT_BASE_IRI, ARGS_INPUT_BASE_IRI_FULL, true,
				"base iri for the output ontology. The prefixes will be added to the result files automatically.");

		return options;
	}

	public static Options optsOutputFile(boolean extended) {
		Options options = new Options();

		if (extended) {
			options.addOption(ARGS_OUTPUT_BBO_FILE, ARGS_OUTPUT_BBO_FILE_FULL, true,
					"output bbo file");
			options.addOption(ARGS_OUTPUT_STAMP_FILE, ARGS_OUTPUT_STAMP_FILE_FULL, true,
					"output stamp file");
		} else {
			options.addOption(ARGS_OUTPUT_FILE, ARGS_OUTPUT_FILE_FULL, true,
					"output file");
		}

		return options;
	}

	public static Options optsBbo() {
		Options options = new Options();

		options.addRequiredOption(ARGS_INPUT_BBO, ARGS_INPUT_BBO_FULL, true,
				"input *.ttl file for the converter, containing individuals of the BBO ontology (typically having the bpmn and organization structure data).");

		return options;
	}

	public static Options optsBpmnOrgAm() {
		Options options = new Options();

		options.addOption(ARGS_INPUT_TYPE, ARGS_INPUT_TYPE_FULL, true,
				format("type of the conversion. Possible values are: %s", ConsoleRunner.ConverterType.strValues()));
		options.addRequiredOption(ARGS_INPUT_BPMN_FILE, ARGS_INPUT_BPMN_FILE_FULL, true,
				"input *.bpmn file for the converter, containing process diagram.");
		options.addRequiredOption(ARGS_INPUT_ORG_FILE, ARGS_INPUT_ORG_FILE_FULL, true,
				"input *.xml file for the converter, containing organization structure definition");
		Option optionActorMapping = new Option(ARGS_INPUT_ACTOR_MAPPING_FILE, ARGS_INPUT_ACTOR_MAPPING_FILE_FULL, true,
				"input *.xml files for the converter, containing actor mapping definitions for every process in the provided bpmn diagram");
		optionActorMapping.setArgs(Option.UNLIMITED_VALUES);
		optionActorMapping.setRequired(true);
		options.addOption(optionActorMapping);

		options.addOption(ARGS_OUTPUT_FILE, ARGS_OUTPUT_FILE_FULL, true,
				"output files directory");
		return options;
	}

	// none
	public static BpmnOrgAmArguments parse1(String[] args) throws ParseException {
		Options options = new Options();
		optType().getOptions().forEach(options::addOption);
		optsBaseIri().getOptions().forEach(options::addOption);
		optsBpmnOrgAm().getOptions().forEach(options::addOption);
//		optsOutputFile(true).getOptions().forEach(options::addOption);

		CommandLine commandLine = parseArgs(options, args);
		return new BpmnOrgAmArguments(commandLine);
	}

	// bbo
	// stamp
	public static BpmnOrgAmArguments parse2(String[] args) throws ParseException {
		Options options = new Options();
		optType().getOptions().forEach(options::addOption);
		optsBaseIri().getOptions().forEach(options::addOption);
		optsBpmnOrgAm().getOptions().forEach(options::addOption);
//		optsOutputFile(false).getOptions().forEach(options::addOption);

		CommandLine commandLine = parseArgs(options, args);
		return new BpmnOrgAmArguments(commandLine);
	}

	// stampFromBbo
	public static StampFromBboArguments parse3(String[] args) throws ParseException {
		Options options = new Options();
		optType().getOptions().forEach(options::addOption);
		optsBaseIri().getOptions().forEach(options::addOption);
		optsBbo().getOptions().forEach(options::addOption);
//		optsOutputFile(false).getOptions().forEach(options::addOption);

		CommandLine commandLine = parseArgs(options, args);
		return new StampFromBboArguments(commandLine);
	}

	private static CommandLine parseArgs(Options options, String[] args) throws ParseException {
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		try {
			return parser.parse(options, args, true);
		} catch (ParseException e) {
			formatter.printHelp("bpmn2stamp", options);
			throw e;
		}
	}

	public static class OutputArguments {
		private final String outputFile;
		private final String outputBboFile;
		private final String outputStampFile;

		public OutputArguments(String outputFile, String outputBboFile, String outputStampFile) {
			this.outputFile = outputFile;
			this.outputBboFile = outputBboFile;
			this.outputStampFile = outputStampFile;
		}

		public OutputArguments(CommandLine commandLine, String defaultFile, String bboDefaultFile, String stampDefaultFile) {
			this.outputFile = commandLine.getOptionValue(ARGS_OUTPUT_FILE) == null ? defaultFile : commandLine.getOptionValue(ARGS_OUTPUT_FILE);
			this.outputBboFile = commandLine.getOptionValue(ARGS_OUTPUT_BBO_FILE) == null ? bboDefaultFile : commandLine.getOptionValue(ARGS_OUTPUT_BBO_FILE);
			this.outputStampFile = commandLine.getOptionValue(ARGS_OUTPUT_STAMP_FILE) == null ? stampDefaultFile : commandLine.getOptionValue(ARGS_OUTPUT_STAMP_FILE);
		}

		public String getOutputFile() {
			return outputFile;
		}

		public String getOutputBboFile() {
			return outputBboFile;
		}

		public String getOutputStampFile() {
			return outputStampFile;
		}

		@Override
		public String toString() {
			return "OutputArguments{" +
					"outputFile='" + outputFile + '\'' +
					", outputBboFile='" + outputBboFile + '\'' +
					", outputStampFile='" + outputStampFile + '\'' +
					'}';
		}
	}

	public static class StampFromBboArguments {
		private final String bboFileArg;

		public StampFromBboArguments(String bboFileArg) {
			this.bboFileArg = bboFileArg;
		}

		public StampFromBboArguments(CommandLine commandLine) {
			this.bboFileArg = commandLine.getOptionValue(ARGS_INPUT_BBO);
		}

		public String getBboFileArg() {
			return bboFileArg;
		}

		@Override
		public String toString() {
			return "StampFromBboArguments{" +
					"bboFileArg='" + bboFileArg + '\'' +
					'}';
		}
	}

	public static class BpmnOrgAmArguments {
		private final String bpmnFileArg;
		private final String orgFileArg;
		private final List<File> actorMappingFilesList;

		public BpmnOrgAmArguments(String bpmnFileArg, String orgFileArg, List<File> actorMappingFilesList) {
			this.bpmnFileArg = bpmnFileArg;
			this.orgFileArg = orgFileArg;
			this.actorMappingFilesList = actorMappingFilesList;
		}

		public BpmnOrgAmArguments(CommandLine commandLine) {
			this.bpmnFileArg = commandLine.getOptionValue(ARGS_INPUT_BPMN_FILE);
			this.orgFileArg = commandLine.getOptionValue(ARGS_INPUT_ORG_FILE);
			this.actorMappingFilesList = convertToFile(commandLine.getOptionValues(ARGS_INPUT_ACTOR_MAPPING_FILE));
		}

		private List<File> convertToFile(String... paths) {
			return Arrays.stream(paths).map(File::new).collect(Collectors.toList());
		}

		public String getBpmnFileArg() {
			return bpmnFileArg;
		}

		public String getOrgFileArg() {
			return orgFileArg;
		}

		public List<File> getActorMappingFilesList() {
			return actorMappingFilesList;
		}

		@Override
		public String toString() {
			return "BpmnOrgAmArguments{" +
					"bpmnFileArg='" + bpmnFileArg + '\'' +
					", orgFileArg='" + orgFileArg + '\'' +
					", actorMappingFilesList=" + actorMappingFilesList +
					'}';
		}
	}

}
