package test.info.novatec.inspectit.runner;

import java.util.ArrayList;
import java.util.List;

import test.info.novatec.inspectit.runner.tools.RunnerTools;

/**
 * @author Valentin Schraub
 *
 */
public class JVMRunner {
	private static int numberOfConcurrentThreads;
	private static int threadExecutionTime;
	private static int threadPause;

	private static List<Thread> threads;

	private static String fileNameResults;

	public static void main(String[] args) {
		threads = new ArrayList<>();

		numberOfConcurrentThreads = Configuration.numberOfConcurrentThreads();
		threadExecutionTime = Configuration.threadExecutionTime();
		threadPause = Configuration.threadPause();
		Configuration.getWeights();
		fileNameResults = Configuration.fileNameResults();

		for (int i = 0; i < numberOfConcurrentThreads; i++) {
			Thread thread = new Thread() {
				Runner runner = new Runner();

				@Override
				public void run() {
					runner.init();
					long timeStart = System.currentTimeMillis();
					while ((System.currentTimeMillis() - timeStart) < threadExecutionTime) {
						runner.execute();
						try {
							Thread.sleep(threadPause);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			thread.start();
			threads.add(thread);
		}
		for (int i = 0; i < numberOfConcurrentThreads; i++) {
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		writeResults();
	}

	private static void writeResults() {
		ArrayList<String> results = new ArrayList<>();
		RunnerTools.writeToFile(fileNameResults, results);
	}
}
