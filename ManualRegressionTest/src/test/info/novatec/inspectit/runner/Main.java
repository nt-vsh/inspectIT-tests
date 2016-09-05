package test.info.novatec.inspectit.runner;

import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyAgentLocation;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyFileNameResults;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyLoggingConfigurationLocation;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyNumberOfAgents;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyRepositoryLocation;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.keyResultsLocation;
import static test.info.novatec.inspectit.runner.ConfigurationKeys.prefix;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

import test.info.novatec.inspectit.runner.tools.RunnerTools;

/**
 * @author Valentin Schraub
 *
 */
public class Main {

	private static final int defaultNumberOfAgents = 5;
	private static int numberOfAgents;
	private static ArrayList<Process> processes;
	private static ArrayList<String> folders;
	private static Result[] results;
	private static int destroyedProcesses;

	private static String loggingConfigurationLocation;
	private static String repositoryLocation;
	private static String agentLocation;
	private static String resultsLocation;

	private static final String defaultAgentLocation = "inspectit-agent.jar";
	private static final String defaultJARLocation = "runners.jar";
	private static final String defaultLoggingConfigurationLocation = "logging-config.xml";
	private static final String defaultRepositoryLocation = "localhost:9070";
	private static final String defaultResultsLocation = "results/";
	private static final String fileNameResults = "results.txt";
	private static final String fileNameErrorStream = "errorStream.txt";
	private static final String fileNameInputStream = "inputStream.txt";

	public static final String startOfResults = "---RESULTS---";

	private static final String connectionToServerFailed = "Connection to the server failed";

	public static void main(String[] args) {
		try {
			numberOfAgents = Integer.parseInt(System.getProperty(keyNumberOfAgents));
		} catch (NumberFormatException e) {
			numberOfAgents = defaultNumberOfAgents;
		}
		loggingConfigurationLocation = System.getProperty(keyLoggingConfigurationLocation, defaultLoggingConfigurationLocation);
		repositoryLocation = System.getProperty(keyRepositoryLocation, defaultRepositoryLocation);
		agentLocation = System.getProperty(keyAgentLocation, defaultAgentLocation);
		resultsLocation = System.getProperty(keyResultsLocation, defaultResultsLocation);

		processes = new ArrayList<>();
		folders = new ArrayList<>();
		results = new Result[numberOfAgents];

		try {
			for (int i = 0; i < numberOfAgents; i++) {
				String agentName = "Agent" + i;
				String folder = resultsLocation + System.currentTimeMillis() + "-" + (int) (1000 * Math.random()) + "/";
				new File(folder).mkdirs();
				Process process = runAgent(agentName, folder);
				processes.add(process);
				folders.add(folder);
			}
			System.out.println("Waiting for processes to terminate.\n");

			for (int i = 0; i < numberOfAgents; i++) {
				final int index = i;
				final Process process = processes.get(i);
				Thread errorReader = new Thread(new Runnable() {
					@Override
					public void run() {
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						ArrayList<String> logs = new ArrayList<>();
						try {
							boolean serverConnection = true;
							String line;
							while (serverConnection && ((line = reader.readLine()) != null)) {
								// clear error stream buffer
								logs.add(line);
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						RunnerTools.writeToFile(folders.get(index) + fileNameErrorStream, logs);
					}
				});
				Thread inputReader = new Thread(new Runnable() {
					@Override
					public void run() {
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						ArrayList<String> logs = new ArrayList<>();
						try {
							boolean serverConnection = true, sendingResults = false;
							String line;
							while (serverConnection && ((line = reader.readLine()) != null)) {
								// clear input stream buffer
								logs.add(line);
								if (sendingResults) {
									Result result = Result.parse(line);
									setResult(index, result);
									sendingResults = false;
								} else if (line.contains(connectionToServerFailed)) {
									serverConnectionFailed(process);
									serverConnection = false;
									logs.add("Main: Stopped reading from stream");
								} else if (line.contains(startOfResults)) {
									sendingResults = true;
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						RunnerTools.writeToFile(folders.get(index) + fileNameInputStream, logs);
					}
				});
				inputReader.start();
				errorReader.start();
			}

			for (int i = 0; i < numberOfAgents; i++) {
				processes.get(i).waitFor();
				if (processes.get(i).exitValue() != 0) {
					System.out.println("A process terminated abnormally. For more information please read the log files at\n" + new File(folders.get(i)).getAbsolutePath());
				} else {
					System.out.println("Process " + i + " terminated successfully.");
				}
			}
			System.out.println("\nAll processes have terminated.");
			if (destroyedProcesses > 0) {
				System.err.println(destroyedProcesses + " out of " + numberOfAgents + " processes have been terminated because no connection to the CMR could be established.");
				System.err.println("Please make sure that the CMR is running.");
			} else {
				long averageDuration = 0;
				for (Result result : results) {
					averageDuration += result.getDuration();
				}
				averageDuration /= numberOfAgents;
				Result overallResult = new Result(averageDuration, 0, 0, 0, 0, 0);
				for (Result result : results) {
					overallResult.aggregate(result);
				}
				System.out.println("\n" + numberOfAgents + " agents have been running for an average " + overallResult.getDuration() + "ms");
				System.out.println("The type distribution of the calls is as follows:");
				System.out.println(overallResult.getDistribution());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void serverConnectionFailed(Process process) {
		process.destroy();
		destroyedProcesses++;
	}

	private static void setResult(int processId, Result result) {
		results[processId] = result;
	}

	private static Process runAgent(String agentName, String folderResults) throws IOException {
		String properties = "";
		Set<Object> keySet = System.getProperties().keySet();
		for (Object key : keySet) {
			if (((String) key).startsWith(prefix)) {
				properties += " -D" + key + "=" + System.getProperty((String) key);
			}
		}

		String command = "java -Xbootclasspath/p:" + agentLocation;
		command += " -javaagent:" + agentLocation;
		command += " -Dinspectit.repository=" + repositoryLocation;
		command += " -Dinspectit.agent.name=" + agentName;
		command += " -Dinspectit.logging.config=" + loggingConfigurationLocation;
		command += properties;
		command += " -D" + keyFileNameResults + "=" + folderResults + fileNameResults;
		command += " -jar " + defaultJARLocation;

		Process process = Runtime.getRuntime().exec(command);
		return process;
	}
}
