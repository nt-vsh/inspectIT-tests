package test.info.novatec.inspectit.runner;

import java.util.ArrayList;
import java.util.List;

import test.info.novatec.inspectit.runner.tools.RunnerTools;

/**
 * @author Valentin Schraub
 *
 */
public class JVMRunner {
	private int numberOfConcurrentThreads;
	private int threadExecutionTime;
	private int threadPause;

	private List<Thread> threads;

	private long duration;
	private List<Result> results;
	private String fileNameResults;

	public static void main(String[] args) {
		JVMRunner jvmRunner = new JVMRunner();
		jvmRunner.run();
	}

	public JVMRunner() {
		threads = new ArrayList<>();
		results = new ArrayList<>();

		numberOfConcurrentThreads = Configuration.numberOfConcurrentThreads();
		threadExecutionTime = Configuration.threadExecutionTime();
		threadPause = Configuration.threadPause();
		Configuration.getWeights();
		fileNameResults = Configuration.fileNameResults();
	}

	private void run() {
		long systemTimeStart = System.currentTimeMillis();
		for (int i = 0; i < numberOfConcurrentThreads; i++) {
			Thread thread = new Thread() {
				Runner runner = new Runner(JVMRunner.this);

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
		duration = System.currentTimeMillis() - systemTimeStart;
		writeResults();
	}

	public void addResult(Result result) {
		results.add(result);
	}

	private void writeResults() {
		ArrayList<String> list = new ArrayList<>();
		list.add(numberOfConcurrentThreads + " concurrent runners performed " + results.size() + " executions.");
		Result jvmResult = new Result(duration, 0, 0, 0, 0, 0);
		for (int i = 0; i < results.size(); i++) {
			Result result = results.get(i);
			jvmResult.aggregate(result);
			list.add(result.getDistributionWithId(i));
		}

		System.out.println(Main.startOfResults);
		System.out.println(jvmResult.toString());

		list.add(1, "Total execution time: " + duration + "ms");
		list.add(2, "The type distribution of the calls is as follows:");
		list.add(3, jvmResult.getDistribution());
		RunnerTools.writeToFile(fileNameResults, list);
	}
}
