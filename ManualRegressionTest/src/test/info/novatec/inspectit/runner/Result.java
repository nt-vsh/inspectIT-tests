package test.info.novatec.inspectit.runner;

/**
 * @author Valentin Schraub
 *
 */
public class Result {
	private static final String separator = ",";

	private long duration;
	private int counterException;
	private int counterHTTP;
	private int counterLogging;
	private int counterSQL;
	private int counterTimer;

	private Result() {

	}

	public Result(long duration, int counterException, int counterHTTP, int counterLogging, int counterSQL, int counterTimer) {
		this.duration = duration;
		this.counterException = counterException;
		this.counterHTTP = counterHTTP;
		this.counterLogging = counterLogging;
		this.counterSQL = counterSQL;
		this.counterTimer = counterTimer;
	}

	public long getDuration() {
		return duration;
	}

	public int getCounterException() {
		return counterException;
	}

	public int getCounterHTTP() {
		return counterHTTP;
	}

	public int getCounterLogging() {
		return counterLogging;
	}

	public int getCounterSQL() {
		return counterSQL;
	}

	public int getCounterTimer() {
		return counterTimer;
	}

	public void aggregate(Result other) {
		this.counterException += other.counterException;
		this.counterHTTP += other.counterHTTP;
		this.counterLogging += other.counterLogging;
		this.counterSQL += other.counterSQL;
		this.counterTimer += other.counterTimer;
	}

	public String getDistribution() {
		String distribution = "Exception: " + counterException + "\n";
		distribution += "HTTP: " + counterHTTP + "\n";
		distribution += "Logging: " + counterLogging + "\n";
		distribution += "SQL: " + counterSQL + "\n";
		distribution += "Timer: " + counterTimer + "\n";
		return distribution;
	}

	public String getDistributionWithId(int executionId) {
		String distribution = "Execution #" + executionId + "\n";
		distribution += "Duration: " + duration + "ms\n";
		distribution += getDistribution();
		return distribution;
	}

	public static Result parse(String value) {
		String[] split = value.split(",");
		int index = 0;
		Result result = new Result();
		result.duration = Long.parseLong(split[index++]);
		result.counterException = Integer.parseInt(split[index++]);
		result.counterHTTP = Integer.parseInt(split[index++]);
		result.counterLogging = Integer.parseInt(split[index++]);
		result.counterSQL = Integer.parseInt(split[index++]);
		result.counterTimer = Integer.parseInt(split[index++]);
		return result;
	}

	@Override
	public String toString() {
		String toString = duration + separator;
		toString += counterException + separator;
		toString += counterHTTP + separator;
		toString += counterLogging + separator;
		toString += counterSQL + separator;
		toString += counterTimer;
		return toString;
	}
}
