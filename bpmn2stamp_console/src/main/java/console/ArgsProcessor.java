package console;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public interface ArgsProcessor<T> {

	/**
	 * @return synopsis string, which could contain multiple line breaks
	 */
	T processArgs(String[] args) throws ParseException;

	Options getOptions();

	/**
	 * @return synopsis string, which could contain multiple line breaks
	 */
	String getSynopsis();
}
