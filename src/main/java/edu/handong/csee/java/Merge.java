package edu.handong.csee.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
//import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

import edu.handong.csee.java.analyze.AnalyzeFile;
import edu.handong.csee.java.util.NotEnoughArgumentException;


public class Merge{
	private String readpath;
	private String writepath;
	private boolean help;
	
	private Thread[] threadSummary;
	private Thread[] threadChart;
	private int countThreadSummary;
	private int countThreadChart;
	
	public Merge() {
		countThreadSummary = 0;
		countThreadChart = 0;
	}
	
	public void runMerge(String[] args) {
		
		Options options = createOptions();
		
		if(parseOptions(options,args)) {
			if(help) {
				printHelp(options);
				return;
			}
			
			try {
				// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
				if(args.length<2)
					throw new NotEnoughArgumentException();
			} catch (NotEnoughArgumentException e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
			
		
			
		}
		
		
		
		ZipFile zipFile;
		Charset euc = Charset.forName("utf-8");
		
		try {
			zipFile = new ZipFile(readpath);
			int numberOfZipFiles = 0;
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while(entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			numberOfZipFiles++;
			System.out.println(entry.getName());
		}
		
		System.out.println(numberOfZipFiles);
		threadSummary = new Thread[numberOfZipFiles];
		threadChart = new Thread[numberOfZipFiles];
		
		entries = zipFile.entries();
		
		while(entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			
			
			if(entry.getName().contains("000")) { 
			try {
				File tempFile = File.createTempFile("tempFile", "zip");
				FileOutputStream tempOut = new FileOutputStream(tempFile);
				IOUtils.copy(zipFile.getInputStream(new ZipEntry(entry.getName())),tempOut);
				ZipFile zipFileSub = new ZipFile(tempFile);
			
				Enumeration<? extends ZipEntry> entriesSub = zipFileSub.entries();
					
				while(entriesSub.hasMoreElements()) {
					ZipEntry eachEntry = (ZipEntry)entriesSub.nextElement();
					System.out.println(eachEntry.getName());
					if(eachEntry.getName().contains("요약")) {
						InputStream stream = zipFileSub.getInputStream(eachEntry);
						threadSummary[countThreadSummary] = new Thread(new AnalyzeFile<String>("Summary"+writepath,stream,entry.getName()));
						threadSummary[countThreadSummary++].start();
					}
					else {
						InputStream stream = zipFileSub.getInputStream(eachEntry);
						threadChart[countThreadChart] = new Thread(new AnalyzeFile<String>("Chart"+writepath,stream,entry.getName()));
						threadChart[countThreadChart++].start();
					
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			else
				continue;
				
		}
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i=0; i<countThreadSummary; i++) {
			try {
				threadSummary[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				threadChart[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);
			
			readpath = cmd.getOptionValue("i");
			writepath = cmd.getOptionValue("o");
			help = cmd.hasOption("h");

			
		} catch (Exception e) {
			printHelp(options);
			return(false);
			
		}

		return true;
	}
	
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());
		
	
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());

		options.addOption(Option.builder("h").longOpt("help")
		        .desc("Help")
		        .build());
		
		return options;
	}
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "JavaFinalProject";
		String footer = "";
		formatter.printHelp("JavaFinalProject", header, options, footer, true);
	}

}

