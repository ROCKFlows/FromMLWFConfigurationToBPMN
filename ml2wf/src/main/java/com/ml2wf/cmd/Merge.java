package com.ml2wf.cmd;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.ml2wf.merge.FeatureModelMerger;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "-m", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "import a worklow in a FeatureModel")
public class Merge implements Runnable {

	@Option(names = { "-i", "--input" }, arity = "1", order = 1, description = "input file")
	String input;

	@Option(names = { "-o", "--output" }, arity = "1", order = 1, description = "output file")
	String output;

	@Option(names = { "-v", "--verbose" }, arity = "0", order = 1, description = "verbose mode")
	boolean verbose;

	@Option(names = { "-b",
			"--backup" }, arity = "0", order = 1, description = "backup the original FeatureModel file before any modification")
	boolean backUp;

	@Override
	public void run() {
		FeatureModelMerger merger;
		try {
			merger = new FeatureModelMerger(this.output);
			merger.mergeWithWF(this.input, this.backUp);
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			// TODO: log the crash
		}
	}
}
