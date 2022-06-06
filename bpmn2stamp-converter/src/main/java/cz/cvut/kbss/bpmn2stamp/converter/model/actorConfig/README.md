# Actor mapping configuration schema

No schema was found on Bonita's web nor in Bonita Studio repository. A solution was to generate schema from multiple
examples of ".conf" files, to obtain the most accurate result.

The generated XSD actorMappingConfig.xsd was created using ".conf" files from "Dozor nad provozovateli letišť" project.

A third-party program was used for schema creation: https://github.com/relaxng/jing-trang.
Example of usage:

    java -jar trang.jar -I xml -O xsd ./input/*.conf definition.xsd