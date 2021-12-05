package utils;

import java.util.UUID;

public class MappingUtils {

    public static final String GENERATE_UUID_EXPRESSION = "java(utils.MappingUtils.generateUuid())";

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

}
