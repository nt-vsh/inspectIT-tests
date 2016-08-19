package test.info.novatec.inspectit.runner;

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

	private static String repositoryLocation;
	private static String agentLocation;

	private static final String defaultAgentLocation = "C:\\Users\\vsh\\inspectit\\git\\inspectIT\\inspectit.agent.java";
	private static final String defaultRepositoryLocation = "localhost:9070";
	private static final String defaultOptions = "-Dexception.cause=true -Dhttp.https=true -Dweight.http=6 -Dweight.exception=2";

	public static void main(String[] args) {
		try {
			numberOfAgents = Integer.parseInt(System.getProperty("agent.number"));
		} catch (NumberFormatException e) {
			numberOfAgents = defaultNumberOfAgents;
		}
		repositoryLocation = System.getProperty("repository.location", defaultRepositoryLocation);
		agentLocation = System.getProperty("agent.location", defaultAgentLocation);

		processes = new ArrayList<>();

		// if no option is provided the standard method is mixed
		allAgentsSameName = "true".equals(System.getProperty("agent.name.same"));
		allAgentsDifferentName = "true".equals(System.getProperty("agent.name.different"));

		try {
			for (int i = 0; i < numberOfAgents; i++) {
				Process process;
				String agentName;
				if (allAgentsSameName) {
					agentName = defaultAgentName;
				} else if (allAgentsDifferentName) {
					agentName = "Agent" + i;
				} else {
					agentName = "Agent" + (int) (10 * Math.random());
				}
				process = runAgent(agentName, defaultOptions);
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
			properties += " " + System.getProperty((String) key);
		}

		String command = "java -Xbootclasspath/p:" + agentLocation + "/build/release/inspectit-agent.jar";
		command += " -javaagent:" + agentLocation + "/build/release/inspectit-agent.jar";
		command += " -Dinspectit.repository=" + repositoryLocation;
		command += " -Dinspectit.agent.name=" + agentName;
		command += " -Dinspectit.logging.config=" + agentLocation + "\\src\\main\\external-resources\\logging-config.xml";
		command += " " + options;
		// command += properties;
		command += " -jar " + fileNameJAR;

		Process process = Runtime.getRuntime().exec(command);
		return process;
	}
}
