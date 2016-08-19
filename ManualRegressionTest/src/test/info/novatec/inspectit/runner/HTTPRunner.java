package test.info.novatec.inspectit.runner;

import test.info.novatec.inspectit.http.HttpFeatures;

/**
 * @author Valentin Schraub
 *
 */
public class HTTPRunner {

	private boolean getRequest;
	private boolean https;

	private HttpFeatures httpFeatures;

	public HTTPRunner() {
		httpFeatures = new HttpFeatures();
		httpFeatures.init();

		getRequest = Configuration.getRequest();
		https = Configuration.https();
	}

	public void run() {
		try {
			if (getRequest) {
				if (https) {
					httpFeatures.httpsGetParameters();
				} else {
					httpFeatures.httpGetParameters();
				}
			} else {
				if (https) {
					httpFeatures.httpsPostParameters();
				} else {
					httpFeatures.httpPostParameters();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
