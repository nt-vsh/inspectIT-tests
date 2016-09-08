# User manual

To create the runnable jar files open your Eclipse installation. The jar files will later be able to run without Eclipse.

Download the code and import the ManualRegressionTest project into your workspace. 
Be sure to include the jar of the inspectIT agent in the project folder or always include the location in the VM arguments.

Run JVMRunner once in Eclipse to create a run configuration. 
Then select open the menu File > Export and in the dialog select Java > Runnable JAR file. 
Choose the JVMRunner launch configuration and set the export destination to ManualRegressionTest\runners.jar
or choose a different name and adjust the defaultJARLocation field in the Main class. Click Finish.

Run Main once in Eclipse to create a run configuration.
If you are running the engine with Eclipse you can set the properties in the VM arguments section of the run configuration.

If you want to run the engine from the console repeat the export step and choose a name for the jar.
Then run the jar on the console with the command `java -jar main.jar`.

In order to run the jar with a specific configuration include the options in the following format between java command and the -jar option:
`-Dit.agent.number=20`

The following options can be set via the command line:

## General settings
it.agent.number						The number of JVMs to start
it.agent.location					The location of the inspectIT
it.logging.configuration.location	The location of a logging configuration
it.repository.location				The location of the CMR
it.results.location					The location where to write results

## JVMRunner settings
it.thread.concurrent		Number of threads running concurrently
it.thread.time.execution	Execution time of a thread
it.thread.time.pause		Pause between executions

## Runner settings
it.runner.children		Total amount of calls per execution
it.weight.exception		Relative amount of exception calls
it.weight.http			Relative amount of HTTP calls
it.weight.logging		Relative amount of logging calls
it.weight.sql			Relative amount of SQL calls
it.weight.timer			Relative amount of timer calls
it.exception.cause		Throw an exception with a cause
it.exception.pass		Call a method that catches and passes exceptions
it.http.get				Make an HTTP get request
it.http.https			Make an HTTPS request
it.sql.bindParameters	Execute an SQL statement with bound parameters
it.sql.preparedStmt		Execute a prepared SQL statement.
it.timer.charting		Use a timer with charting
