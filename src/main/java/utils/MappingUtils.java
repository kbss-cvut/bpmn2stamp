package utils;

import common.Constants;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TBaseElement;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TBaseElementWithMixedContent;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.UUID;

public class MappingUtils {

    public static final String GENERATE_UUID_EXPRESSION = "java(utils.MappingUtils.generateUuid())";

    public static final String CALCULATE_ID_FROM_GROUP_NAME_EXPRESSION = "java(utils.MappingUtils.transformToUriCompliant(group.getName()))";
    public static final String CALCULATE_ID_FROM_ROLE_NAME_EXPRESSION = "java(utils.MappingUtils.transformToUriCompliant(role.getName()))";

    public static final String TRANSFORM_TO_IRI_BPMN = "java(utils.MappingUtils.transformToUriCompliant(group.getName()))";
    public static final String TRANSFORM_TO_IRI_ORG = "java(utils.MappingUtils.transformToUriCompliant(group.getName()))";

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public static String transformToUriCompliant(String baseUri, String string) {
        if (string == null) {
            string = generateUuid();
        }
        String compliantId = string
                .toLowerCase()
//                .replaceAll("[^a-zA-Z0-9/]" , "-");
                .replaceAll("\\s" , "_");
        if (!baseUri.endsWith(Constants.ONTOLOGY_IRI_SUFFIX)) {
            return baseUri + Constants.ONTOLOGY_IRI_SUFFIX + compliantId;
        }
        return baseUri + compliantId;
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
