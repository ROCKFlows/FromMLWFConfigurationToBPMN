package com.ml2wf;

import com.ml2wf.cmd.Generate;
import com.ml2wf.cmd.Merge;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "ml2wf", version = "1.0", sortOptions = false, usageHelpWidth = 60, header = "\n ----  Machine Learning problem to Workflow  ---- \n\n", footer = "\n\n  ---- Provided by AUTHOR ---- \n", description = "")
public class App implements Runnable {

	public static void main(String[] args) {
		CommandLine commandLine = new CommandLine(new App())
				.addSubcommand("-g", new Generate())
				.addSubcommand("-m", new Merge());
		commandLine.execute(args);
		if (commandLine.isUsageHelpRequested()) {
			commandLine.usage(System.out);
		}
	}

	@Override
	public void run() {
		// TODO: to complete
	}
}
