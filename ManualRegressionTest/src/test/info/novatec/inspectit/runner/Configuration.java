package test.info.novatec.inspectit.runner;

import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyBindParameters;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyCauseException;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyFileNameResults;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyHTTPGet;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyHTTPS;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyNumberOfConcurrentThreads;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyNumberOfFirstLevelChildren;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyPassExceptions;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyPreparedStatement;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keySimpleInvocationSequence;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyThreadExecutionTime;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyThreadPause;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyTimerCharting;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyWeightException;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyWeightHTTP;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyWeightLogging;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyWeightSQL;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyWeightTimer;

import java.util.Set;

/**
 * @author Valentin Schraub
 *
 */
public class Configuration {

	private static final int defaultNumberOfConcurrentThreads = 5;
	private static final int defaultThreadExecutionTime = 30000;
	private static final int defaultThreadPause = 50;
	private static final int defaultNumberOfFirstLevelChildren = 100;

	private static final int defaultWeightException = 15;
	private static final int defaultWeightHTTP = 20;
	private static final int defaultWeightLogging = 1;
	private static final int defaultWeightSQL = 2;
	private static final int defaultWeightTimers = 7;

	private static final String defaultFileNameResults = "results/results.txt";


	private static int weightException;
	private static int weightHTTP;
	private static int weightLogging;
	private static int weightSQL;
	private static int weightTimers;
	private static int sumOfWeights;

	public static void getWeights() {
		sumOfWeights = 0;
		Set<Object> keySet = System.getProperties().keySet();
		boolean hasAny = keySet.contains(keyWeightException) || keySet.contains(keyWeightHTTP) || keySet.contains(keyWeightLogging) || keySet.contains(keyWeightSQL) || keySet.contains(keyWeightTimer);
		if (hasAny) {
			try {
				weightException = Integer.parseInt(System.getProperty(keyWeightException));
				sumOfWeights += weightException;
			} catch (NumberFormatException e) {
			}
			try {
				weightHTTP = Integer.parseInt(System.getProperty(keyWeightHTTP));
				sumOfWeights += weightHTTP;
			} catch (NumberFormatException e) {
			}
			try {
				weightLogging = Integer.parseInt(System.getProperty(keyWeightLogging));
				sumOfWeights += weightLogging;
			} catch (NumberFormatException e) {
			}
			try {
				weightSQL = Integer.parseInt(System.getProperty(keyWeightSQL));
				sumOfWeights += weightSQL;
			} catch (NumberFormatException e) {
			}
			try {
				weightTimers = Integer.parseInt(System.getProperty(keyWeightTimer));
				sumOfWeights += weightTimers;
			} catch (NumberFormatException e) {
			}
		}

		if (sumOfWeights == 0) {
			// default values
			weightException = defaultWeightException;
			weightHTTP = defaultWeightHTTP;
			weightLogging = defaultWeightLogging;
			weightSQL = defaultWeightSQL;
			weightTimers = defaultWeightTimers;
			sumOfWeights = weightException + weightHTTP + weightLogging + weightSQL + weightTimers;
		}
	}

	public static String fileNameResults() {
		return System.getProperty(keyFileNameResults, defaultFileNameResults);
	}

	// Thread configuration
	public static int numberOfConcurrentThreads() {
		try {
			return Integer.parseInt(System.getProperty(keyNumberOfConcurrentThreads));
		} catch (NumberFormatException e) {
			return defaultNumberOfConcurrentThreads;
		}
	}

	public static int threadExecutionTime() {
		try {
			return Integer.parseInt(System.getProperty(keyThreadExecutionTime));
		} catch (NumberFormatException e) {
			return defaultThreadExecutionTime;
		}
	}

	public static int threadPause() {
		try {
			return Integer.parseInt(System.getProperty(keyThreadPause));
		} catch (NumberFormatException e) {
			return defaultThreadPause;
		}
	}

	// Runner configuration
	public static int numberOfInvocationChildren() {
		try {
			return Integer.parseInt(System.getProperty(keyNumberOfFirstLevelChildren));
		} catch (NumberFormatException e) {
			return defaultNumberOfFirstLevelChildren;
		}
	}

	public static int weightException() {
		return weightException;
	}

	public static int weightHTTP() {
		return weightHTTP;
	}

	public static int weightLogging() {
		return weightLogging;
	}

	public static int weightSQL() {
		return weightSQL;
	}

	public static int weightTimers() {
		return weightTimers;
	}

	public static int sumOfWeights() {
		return sumOfWeights;
	}

	// Exception
	public static boolean causeException() {
		return "true".equals(System.getProperty(keyCauseException));
	}

	public static boolean passExceptions() {
		return "true".equals(System.getProperty(keyPassExceptions));
	}

	// HTTP
	public static boolean getRequest() {
		return "true".equals(System.getProperty(keyHTTPGet));
	}

	public static boolean https() {
		return "true".equals(System.getProperty(keyHTTPS));
	}

	// Invocation sequence
	public static boolean simple() {
		return "true".equals(System.getProperty(keySimpleInvocationSequence));
	}

	// SQL
	public static boolean bindParameters() {
		return "true".equals(System.getProperty(keyBindParameters));
	}

	public static boolean preparedStatement() {
		return "true".equals(System.getProperty(keyPreparedStatement));
	}

	// Timer
	public static boolean withCharting() {
		return "true".equals(System.getProperty(keyTimerCharting));
	}
}
