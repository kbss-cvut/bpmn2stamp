package cz.cvut.kbss.bpmn2stamp.converter.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotated with this will not be used by {@link SmartMapstructMapper#mapNext(Object...)} and {@link SmartMapstructMapper#mapNextUnchecked(Object...)}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PrivateMapping {
}
