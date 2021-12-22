package console;

import com.google.common.collect.Sets;
import mapper.bbo2stamp.Bbo2StampMappingResult;
import mapper.bpmn2bbo.Bpmn2BboMappingResult;
import mapper.org2bbo.Org2BboMappingResult;
import model.actor.ActorMappings;
import model.bbo.model.Thing;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import model.organization.Organization;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import persistance.BboRdfRepositoryReader;
import persistance.RdfRepositoryWriter;
import service.Bbo2StampMappingService;
import service.Bpmn2BboMappingService;
import service.FileReadingService;
import service.Organization2BboMappingService;
import service.OrganizationBbo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * TODO not implemented yed
 */
public class ConsoleRunner {
    public static final List<String> CONVERTER_MODS = List.of("bpmn2stamp");

    public static final String INPUT_FILE_OPT_NAME = "i";
    public static final String OUTPUT_FILE_OPT_NAME = "o";
    public static final String WORKING_TEMP_FOLDER_OPT_NAME = "w";

    public static final String DEFAULT_WORKING_TEMP_FOLDER = "./temp/";

    public static void main(String[] args) throws Exception {
        try {
            throw new Exception("Not implemented yet.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        /*

        bpmn2stamp-converter bpmn2bbo -i process.bpmn -o process.bbo -b http://ontology.com/
        converter bpmn2bbo -i process.bpmn -o process.bbo -b http://ontology.com/

        converter org2bbo -i organization.xml -o organization.bbo

        converter bbo2stamp -i process.bpmn -o process.bbo -b http://ontology.com/

        converter bpmn2stamp
            --bpmn_files process_1.bpmn,process_1.bpmn
            --organization_file organization.bpmn,process_1.bpmn
            -o process.bbo -b http://ontology.com/


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

        String modeArg = commandLine.getOptionValue("m");
        String bpmnFileArg = commandLine.getOptionValue("b");
        String orgFileArg = commandLine.getOptionValue("r");
        String actorMappingArg = commandLine.getOptionValue("a");
        String baseIriArg = commandLine.getOptionValue("i");
        String outputDirArg = commandLine.getOptionValue("o");

        if (modeArg.equals("bpmn2stamp")) {
            runBpmn2Stamp(
                    verifyThatFileExists(bpmnFileArg, true),
                    verifyThatFileExists(orgFileArg, true),
                    verifyThatFileExists(actorMappingArg, false),
                    baseIriArg,
                    new File(outputDirArg)
            );
        }

//        if (bpmnFileArg == null && orgFileArg == null) {
//            throw new IllegalArgumentException("at least one of the following inputs should present:" +
//                    "bpmn file, organization structure file");
//        }
    }

    private static CommandLine parseArguments(String[] args) throws ParseException {
        Options options = new Options();

        options.addRequiredOption("m", "mode", true,
                format("running mode of the converter. Possible values are: %s", CONVERTER_MODS));

        options.addRequiredOption("b", "bpmn", true,
                "input *.bpmn file for the converter, containing process diagram.");
        options.addRequiredOption("r", "org", true,
                "input *.xml file for the converter, containing organization structure definition");
        options.addRequiredOption("a", "actor", true,
                "input *.xml file for the converter, containing actor mapping definitions");
        options.addRequiredOption("i", "iri", true,
                "base iri for the output ontology");

        options.addRequiredOption("o", "output", true,
                "output files directory");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("", options);
            System.exit(1);
            throw e;
        }
    }

    private static void runBpmn2Stamp(File bpmnFile, File orgFile, File actorMappingFile, String baseIri, File outputDir) throws IOException {
        FileReadingService fileReadingService = new FileReadingService();

        String pre = baseIri;
        if (baseIri.endsWith("/"))
            pre = baseIri.substring(0, baseIri.length()-1);
        String bpmnOntologyIri = pre + "-bpmn";
        String organizationStructureOntologyIri = pre + "-organization-structure";
        String preStampOntologyIri = pre + "-prestamp";

        File bpmnOntoFile = outputDir.toPath().resolve("bpmn.owl").toFile();
        File orgOntoFile = outputDir.toPath().resolve("org.owl").toFile();
        File prestampOntoFile = outputDir.toPath().resolve("prestamp.owl").toFile();

        TDefinitions tDefinitions = fileReadingService.readBpmn(bpmnFile.getAbsolutePath());
        Bpmn2BboMappingService bpmnMapper = new Bpmn2BboMappingService();
        Bpmn2BboMappingResult bpmnResult = bpmnMapper.transform(
                tDefinitions,
                bpmnOntologyIri
        );
        new RdfRepositoryWriter(
                bpmnOntoFile.getAbsolutePath(),
                bpmnOntologyIri,
                Sets.newHashSet(organizationStructureOntologyIri)
        ).write(bpmnResult.getMappedObjects().values());

        ActorMappings actorMappings = fileReadingService.readActorMappings(actorMappingFile.getAbsolutePath());

        Organization organization = fileReadingService.readOrganizationStructure(orgFile.getAbsolutePath());
        Organization2BboMappingService organization2BboMappingService = new Organization2BboMappingService();
        Org2BboMappingResult organizationResult = organization2BboMappingService.transform(
                organization,
                organizationStructureOntologyIri
        );
        OrganizationBbo organizationBbo = organization2BboMappingService.extendOrganizationHierarchy(organizationResult.getOrganizationBbo(), actorMappings);
        RdfRepositoryWriter organizationRepoWriter = new RdfRepositoryWriter(
                orgOntoFile.getAbsolutePath(),
                organizationStructureOntologyIri,
                Sets.newHashSet("http://BPMNbasedOntology")
        );
        organizationRepoWriter.write(organizationBbo.getAllObjects().values());


//        BboRdfRepositoryReader rdfRepositoryReader = new BboRdfRepositoryReader(
//                bpmnOntoFile.getAbsolutePath(),
//                bpmnOntologyIri
//        );
//        List<Thing> list = rdfRepositoryReader.readAll();
//
//        BboRdfRepositoryReader rdfRepositoryReader2 = new BboRdfRepositoryReader(
//                orgOntoFile.getAbsolutePath(),
//                organizationStructureOntologyIri
//        );
//        List<Thing> list2 = rdfRepositoryReader2.readAll();
//        list.addAll(list2);
//        RdfRepositoryWriter preStampRepoWriter = new RdfRepositoryWriter(
//                prestampOntoFile.getAbsolutePath(),
//                preStampOntologyIri,
//                Sets.newHashSet("http://onto.fel.cvut.cz/ontologies/stamp", bpmnOntologyIri)
//        );
//        Bbo2StampMappingService bbo2StampMappingService = new Bbo2StampMappingService();
//        Bbo2StampMappingResult preStampResult = bbo2StampMappingService.transform(
//                list.stream().filter(Objects::nonNull).collect(Collectors.toList()),
//                preStampOntologyIri
//        );
//        preStampRepoWriter.write(preStampResult.getMappedObjects().values());
//        createFileIfMissing(outputDir);
    }

    private static File verifyThatFileExists(String filePath, boolean required) {
        File file = new File(filePath);
        if (!file.exists() && required)
            throw new IllegalArgumentException(format("File %s does not exist.", file));
        if (!file.exists() && !required)
            return null;
        return file;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createFileIfMissing(File file) throws IOException {
        file.createNewFile();
    }
}