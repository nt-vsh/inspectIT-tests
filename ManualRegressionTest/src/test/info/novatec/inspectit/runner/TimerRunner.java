package test.info.novatec.inspectit.runner;

import test.info.novatec.inspectit.timer.Timer;

/**
 * @author Valentin Schraub
 *
 */
public class TimerRunner {

	private boolean withCharting;

	private Timer timer;

	public TimerRunner() {
		timer = new Timer();
		timer.init();

		withCharting = Configuration.withCharting();
	}

	public void run() {
		if (withCharting) {
			timer.shortTimerWithCharting();
		} else {
			timer.shortTimerWithoutCharting();
		}
	}
}
