package com.lassilaiho.calculator.ui;

import org.apache.commons.cli.*;

/**
 * Parses command line arguments.
 */
public final class ArgumentSet {
    private static Options options = new Options();
    private static DefaultParser parser = new DefaultParser();
    private static HelpFormatter helpFormatter = new HelpFormatter();

    private boolean help;
    private String defaultSessionFile;

    public boolean getHelp() {
        return help;
    }

    public String getDefaultSessionFile() {
        return defaultSessionFile;
    }

    static {
        options.addOption(
            Option.builder().longOpt("default-session").hasArg().type(String.class)
                .desc("path to the default session file").argName("session file")
                .numberOfArgs(1).build());
        options.addOption(
            Option.builder("h").longOpt("help").desc("display usage information")
                .build());
    }

    private ArgumentSet(CommandLine commandLine) {
        help = commandLine.hasOption("help");
        defaultSessionFile =
            commandLine.getOptionValue("default-session", "default.session");
    }

    /**
     * Prints help text to standard output.
     */
    public void printHelp() {
        helpFormatter.printHelp("calculator [options]", options);
    }

    /**
     * Parses args into an {@link ArgumentSet}.
     * 
     * @param  args           the arguments to parse
     * @return                a new {@link ArgumentSet} matching args
     * @throws ParseException thrown if the parsing fails
     */
    public static ArgumentSet parse(String[] args) throws ParseException {
        return new ArgumentSet(parser.parse(options, args));
    }
}
