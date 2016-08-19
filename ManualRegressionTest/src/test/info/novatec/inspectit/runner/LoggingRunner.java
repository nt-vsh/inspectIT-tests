package test.info.novatec.inspectit.runner;

import test.info.novatec.inspectit.logging.Log4JLoggingFeature;

/**
 * @author Valentin Schraub
 *
 */
public class LoggingRunner {

	private Log4JLoggingFeature loggingFeature;

	public LoggingRunner() {
		loggingFeature = new Log4JLoggingFeature();
	}

	public void run() {
		loggingFeature.normalLoggingWithAllSeverities();
	}
}
