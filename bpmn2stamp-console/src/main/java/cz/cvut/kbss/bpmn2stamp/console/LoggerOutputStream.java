package cz.cvut.kbss.bpmn2stamp.console;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.io.OutputStream;

/**
 * This class logs all bytes written to it as output stream with a specified logging level.
 *
 * @author <a href="mailto:cspannagel@web.de">Christian Spannagel</a>
 * @version 1.0
 */
public class LoggerOutputStream extends OutputStream {
	/** The logger where to log the written bytes. */
	private Logger logger;

	/** The level. */
	private Level level;

	/** The internal memory for the written bytes. */
	private String mem;

	/**
	 * Creates a new log output stream which logs bytes to the specified logger with the specified
	 * level.
	 *
	 * @param logger the logger where to log the written bytes
	 * @param level the level
	 */
	public LoggerOutputStream(Logger logger, Level level) {
		setLogger(logger);
		setLevel(level);
		mem = "";
	}

	/**
	 * Sets the logger where to log the bytes.
	 *
	 * @param logger the logger
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Returns the logger.
	 *
	 * @return DOCUMENT ME!
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Sets the logging level.
	 *
	 * @param level DOCUMENT ME!
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * Returns the logging level.
	 *
	 * @return DOCUMENT ME!
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Writes a byte to the output stream. This method flushes automatically at the end of a line.
	 *
	 * @param b DOCUMENT ME!
	 */
	public void write(int b) {
		byte[] bytes = new byte[1];
		bytes[0] = (byte) (b & 0xff);
		mem = mem + new String(bytes);
	
		if (mem.endsWith ("\n")) {
			mem = mem.substring (0, mem.length () - 1);
			flush ();
		}
	}

	/**
	 * Flushes the output stream.
	 */
	public void flush () {
		if (level == Level.INFO) logger.info(mem);
		else if (level == Level.DEBUG) logger.debug(mem);
		else if (level == Level.ERROR) logger.error(mem);
		else if (level == Level.WARN) logger.warn(mem);
		else if (level == Level.TRACE) logger.trace(mem);
		mem = "";
	}
}