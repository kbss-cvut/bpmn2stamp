# BPMN to STAMP converter (bpmn2stamp)

## About
This is a BPMN to STAMP converter, which contains two applications in form of submodules.
- bpmn2stamp - main parent module.
- bpmn2stamp-converter - converter library containing main logic.
- bpmn2stamp-console - CLI tool, uses the bpmn2stamp-converter as a dependency.

## Requirements
- Installed JDK of version 11+
- TODO add other requirements if needed (e.g. JAXB)

## CLI documentation and usage
The program can be run using by executing the JAR file. The command can be executed using one of the three types: bbo, stamp, and stampFromBbo.

### Type: BBO
Will generate a BBO ontology file, containing individuals of a transformed BPMN file, Organization structure fie, and Actor mapping files. Requires the following input arguments to be passed:
- **-iam, --inputActorMappings <arg>** - input *.xml files for the converter, containing actor mapping definitions for every process in the provided bpmn diagram
- **-ibpmn, --inputBpmn <arg>** - input *.bpmn file for the converter, containing process diagram.
- **-iorg, --inputOrg <arg>** - input *.xml file for the converter, containing organization structure definition
- **-iri, --baseIri <arg>** - base IRI for the output ontology. The prefixes will be added to the result files automatically.
- **-o, --output <arg>** - output files directory

#### Example of usage:
    java -jar bpmn2stamp.jar -t bbo -iam "Jednani-sag-actor-mapping.xml" -ibpmn "Jednani-sag.bpmn" -iorg "ucl.xml" -iri "http://jednani-sag-onto" -o "output_bbo.ttl"

### Type: Stamp
Will generate a STAMP ontology file, containing individuals of a transformed BPMN file, Organization structure fie, and Actor mapping files. Requires the following input arguments to be passed:
- **-iam, --inputActorMappings <arg>** - input *.xml files for the converter, containing actor mapping definitions for every process in the provided bpmn diagram
- **-ibpmn, --inputBpmn <arg>** - input *.bpmn file for the converter, containing process diagram.
- **-iorg, --inputOrg <arg>** - input *.xml file for the converter, containing organization structure definition
- **-iri, --baseIri <arg>** - base IRI for the output ontology. The prefixes will be added to the result files automatically.
- **-o, --output <arg>** - output files directory

#### Example of usage:
    java -jar bpmn2stamp.jar -t stamp -iam "Jednani-sag-actor-mapping.xml" -ibpmn "Jednani-sag.bpmn" -iorg "ucl.xml" -iri "http://jednani-sag-onto" -o "output_stamp.ttl"

### Type: StampFromBbo
Will generate a STAMP ontology file, containing individuals of a transformed BBO file. Requires the following input arguments to be passed:

- **-ibbo,--inputBbo <arg>** - input *.ttl file for the converter, containing individuals of the BBO ontology (typically having the bpmn and organization structure data).
- **-iri,--baseIri <arg>** - base IRI for the output ontology. The prefixes will be added to the result files automatically.
- **-o,--output <arg>** - output file

#### Example of usage:
    java -jar bpmn2stamp.jar -t stampFromBbo -ibbo “jednani-sag_bbo.ttl” -iri "http://jednani-sag-onto" -o "output_stamp.ttl"

## Development

### JOPA model generation

JOPA model is not generated automatically. Separate parts of the model, i.e. BBO part, BBO Extenstion part, STAMP part can be re-generated using mvn command-line as follows:
- `mvn jopa:owl2java-transform@generate-bbo`
- `mvn jopa:owl2java-transform@generate-bbo-e`
- `mvn jopa:owl2java-transform@generate-stamp`

See more information in relavant [pom.xml](./bpmn2stamp-converter/pom.xml).

## Other information
Reference examples of the output ontologies, along with the converter inputs/outputs can be found in [BPMN convertor examples](https://github.com/kbss-cvut/bpmn-convertor-examples) repository. 

-----
This work has been supported by the grant [No. CK01000204 "Digitalization of integrated aviation safety oversight"](https://starfos.tacr.cz/en/project/CK01000073) of Technology Agency of the Czech Republic.
