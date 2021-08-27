package com.vector.svg2vectorandroid;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

public class Runner {

    public static void main(String[] args){
        CommandLine commandLine;
        Option option_S = Option.builder("s")
                .required(true)
                .hasArg()
                .desc("Source directory of svg files")
                .longOpt("src")
                .build();
        Option option_O = Option.builder("o")
                .required(false)
                .hasArg()
                .desc("Output directory for VectorDrawable files")
                .longOpt("output")
                .build();
        Option option_E = Option.builder("e")
                .required(false)
                .hasArg()
                .desc("Extension for VectorDrawable files")
                .longOpt("extension")
                .build();
        Option option_P = Option.builder("p")
                .required(false)
                .hasArg()
                .desc("Prefix to add to files")
                .longOpt("prefix")
                .build();
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(option_S);
        options.addOption(option_O);
        options.addOption(option_E);
        options.addOption(option_P);

        try
        {
            commandLine = parser.parse(options, args);

            SvgFilesProcessor processor = new SvgFilesProcessor(commandLine.getOptionValue("s"), commandLine.getOptionValue("o"), commandLine.getOptionValue("e"), commandLine.getOptionValue("p"));
            processor.process();
        }
        catch (ParseException pe) {
            System.out.println("Error parsing command-line arguments!");
            System.out.println("Please, follow the instructions below:");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "SVG2VectorDrawable", options );
            System.exit(1);
        }
    }
}
