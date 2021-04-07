package com.ml2wf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ml2wf.cmd.Build;
import com.ml2wf.cmd.Generate;
import com.ml2wf.cmd.Merge;
import com.ml2wf.cmd.Save;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "ml2wf", version = "1.0", sortOptions = false, usageHelpWidth = 60, header = "\n ----  Machine Learning problem to Workflow  ---- \n\n", footer = "\n\n  ---- Provided by AUTHOR ---- \n", description = "")
public class App {

	@Spec
	Model.CommandSpec spec;

	@Option(names = { "-v",
			"--version" }, versionHelp = true, arity = "0", order = 1, description = "Displays version info")
	boolean version;

	private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) {
		// TODO: check windows console colors support
		/*-AnsiConsole.systemInstall();
		System.clearProperty("log4j2.skipJansi");
		System.setProperty("log4j2.skipJansi", "false");
		logger.fatal("@|red,bold Fatal|@");*/
		CommandLine commandLine = new CommandLine(new App())
				.addSubcommand("generate", new Generate())
				.addSubcommand("save", new Save())
				.addSubcommand("build", new Build())
				.addSubcommand("merge", new Merge());
		commandLine.execute(args);
		if (commandLine.isUsageHelpRequested()) {
			logger.info(commandLine.getUsageMessage());
		}
	}
}
