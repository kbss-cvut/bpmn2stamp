package console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * TODO not implemented yed
 */
public class ConsoleRunner {
    public static final String INPUT_FILE_OPT_NAME = "i";
    public static final String OUTPUT_FILE_OPT_NAME = "o";
    public static final String WORKING_TEMP_FOLDER_OPT_NAME = "w";
    public static final String THREADS_OPT_NAME = "t";
    public static final String DELAY_OPT_NAME = "d";
    public static final String AGENT_OPT_NAME = "a";

    public static final String DEFAULT_WORKING_TEMP_FOLDER = "./temp/";
    public static final String DEFAULT_THREADS_NUMBER = "1";
    public static final String DEFAULT_DELAY = "250";

    public static final int THREAD_SHUTDOWN_DELAY_SECONDS = 3;
    public static final int CLEANUP_DELAY_SECONDS = 3;

    public static void main(String[] args) throws Exception {
        try {
            throw new Exception("Not implemented yet.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        /*

        Inputs:
            - .bpmn file, containing multiple pools
            - .xml file, containing organization
                - without actor mapping to change hierarchy
                - with actor mapping file[s] to change hierarchy
            - .xml file, containing actor mapping
        Configuration:
            - output Ontology iri
            - org element iri constructor
            - bpmn element iri constructor
        Validation:
            - .bpmn file structure
                - validated based on xsd
            - .xml (organization structure) file structure
                - validated based on xsd
            - .xml (actor mapping) file structure
                - validated based on xsd
        Processing:
            - .bpmn file structure
                - validated based on xsd
            - .xml (organization structure) file structure
                - validated based on xsd
            - .xml (actor mapping) file structure
                - validated based on xsd
            actors.xml
            bbo-bpmn.ttl
            bbo-org.ttl
        Outputs:
            prestamp.ttl
        Optional:
            external bbo ontology
            external stamp ontology

        Combinations:
        bpmn.xml[, actors.xml] > bbo-bpmn.ttl > prestamp.ttl
        org.xml[, actors.xml] > bbo-org.ttl > prestamp.ttl
        bpmn.xml, org.xml[, actors.xml] > bbo-bpmn.ttl, bbo-org.ttl > prestamp.ttl



        converter bpmn2bbo --input "C://..." --output "C://..."

        converter bpmn2bbo --input "C://..." --output "C://..."



         */

        CommandLine commandLine = parseArguments(args);

        File inputFile = new File(commandLine.getOptionValue(INPUT_FILE_OPT_NAME));
        File outputFile = new File(commandLine.getOptionValue(OUTPUT_FILE_OPT_NAME));
        String crawlStorageFolderPath = commandLine.getOptionValue(WORKING_TEMP_FOLDER_OPT_NAME, DEFAULT_WORKING_TEMP_FOLDER);
        int threadsNumber = Integer.parseInt(commandLine.getOptionValue(THREADS_OPT_NAME, DEFAULT_THREADS_NUMBER));
        int delay = Integer.parseInt(commandLine.getOptionValue(DELAY_OPT_NAME, DEFAULT_DELAY));
        String agent = commandLine.getOptionValue(AGENT_OPT_NAME, null);

        createFileIfMissing(outputFile);

//        saveAllItemsAsJson(outputFile, itemHashMap);
//        log("Finished.");
    }

    private static CommandLine parseArguments(String[] args) throws ParseException {
        Options options = new Options();

        Option inputFile = new Option(INPUT_FILE_OPT_NAME, "input", true,
                "file, containing seed urls (every url on separate line without delimiter)");
        inputFile.setRequired(true);
        inputFile.setType(String.class);
        options.addOption(inputFile);

        Option outputFile = new Option(OUTPUT_FILE_OPT_NAME, "output", true,
                "file, where output json will be saved. File will be created if doesn't exist");
        outputFile.setRequired(true);
        outputFile.setType(String.class);
        options.addOption(outputFile);

        Option numberOfThreads = new Option(THREADS_OPT_NAME, "threads", true,
                "number of threads for crawlers");
        numberOfThreads.setRequired(false);
        numberOfThreads.setType(Integer.class);
        options.addOption(numberOfThreads);

        Option delay = new Option(DELAY_OPT_NAME, "delay", true,
                "politeness delay in milliseconds (delay between sending two requests to the same host)");
        delay.setRequired(false);
        delay.setType(Integer.class);
        options.addOption(delay);

        Option agent = new Option(AGENT_OPT_NAME, "agent", true,
                "user-agent string that is used for representing your crawler to web servers. See http://en.wikipedia.org/wiki/User_agent for more details");
        agent.setRequired(false);
        agent.setType(Integer.class);
        options.addOption(agent);

        Option workingTempFolder = new Option(WORKING_TEMP_FOLDER_OPT_NAME, "tmp", true,
                "the folder which will be used by crawler for storing the intermediate crawl data. The content of this folder should not be modified manually");
        workingTempFolder.setRequired(false);
        workingTempFolder.setType(Integer.class);
        options.addOption(workingTempFolder);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("java -jar heureka-parser.jar -i input_seeds.txt -o result.json", options);
            System.exit(1);
            throw e;
        }
    }

    private static List<String> readSeedUrlsFromFile(File seedsInputFile) throws IOException {
        return Files.readAllLines(seedsInputFile.toPath());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createFileIfMissing(File file) throws IOException {
        file.createNewFile();
    }
}