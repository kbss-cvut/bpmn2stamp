package org.example.mapper;

import java.util.function.Function;

public class OntologyMappingConfig {

    /**
     * Base part of the IRIs, generated through mapping process.
     * <p>
     * Example: {@code "http://example.com/ontology/"}
     * </p>
     */
    private String baseIri;
    private Function<String, String> iriMappingFunction;

    public OntologyMappingConfig(String baseIri, Function<String, String> iriMappingFunction) {
        this.baseIri = baseIri;
        this.iriMappingFunction = iriMappingFunction;
    }

    public String getBaseIri() {
        return baseIri;
    }

    public void setBaseIri(String baseIri) {
        this.baseIri = baseIri;
    }

    public Function<String, String> getIriMappingFunction() {
        return iriMappingFunction;
    }

    public void setIriMappingFunction(Function<String, String> iriMappingFunction) {
        this.iriMappingFunction = iriMappingFunction;
    }

}
