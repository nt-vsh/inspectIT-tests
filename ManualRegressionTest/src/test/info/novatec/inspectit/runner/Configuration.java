package test.info.novatec.inspectit.runner;

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


	private static int weightException;
	private static int weightHTTP;
	private static int weightLogging;
	private static int weightSQL;
	private static int weightTimers;

	public static void getWeights() {
		int correctWeights = 0;
		Set<Object> keySet = System.getProperties().keySet();
		boolean hasAny = keySet.contains("weight.exception") || keySet.contains("weight.http") || keySet.contains("weight.logging") || keySet.contains("weight.sql") || keySet.contains("weight.timer");
		if (hasAny) {
			try {
				weightException = Integer.parseInt(System.getProperty("weight.exception"));
				correctWeights++;
			} catch (NumberFormatException e) {
			}
			try {
				weightHTTP = Integer.parseInt(System.getProperty("weight.http"));
				correctWeights++;
			} catch (NumberFormatException e) {
			}
			try {
				weightLogging = Integer.parseInt(System.getProperty("weight.logging"));
				correctWeights++;
			} catch (NumberFormatException e) {
			}
			try {
				weightSQL = Integer.parseInt(System.getProperty("weight.sql"));
				correctWeights++;
			} catch (NumberFormatException e) {
			}
			try {
				weightTimers = Integer.parseInt(System.getProperty("weight.timer"));
				correctWeights++;
			} catch (NumberFormatException e) {
			}
		}

		if (correctWeights == 0) {
			// default values
			weightException = defaultWeightException;
			weightHTTP = defaultWeightHTTP;
			weightLogging = defaultWeightLogging;
			weightSQL = defaultWeightSQL;
			weightTimers = defaultWeightTimers;
		}
	}

	// Thread configuration
	public static int numberOfConcurrentThreads() {
		try {
			return Integer.parseInt(System.getProperty("thread.concurrent"));
		} catch (NumberFormatException e) {
			return defaultNumberOfConcurrentThreads;
		}
	}

	public static int threadExecutionTime() {
		try {
			return Integer.parseInt(System.getProperty("thread.time.execution"));
		} catch (NumberFormatException e) {
			return defaultThreadExecutionTime;
		}
	}

	public static int threadPause() {
		try {
			return Integer.parseInt(System.getProperty("thread.time.pause"));
		} catch (NumberFormatException e) {
			return defaultThreadPause;
		}
	}

	// Runner configuration
	public static int numberOfInvocationChildren() {
		return defaultNumberOfFirstLevelChildren;
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

	// Exception
	public static boolean causeException() {
		return "true".equals(System.getProperty("exception.cause"));
	}

	public static boolean passExceptions() {
		return "true".equals(System.getProperty("exception.pass"));
	}

	// HTTP
	public static boolean getRequest() {
		return "true".equals(System.getProperty("http.get"));
	}

	public static boolean https() {
		return "true".equals(System.getProperty("http.https"));
	}

	// Invocation sequence
	public static boolean simple() {
		return "true".equals(System.getProperty("isequence.simple"));
	}

	// SQL
	public static boolean bindParameters() {
		return "true".equals(System.getProperty("sql.bindParameters"));
	}

	public static boolean preparedStatement() {
		return "true".equals(System.getProperty("sql.preparedStmt"));
	}

	// Timer
	public static boolean withCharting() {
		return "true".equals(System.getProperty("timer.charting"));
	}
}
