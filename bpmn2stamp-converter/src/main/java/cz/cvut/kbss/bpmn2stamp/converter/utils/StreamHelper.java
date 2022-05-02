package cz.cvut.kbss.bpmn2stamp.converter.utils;

import javax.xml.bind.JAXBElement;
import java.util.stream.Stream;

/**
 * Utils for Java streams
 */
public class StreamHelper {

    /**
     * Filters {@link JAXBElement elements} from the given stream, by the given filter class.
     * @param stream stream to process
     * @param classToFilter filter class
     * @return stream of the filtered elements. Every element is cast to the target class after filtering.
     */
    public static <T, C> Stream<C> filterAndMapValue(Stream<JAXBElement<? extends T>> stream, Class<C> classToFilter) {
        return stream
                .filter(e -> e.getValue().getClass() == classToFilter)
                .map(e -> classToFilter.cast(e.getValue()));
    }

}
