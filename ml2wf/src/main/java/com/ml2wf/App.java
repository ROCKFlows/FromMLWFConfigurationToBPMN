package com.ml2wf;

import com.ml2wf.cmd.Generate;
import com.ml2wf.cmd.Merge;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model;
import picocli.CommandLine.Spec;

@Command(name = "ml2wf", version = "1.0", sortOptions = false, usageHelpWidth = 60, header = "\n ----  Machine Learning problem to Workflow  ---- \n\n", footer = "\n\n  ---- Provided by AUTHOR ---- \n", description = "")
public class App implements Runnable {

	@Spec
	Model.CommandSpec spec;

	public static void main(String[] args) {
		CommandLine commandLine = new CommandLine(new App())
				.addSubcommand("-g", new Generate())
				.addSubcommand("-m", new Merge());
		commandLine.execute(args);
		if (commandLine.isUsageHelpRequested()) {
			commandLine.usage(System.out); // TODO: replace System.out by logger
		}
	}

	@Override
	public void run() {
		CommandLine.usage(this.spec, System.out); // TODO: replace System.out by logger
	}
}
