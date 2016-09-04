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

	private JVMRunner jvmRunner;

	private ExceptionRunner exceptionRunner;
	private HTTPRunner httpRunner;
	private LoggingRunner loggingRunner;
	private SQLRunner sqlRunner;
	private TimerRunner timerRunner;

	private long duration;
	private int counterException;
	private int counterHTTP;
	private int counterLogging;
	private int counterSQL;
	private int counterTimer;

	public Runner(JVMRunner jvmRunner) {
		this.jvmRunner = jvmRunner;
	}

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

	private void resetCounters() {
		counterException = 0;
		counterHTTP = 0;
		counterLogging = 0;
		counterSQL = 0;
		counterTimer = 0;
	}

	public void execute() {
		resetCounters();
		long systemTimeStart = System.currentTimeMillis();

		for (int i = 0; i < numberOfInvocationChildren; i++) {
			int random = (int) (Math.random() * sumOfWeights);
			if ((random -= weightException) < 0) {
				exceptionRunner.run();
				counterException++;
			} else if ((random -= weightHTTP) < 0) {
				httpRunner.run();
				counterHTTP++;
			} else if ((random -= weightLogging) < 0) {
				loggingRunner.run();
				counterLogging++;
			} else if ((random -= weightSQL) < 0) {
				sqlRunner.run();
				counterSQL++;
			} else if ((random -= weightTimers) < 0) {
				timerRunner.run();
				counterTimer++;
			}
		}

		duration = System.currentTimeMillis() - systemTimeStart;
		Result result = new Result(duration, counterException, counterHTTP, counterLogging, counterSQL, counterTimer);
		jvmRunner.addResult(result);
	}
}
