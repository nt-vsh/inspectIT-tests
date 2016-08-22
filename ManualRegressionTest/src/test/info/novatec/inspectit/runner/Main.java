package test.info.novatec.inspectit.runner;

import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyAgentLocation;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyCauseException;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyDifferentNameForAgents;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyHTTPS;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyLoggingConfigurationLocation;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyNumberOfAgents;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyRepositoryLocation;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keySameNameForAgents;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyWeightException;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyWeightHTTP;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.prefix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author Valentin Schraub
 *
 */
public class Main {

	private static final String fileNameJAR = "runners.jar";

	private static final String defaultAgentName = "OneAgent";
	private static boolean allAgentsSameName;
	private static boolean allAgentsDifferentName;

	private static final int defaultNumberOfAgents = 5;
	private static int numberOfAgents;
	private static ArrayList<Process> processes;

	private static String loggingConfigurationLocation;
	private static String repositoryLocation;
	private static String agentLocation;

	private static final String defaultAgentLocation = "inspectit-agent.jar";
	private static final String defaultLoggingConfigurationLocation = "logging-config.xml";
	private static final String defaultRepositoryLocation = "localhost:9070";
	private static final String defaultOptions = "-D" + keyCauseException + "=true -D" + keyHTTPS + "=true -D" + keyWeightHTTP + "=6 -D" + keyWeightException + "=2";

	public static void main(String[] args) {
		try {
			numberOfAgents = Integer.parseInt(System.getProperty(keyNumberOfAgents));
		} catch (NumberFormatException e) {
			numberOfAgents = defaultNumberOfAgents;
		}
		loggingConfigurationLocation = System.getProperty(keyLoggingConfigurationLocation, defaultLoggingConfigurationLocation);
		repositoryLocation = System.getProperty(keyRepositoryLocation, defaultRepositoryLocation);
		agentLocation = System.getProperty(keyAgentLocation, defaultAgentLocation);

		processes = new ArrayList<>();

		// if no option is provided the standard method is mixed
		allAgentsSameName = "true".equals(System.getProperty(keySameNameForAgents));
		allAgentsDifferentName = "true".equals(System.getProperty(keyDifferentNameForAgents));

		try {
			for (int i = 0; i < numberOfAgents; i++) {
				String agentName;
				if (allAgentsSameName) {
					agentName = defaultAgentName;
				} else if (allAgentsDifferentName) {
					agentName = "Agent" + i;
				} else {
					agentName = "Agent" + (int) (10 * Math.random());
				}
				Process process = runAgent(agentName, defaultOptions);
				processes.add(process);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Process runAgent(String agentName, String options) throws IOException {
		String properties = "";
		Set<Object> keySet = System.getProperties().keySet();
		for (Object key : keySet) {
			if (((String) key).startsWith(prefix)) {
				properties += " -D" + key + "=" + System.getProperty((String) key);
			}
		}
		System.out.println("properties: " + properties);

		String command = "java -Xbootclasspath/p:" + agentLocation;
		command += " -javaagent:" + agentLocation;
		command += " -Dinspectit.repository=" + repositoryLocation;
		command += " -Dinspectit.agent.name=" + agentName;
		command += " -Dinspectit.logging.config=" + loggingConfigurationLocation;
		command += " " + options;
		command += properties;
		command += " -jar " + fileNameJAR;

		Process process = Runtime.getRuntime().exec(command);
		return process;
	}
}
