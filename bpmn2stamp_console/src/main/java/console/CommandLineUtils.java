package console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineUtils {

	public static CommandLine parseArgs(Options options, String[] args) throws ParseException {
		return new DefaultParser().parse(options, args, true);
	}

}
