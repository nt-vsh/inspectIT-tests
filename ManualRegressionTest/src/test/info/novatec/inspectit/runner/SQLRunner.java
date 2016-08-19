package test.info.novatec.inspectit.runner;

import java.sql.Connection;
import java.sql.SQLException;

import test.info.novatec.inspectit.sql.SQLFeatures;
import test.info.novatec.inspectit.sql.tools.ConnectionFactory;

/**
 * @author Valentin Schraub
 *
 */
public class SQLRunner {

	private boolean bindParameters;
	private boolean preparedStatement;

	private SQLFeatures sqlFeatures;
	private Connection connection;

	public SQLRunner() {
		sqlFeatures = new SQLFeatures();
		connection = ConnectionFactory.getRandom();

		bindParameters = Configuration.bindParameters();
		preparedStatement = Configuration.preparedStatement();
	}

	public void run() {
		try {
			if (bindParameters) {
				sqlFeatures.preparedStatementWithBindParameters(connection);
			} else if (preparedStatement) {
				sqlFeatures.preparedStatement(connection);
			} else {
				sqlFeatures.unpreparedStatement(connection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
