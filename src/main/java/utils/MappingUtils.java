package utils;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TBaseElement;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TBaseElementWithMixedContent;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MappingUtils {

    public static final String GENERATE_UUID_EXPRESSION = "java(utils.MappingUtils.generateUuid())";

    public static final String CALCULATE_ID_FROM_GROUP_NAME_EXPRESSION = "java(utils.MappingUtils.transformToUriCompliant(group.getName()))";
    public static final String CALCULATE_ID_FROM_ROLE_NAME_EXPRESSION = "java(utils.MappingUtils.transformToUriCompliant(role.getName()))";

    public static final String TRANSFORM_TO_IRI_BPMN = "java(utils.MappingUtils.transformToUriCompliant(group.getName()))";
    public static final String TRANSFORM_TO_IRI_ORG = "java(utils.MappingUtils.transformToUriCompliant(group.getName()))";

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

    public static Set<String> ensurePropertyValue(String propertyKey, Supplier<Map<String, Set<String>>> propertiesGetter, Consumer<Map<String, Set<String>>> propertiesSetter) {
        Map<String, Set<String>> properties = propertiesGetter.get();
        if (properties == null) {
            properties = new HashMap<>();
            propertiesSetter.accept(properties);
        }
        properties.putIfAbsent(propertyKey, new HashSet<>());
        return properties.get(propertyKey);
    }

    public static <T> String getClassIRI(Class<T> clazz) {
        OWLClass annotation = clazz.getAnnotation(OWLClass.class);
        if (annotation == null) {
            return null;
        }
        return annotation.iri();
    }

    public static String checkAndRepairId(String id) {
        if (StringUtils.isEmpty(id)) {
            return MappingUtils.generateUuid();
        }

        try {
            //noinspection ResultOfMethodCallIgnored
            URI.create(id);
        } catch (IllegalArgumentException e) {
//            String newId = MappingUtils.transformToUriCompliants(id);
//            LOG.warn("Entity has illegal id: {}. It was converted to the correct form: {}", id, newId);
//            return newId;
        }
        return id;
    }

    public static String extractSourceId(Object instance) {
        if (instance instanceof TBaseElement) {
            return ((TBaseElement) instance).getId();
        } else if (instance instanceof TBaseElementWithMixedContent) {
            return ((TBaseElementWithMixedContent) instance).getId();
        }
        return null;
    }

}
