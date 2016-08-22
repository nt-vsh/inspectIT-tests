package test.info.novatec.inspectit.runner;

/**
 * @author Valentin Schraub
 *
 */
public class ConfigurationKeys {
	public static final String prefix = "it.";

	public static final String keyNumberOfAgents = prefix + "agent.number";
	public static final String keyAgentLocation = prefix + "agent.location";
	public static final String keyLoggingConfigurationLocation = prefix + "logging.configuration.location";
	public static final String keyRepositoryLocation = prefix + "repository.location";
	public static final String keyDifferentNameForAgents = prefix + "agent.name.different";
	public static final String keySameNameForAgents = prefix + "agent.name.same";

	public static final String keyNumberOfConcurrentThreads = prefix + "thread.concurrent";
	public static final String keyThreadExecutionTime = prefix + "thread.time.execution";
	public static final String keyThreadPause = prefix + "thread.time.pause";

	public static final String keyNumberOfFirstLevelChildren = prefix + "runner.children";

	public static final String keyWeightException = prefix + "weight.exception";
	public static final String keyWeightHTTP = prefix + "weight.http";
	public static final String keyWeightLogging = prefix + "weight.logging";
	public static final String keyWeightSQL = prefix + "weight.sql";
	public static final String keyWeightTimer = prefix + "weight.timer";

	public static final String keyCauseException = prefix + "exception.cause";
	public static final String keyPassExceptions = prefix + "exception.pass";

	public static final String keyHTTPGet = prefix + "http.get";
	public static final String keyHTTPS = prefix + "http.https";

	public static final String keySimpleInvocationSequence = prefix + "isequence.simple";

	public static final String keyBindParameters = prefix + "sql.bindParameters";
	public static final String keyPreparedStatement = prefix + "sql.preparedStmt";

	public static final String keyTimerCharting = prefix + "timer.charting";
}
