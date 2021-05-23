package de.fuh.evaluation.trec;

import java.io.*;

/**
 * Executes the external trec_eval program
 * 
 * @author mklein
 */
public class TrecEvalRunner {

	private String pathToProgram;
	private String params;
	private String resultsDir;
	public String output = "";
	public int exitCode;

	/**
	 * Constructor
	 * 
	 * @param pathToProgram		Full path to the external trec_eval program
	 * @param params			Parameters to be used with trec_eval
	 * @param resultsDir		Directory to save the results file to
	 */
	public TrecEvalRunner(String pathToProgram, String params, String resultsDir) {
		this.pathToProgram = pathToProgram;
		this.params = params;
		this.resultsDir = resultsDir;
	}

	/**
	 * Runs the TrecEvalRunner after it has been instantiated with the necessary parameters
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void runTrecEval() throws IOException, InterruptedException {

		File trecExecutable = new File(pathToProgram);

//		System.out.println("pathToProgram : " + pathToProgram);
//		System.out.println("exists        : " + trecExecutable.exists());
//		System.out.println("params        : " + params);
//		System.out.println("resultsDir    : " + resultsDir);

		if (trecExecutable.exists()) {
			
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(pathToProgram + " " + params);
			
			BufferedReader stdOutput = new BufferedReader(new InputStreamReader(p.getInputStream())); // InputStream == stdout of the process
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream())); // ErrorStream == stderr of the process
			
			while (p.isAlive() || stdOutput.ready() || stdError.ready()) {
				
				// Read Output Stream
				if (stdOutput.ready()) {
					output += stdOutput.readLine() + "\n";
				}
				
				// Read Error Stream
				if (stdError.ready()) {
					output += stdError.readLine() + "\n";
				}
				
			}
			
			exitCode = p.exitValue();

		} else {
			
			exitCode = 999;
			System.out.println("ERROR: trec_eval executable not found");
			
		}
		
		// Write output to file
		System.out.println(resultsDir + "trec-results.txt");
		FileWriter fw = new FileWriter(resultsDir + "trec-results.txt");
		fw.write(output);
		fw.close();
		
	}

}
