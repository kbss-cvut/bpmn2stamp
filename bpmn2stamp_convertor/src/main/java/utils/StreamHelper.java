package utils;

import javax.xml.bind.JAXBElement;
import java.util.stream.Stream;

public class StreamHelper {

    public static <T, C> Stream<C> filterAndMapValue(Stream<JAXBElement<? extends T>> stream, Class<C> classToFilter) {
        return stream
                .filter(e -> e.getValue().getClass() == classToFilter)
                .map(e -> classToFilter.cast(e.getValue()));
    }

}
