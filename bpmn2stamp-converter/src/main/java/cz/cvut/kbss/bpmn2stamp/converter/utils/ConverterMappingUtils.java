package cz.cvut.kbss.bpmn2stamp.converter.utils;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TBaseElement;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TBaseElementWithMixedContent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Class provides helping methods for the implemented Mapstruct mappers in the bpmn2stamp converter.
 */
public class ConverterMappingUtils {

    /**
     * Generates a random UUID.
     * @implNote uniqueness insurance is delegated to the {@link UUID#randomUUID underlying method}
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Transforms given string to the form, that is compliant to the URI.
     * If input is null then generated random UUID
     * @param string string to be transformed
     */
    public static String transformToUriCompliant(String string) {
        if (string == null) {
            string = generateUuid();
        }
        return string
                .toLowerCase()
                .replaceAll("\\s" , "_");
    }

    /**
     * Method ensures if a given individual porperty map has a given key.
     * Initializes property map if needed. Creates an empty {@link HashMap} and puts as a value if a key is not set or value is null.
     *
     * @param propertyKey key to be ensured
     * @param propertiesGetter getter of the individual properties
     * @param propertiesSetter setter of the individual properties
     * @return property value of a given key
     */
    public static Set<String> ensurePropertyValue(String propertyKey, Supplier<Map<String, Set<String>>> propertiesGetter, Consumer<Map<String, Set<String>>> propertiesSetter) {
        Map<String, Set<String>> properties = propertiesGetter.get();
        if (properties == null) {
            properties = new HashMap<>();
            propertiesSetter.accept(properties);
        }
        properties.putIfAbsent(propertyKey, new HashSet<>());
        return properties.get(propertyKey);
    }

    /**
     * Retrieves value of a given clazz. Class should be annotated with {@lnk cz.cvut.kbss.jopa.model.annotations.OWLClass} class.
     * @return iri in string format or null
     */
    public static <T> String getClassIRI(Class<T> clazz) {
        OWLClass annotation = clazz.getAnnotation(OWLClass.class);
        if (annotation == null) {
            return null;
        }
        return annotation.iri();
    }

    /**
     * Extracts id of the given instance. This method works with the instances of XML model.
     * @param instance object to extract id from
     * @return extracted id
     */
    public static String extractSourceId(Object instance) {
        if (instance instanceof TBaseElement) {
            return ((TBaseElement) instance).getId();
        } else if (instance instanceof TBaseElementWithMixedContent) {
            return ((TBaseElementWithMixedContent) instance).getId();
        }
        return null;
    }

    /**
     * Adds class IRI to the set, which is accessed through the given getters and setters. Ensures if the set is initialized.
     *
     * @param typesGetter getter method of the specific individual, providing set of the individual type IRIs
     * @param typesSetter setter method of the specific individual, accepting set of the individual type IRIs
     * @param typesToAdd types to add to the set
     * @implNote for extraction of the class iri uses {@link #getClassIRI} method
     */
    public static void addTypesToIndividual(Supplier<Set<String>> typesGetter, Consumer<Set<String>> typesSetter, Class... typesToAdd) {
        Set<String> individualTypes = typesGetter.get();
        if (individualTypes == null)
            individualTypes = new HashSet<>();
        typesSetter.accept(individualTypes);
        for (Class type : typesToAdd) {
            String classIRI = ConverterMappingUtils.getClassIRI(type);
            if (classIRI != null)
                individualTypes.add(classIRI);
        }
    }

}
