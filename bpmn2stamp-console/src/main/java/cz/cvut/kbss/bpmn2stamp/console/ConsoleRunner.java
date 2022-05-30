package cz.cvut.kbss.bpmn2stamp.console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

import static cz.cvut.kbss.bpmn2stamp.console.CommandLineUtils.parseArgs;
import static java.lang.String.format;

public class ConsoleRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ConsoleRunner.class.getName());

	private final Option helpOpt = Option.builder().option("h").longOpt("help").desc("Print help, usage").build();
	private final Option inputTypeOpt = Option.builder().option("t").longOpt("type")
			//instead of null we should just say what is default and that --type is optional.
			.desc(format("(optional) type of the conversion. Possible values are: %s. By default will generate both Bbo and Stamp files.", ConverterType.strValues())).build();

	private final Configuration config;
	private final Options helpOptions;
	private final Options baseOptions;
	private final NoneTypeArgsProcessor noneTypeArgsProcessor;
	private final StampTypeArgsProcessor stampTypeArgsProcessor;
	private final BboTypeArgsProcessor bboTypeArgsProcessor;
	private final StampFromBboTypeArgsProcessor stampFromBboTypeArgsProcessor;
	private final Bpmn2StampConverterService service;

	public ConsoleRunner(Configuration instance,
						 NoneTypeArgsProcessor noneTypeArgsProcessor,
						 StampTypeArgsProcessor stampTypeArgsProcessor,
						 BboTypeArgsProcessor bboTypeArgsProcessor,
						 StampFromBboTypeArgsProcessor stampFromBboTypeArgsProcessor,
						 Bpmn2StampConverterService bpmn2StampConverterService) {
		this.config = instance;
		this.noneTypeArgsProcessor = noneTypeArgsProcessor;
		this.stampTypeArgsProcessor = stampTypeArgsProcessor;
		this.bboTypeArgsProcessor = bboTypeArgsProcessor;
		this.stampFromBboTypeArgsProcessor = stampFromBboTypeArgsProcessor;
		this.service = bpmn2StampConverterService;
		
		this.helpOptions = new Options();
		this.helpOptions.addOption(helpOpt);
		this.baseOptions = new Options();
		this.baseOptions.addOption(inputTypeOpt);
	}

	public static void main(String[] args) throws Exception {
		ConsoleRunner app = new ConsoleRunner(
				Configuration.getInstance(),
				new NoneTypeArgsProcessor(),
				new StampTypeArgsProcessor(),
				new BboTypeArgsProcessor(),
				new StampFromBboTypeArgsProcessor(),
				new Bpmn2StampConverterService()
		);
		System.exit(app.run(args));
	}

	public int run(String[] args) {
		try {
			CommandLine cmd = parseArgs(helpOptions, args);

			if (cmd.hasOption(helpOpt)) {
				HelpFormatter f = new HelpFormatter();

				LOG.info("SYNOPSIS");
				LOG.info(noneTypeArgsProcessor.getSynopsis());
				LOG.info(stampTypeArgsProcessor.getSynopsis());
				LOG.info(bboTypeArgsProcessor.getSynopsis());
				LOG.info(stampFromBboTypeArgsProcessor.getSynopsis());

				LOG.info("\n");
				printHelp(f, "Required args are ", baseOptions);
				LOG.info("Each type has own syntax:");
				printHelp(f, "for type=none the program will generate BBO and STAMP ontology files. Arguments are ", noneTypeArgsProcessor.getOptions());
				printHelp(f, "for type=bbo the program will generate a BBO ontology file. Arguments are ", stampTypeArgsProcessor.getOptions());
				printHelp(f, "for type=stamp the program will generate a STAMP ontology file. Arguments are ", bboTypeArgsProcessor.getOptions());
				printHelp(f, "for type=stampFromBbo the program will generate a STAMP ontology file, containing individuals of a transformed BBO file. Arguments are ", stampFromBboTypeArgsProcessor.getOptions());
				return 0;
			}

			cmd = parseArgs(baseOptions, args);

			ConverterType type = ConverterType.resolve(cmd.getOptionValue(inputTypeOpt));

			if (type == ConverterType.NONE) {
				NoneTypeArgsProcessor.ParsingResult res = noneTypeArgsProcessor.processArgs(cmd.getArgs());

				service.init(appendSuffix(res.getBaseIri(), config.getBpmnSuffix()),
						appendSuffix(res.getBaseIri(), config.getOrgSuffix()),
						appendSuffix(res.getBaseIri(), config.getStampSuffix()));

				service.convertToStampAndBbo(
						res.getInputBpmnFile(),
						res.getInputOrgFile(),
						res.getInputActorMappingFile(),
						res.getOutputBboFile(),
						res.getOutputStampFile()
				);
			} else if (type == ConverterType.BBO) {
				BboTypeArgsProcessor.ParsingResult res = bboTypeArgsProcessor.processArgs(cmd.getArgs());

				service.init(appendSuffix(res.getBaseIri(), config.getBpmnSuffix()),
						appendSuffix(res.getBaseIri(), config.getOrgSuffix()),
						appendSuffix(res.getBaseIri(), config.getStampSuffix()));

				service.convertToBbo(
						res.getInputBpmnFile(),
						res.getInputOrgFile(),
						res.getInputActorMappingFile(),
						res.getOutputBboFile()
				);
			} else if (type == ConverterType.STAMP) {
				StampTypeArgsProcessor.ParsingResult res = stampTypeArgsProcessor.processArgs(cmd.getArgs());

				service.init(appendSuffix(res.getBaseIri(), config.getBpmnSuffix()),
						appendSuffix(res.getBaseIri(), config.getOrgSuffix()),
						appendSuffix(res.getBaseIri(), config.getStampSuffix()));

				service.convertToStamp(
						res.getInputBpmnFile(),
						res.getInputOrgFile(),
						res.getInputActorMappingFile(),
						res.getOutputStampFile()
				);
			} else if (type == ConverterType.STAMP_FROM_BBO) {
				StampFromBboTypeArgsProcessor.ParsingResult res = stampFromBboTypeArgsProcessor.processArgs(cmd.getArgs());

				service.init(appendSuffix(res.getBaseIri(), config.getBpmnSuffix()),
						appendSuffix(res.getBaseIri(), config.getOrgSuffix()),
						appendSuffix(res.getBaseIri(), config.getStampSuffix()));

				service.convertToStampFromBbo(
						res.getInputBboFile(),
						res.getOutputStampFile()
				);
			}
		} catch (ParseException e) {
			LOG.error(e.getMessage());
			LOG.info("For help use argument -h or --help");
			return 1;
		}
		LOG.info("Done!");
		return 0;
	}
	
	private void printHelp(final HelpFormatter formatter, final String cmdLineSyntax, final Options options) {
		try (
				OutputStream loggerOS = new LoggerOutputStream(LOG, Level.INFO);
				PrintWriter printWriter = new PrintWriter(loggerOS);
		) {
			formatter.printHelp(printWriter, formatter.getWidth(), cmdLineSyntax, null, options, formatter.getLeftPadding(), formatter.getDescPadding(),null, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String appendSuffix(String base, String suffix) {
		String pre = base;
		if (base.endsWith("/"))
			pre = base.substring(0, base.length() - 1);
		return pre + suffix;
	}

	enum ConverterType {
		NONE(null),
		BBO("bbo"),
		STAMP("stamp"),
		STAMP_FROM_BBO("stampFromBbo");

		private final String value;

		ConverterType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static ConverterType resolve(String value) throws ParseException {
			if (value == null) return NONE;
			for (ConverterType converterType : ConverterType.values()) {
				if (converterType.getValue() != null && converterType.getValue().equals(value))
					return converterType;
			}
			throw new ParseException("Unknown conversion type");
		}

		public static String strValues() {
			return Arrays.stream(ConverterType.values())
					.filter(e -> e != NONE)
					.map(ConverterType::getValue)
					.collect(Collectors.joining(","));
		}
	}
}