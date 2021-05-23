package de.fuh.evaluation.trec;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Outsourced TrecEval-specific functions
 * 
 * @author mklein
 * @author Engel
 */
public class TrecEvalUtilities {

	/**
	 * Creates a QREL-styled file out of the results
	 *
	 * @param rankedDocMap
	 * @param resultsDir
	 * @param runId
	 * @throws IOException
	 */
	public void createQrelFile( ArrayList<ScoreDoc[]> rankedDocMap, String resultsDir, String runId) throws IOException {

		int lines;
		String queryNr;
		Directory index = FSDirectory.open(Paths.get("src/main/resources/index"));
		IndexSearcher searcher;
		IndexReader reader = DirectoryReader.open(index);
			searcher = new IndexSearcher(reader);

		File dir = new File(resultsDir);
		dir.mkdirs();

		File file = new File(dir.getAbsolutePath() + "/results.txt");
		file.createNewFile();

		int cnt = 0;
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file), 131072)) { // 128 kb buffer
			lines = 0;
			// Iterate over the sorted List of <docId, sim> Map entries for current Query
			for (ScoreDoc[] dm : rankedDocMap) {
				for (ScoreDoc sd : dm) {

					Document doc = searcher.doc(sd.doc);
					System.out.println("OUR" + doc.get("id"));

					lines++;
					// Compose line for output file
					StringJoiner sj = new StringJoiner("\t");
					sj.add(Integer.toString(cnt));
					sj.add("0");
					//sj.add(String.valueOf(sd.doc));
					sj.add(String.valueOf(doc.get("id")));
					sj.add(Integer.toString(lines));
					sj.add(Double.toString(sd.score));
					sj.add(runId);

					bw.write(sj.toString() + "\n");
				}
				cnt ++;
			}
		}
	}

	/**
	 * Builds the complete parameter String for Trec Eval
	 *
	 * @param trecParams	Parameters (excluding the file paths)
	 * @param qrelsFile		Path to the QRELs file
	 * @param outputDir		Path to the output diretory
	 * @return				Full parameter string
	 */
	public static String buildTrecParameters(String trecParams, String qrelsFile, String outputDir) {

		StringJoiner sj = new StringJoiner(" ");
		sj.add(trecParams);
		sj.add( qrelsFile);
		sj.add(outputDir );

		return sj.toString();
	}
}
