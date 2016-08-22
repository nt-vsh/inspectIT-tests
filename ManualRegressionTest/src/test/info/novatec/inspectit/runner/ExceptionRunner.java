package test.info.novatec.inspectit.runner;

import test.info.novatec.inspectit.exceptions.CauseException;
import test.info.novatec.inspectit.exceptions.MainException;
import test.info.novatec.inspectit.exceptions.ThrowExceptions;

/**
 * @author Valentin Schraub
 *
 */
public class ExceptionRunner {

	private boolean causeException;
	private boolean passExceptions;

	private ThrowExceptions throwExceptions;

	public ExceptionRunner() {
		throwExceptions = new ThrowExceptions();

		causeException = Configuration.causeException();
		passExceptions = Configuration.passExceptions();
	}

	public void run() {
		try {
			if (passExceptions) {
				throwExceptions.passingExceptionsNoLogging();
			} else if (causeException) {
				throwExceptions.throwCauseException();
			} else {
				throwExceptions.throwMainException();
			}
		} catch (CauseException | MainException e) {
			e.printStackTrace();
		}
	}
}
