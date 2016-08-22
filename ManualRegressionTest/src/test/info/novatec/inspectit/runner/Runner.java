/**
 *
 */
package test.info.novatec.inspectit.runner;

/**
 * @author Valentin Schraub
 *
 */
public class Runner {

	private int numberOfInvocationChildren;
	private int weightException;
	private int weightHTTP;
	private int weightLogging;
	private int weightSQL;
	private int weightTimers;
	private int sumOfWeights;

	private ExceptionRunner exceptionRunner;
	private HTTPRunner httpRunner;
	private LoggingRunner loggingRunner;
	private SQLRunner sqlRunner;
	private TimerRunner timerRunner;

	public void init() {
		exceptionRunner = new ExceptionRunner();
		httpRunner = new HTTPRunner();
		loggingRunner = new LoggingRunner();
		sqlRunner = new SQLRunner();
		timerRunner = new TimerRunner();

		numberOfInvocationChildren = Configuration.numberOfInvocationChildren();
		weightException = Configuration.weightException();
		weightHTTP = Configuration.weightHTTP();
		weightLogging = Configuration.weightLogging();
		weightSQL = Configuration.weightSQL();
		weightTimers = Configuration.weightTimers();

		sumOfWeights = Configuration.sumOfWeights();
	}

	public void execute() {
		long systemTimeStart = System.currentTimeMillis();
		for (int i = 0; i < numberOfInvocationChildren; i++) {
			int random = (int) (Math.random() * sumOfWeights);
			if ((random -= weightException) < 0) {
				exceptionRunner.run();
			} else if ((random -= weightHTTP) < 0) {
				httpRunner.run();
			} else if ((random -= weightLogging) < 0) {
				loggingRunner.run();
			} else if ((random -= weightSQL) < 0) {
				sqlRunner.run();
			} else if ((random -= weightTimers) < 0) {
				timerRunner.run();
			}
		}
		long duration = System.currentTimeMillis() - systemTimeStart;

	}
}
