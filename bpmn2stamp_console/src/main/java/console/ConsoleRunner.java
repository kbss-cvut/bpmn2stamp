package console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * TODO not implemented yed
 */
public class ConsoleRunner {

    public static void main(String[] args) throws Exception {

        ConverterType type = parseTypeArguments(args);
        if (type == null) {
            System.err.printf("Unknown conversion type. Possible values are: %s", ConverterType.strValues());
            System.exit(-1);
        }
        if (type == ConverterType.BBO) {
            CommandLine commandLine = parseBpmnOrgAmArguments(args);
            String bpmnFileArg = commandLine.getOptionValue("ibpmn");
            String orgFileArg = commandLine.getOptionValue("iorg");
            String[] actorMappingFilesArg = commandLine.getOptionValues("iam");

            List<File> actorMappingFilesList = Arrays.stream(actorMappingFilesArg).map(File::new).collect(Collectors.toList());

            String baseIriArg = commandLine.getOptionValue("iri");
            String outputFileArg = commandLine.getOptionValue("o");

            Bpmn2StampConverterService service = new Bpmn2StampConverterService(
                    appendSuffix(baseIriArg, "-bpmn"),
                    appendSuffix(baseIriArg, "-organization-structure"),
                    appendSuffix(baseIriArg, "-pre-stamp")
            );
            service.convertToBbo(new File(bpmnFileArg), new File(orgFileArg), actorMappingFilesList, new File(outputFileArg));
        } else if (type == ConverterType.STAMP) {
            CommandLine commandLine = parseBpmnOrgAmArguments(args);
            String bpmnFileArg = commandLine.getOptionValue("ibpmn");
            String orgFileArg = commandLine.getOptionValue("iorg");
            String[] actorMappingFilesArg = commandLine.getOptionValues("iam");

            List<File> actorMappingFilesList = Arrays.stream(actorMappingFilesArg).map(File::new).collect(Collectors.toList());

            String baseIriArg = commandLine.getOptionValue("iri");
            String outputFileArg = commandLine.getOptionValue("o");

            Bpmn2StampConverterService service = new Bpmn2StampConverterService(
                    appendSuffix(baseIriArg, "-bpmn"),
                    appendSuffix(baseIriArg, "-organization-structure"),
                    appendSuffix(baseIriArg, "-pre-stamp")
            );
            service.convertToStamp(new File(bpmnFileArg), new File(orgFileArg), actorMappingFilesList, new File(outputFileArg));
        } else if (type == ConverterType.STAMP_FROM_BBO) {
            CommandLine commandLine = parseBboArguments(args);
            String bboFileArg = commandLine.getOptionValue("ibbo");
            String baseIriArg = commandLine.getOptionValue("iri");
            String outputFileArg = commandLine.getOptionValue("o");

            Bpmn2StampConverterService service = new Bpmn2StampConverterService(
                    appendSuffix(baseIriArg, "-bpmn"),
                    appendSuffix(baseIriArg, "-organization-structure"),
                    appendSuffix(baseIriArg, "-pre-stamp")
            );
            service.convertToStamp(new File(bboFileArg), new File(outputFileArg));
        }
        System.out.println("Done!");
    }

    private static String appendSuffix(String base, String suffix) {
        String pre = base;
        if (base.endsWith("/"))
            pre = base.substring(0, base.length() - 1);
        return pre + suffix;
    }

    private static ConverterType parseTypeArguments(String[] args) throws ParseException {
        Options options = new Options();

        options.addRequiredOption("t", "type", true,
                format("type of the conversion. Possible values are: %s", ConverterType.strValues()));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args, true);
            return ConverterType.resolve(cmd.getOptionValue("t"));
        } catch (ParseException e) {
            e.printStackTrace();
            formatter.printHelp("bpmn2stamp", options);
            System.exit(1);
            throw e;
        }
    }

    private static CommandLine parseBboArguments(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption("t", "type", true,
                format("type of the conversion. Possible values are: %s", ConverterType.strValues()));

        options.addRequiredOption("ibbo", "inputBbo", true,
                "input *.ttl file for the converter, containing individuals of the BBO ontology (typically having the bpmn and organization structure data).");

        options.addRequiredOption("iri", "baseIri", true,
                "base iri for the output ontology. The prefixes will be added to the result files automatically.");

        options.addRequiredOption("o", "output", true,
                "output file");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            formatter.printHelp("bpmn2stamp", options);
            System.exit(1);
            throw e;
        }
    }

    private static CommandLine parseBpmnOrgAmArguments(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption("t", "type", true,
                format("type of the conversion. Possible values are: %s", ConverterType.strValues()));
        options.addRequiredOption("ibpmn", "inputBpmn", true,
                "input *.bpmn file for the converter, containing process diagram.");
        options.addRequiredOption("iorg", "inputOrg", true,
                "input *.xml file for the converter, containing organization structure definition");
        Option optionActorMapping = new Option("iam", "inputActorMappings", true,
                "input *.xml files for the converter, containing actor mapping definitions for every process in the provided bpmn diagram");
        optionActorMapping.setArgs(Option.UNLIMITED_VALUES);
        optionActorMapping.setRequired(true);
        options.addOption(optionActorMapping);

        options.addRequiredOption("iri", "baseIri", true,
                "base iri for the output ontology. The prefixes will be added to the result files automatically.");

        options.addRequiredOption("o", "output", true,
                "output files directory");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            formatter.printHelp("bpmn2stamp", options);
            System.exit(1);
            throw e;
        }
    }

    private static File verifyThatFileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            throw new IllegalArgumentException(format("File %s does not exist.", file));
        return file;
    }

    enum ConverterType {
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

        public static ConverterType resolve(String value) {
            for (ConverterType converterType : ConverterType.values()) {
                if (converterType.getValue().equals(value))
                    return converterType;
            }
            return null;
        }
        public static String strValues() {
            return Arrays.stream(ConverterType.values()).map(ConverterType::getValue).collect(Collectors.joining(","));
        }
    }

//    @SuppressWarnings("ResultOfMethodCallIgnored")
//    private static void createFileIfMissing(File file) throws IOException {
//        file.createNewFile();
//    }
}