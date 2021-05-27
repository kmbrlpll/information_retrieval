package de.fuh.evaluation;

import de.fuh.evaluation.trec.TrecEvalRunner;
import de.fuh.evaluation.trec.TrecEvalUtilities;
import de.fuh.search.Search;
import de.fuh.utilities.Utilities;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Evaluation {

    private static Utilities ut = new Utilities();
    //Load properties from main/resources folder
    private static Properties prop = null;

    /**
     *
     * @param args
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public static void main(String args[]) throws IOException, ParseException, InterruptedException {
        Search search = new Search();
        Evaluation eval = new Evaluation();
        ArrayList<ScoreDoc[]> runs = new ArrayList<ScoreDoc[]>();

        String date = Long.toString(System.currentTimeMillis());

        TrecEvalUtilities tu = new TrecEvalUtilities();
        prop = ut.getProperties();

        File outputDir = new File(prop.get("trec").toString() + date) ;
        outputDir.mkdirs();

        String[] queries = prop.get("cds18topics").toString().split(", ");

        for (String query : queries) {

            // Choose a query in the configuration file
            ScoreDoc[] result = search.searchFiles(new File(prop.get("index").toString()), prop.get("field1").toString(), query, new StandardAnalyzer());

            runs.add(result);
        }

        tu.createQrelFile(runs, prop.get("trec").toString() + date, prop.get("runid").toString());

        String trecParams = tu.buildTrecParameters(prop.get("trecparams").toString(),
                new File(prop.get("qrel").toString()).getAbsolutePath() ,
                outputDir.getAbsolutePath()+ "/results.txt");

        TrecEvalRunner ter = new TrecEvalRunner(prop.get("trecbinary").toString(), trecParams, outputDir.getAbsolutePath() +"/");
        ter.runTrecEval();
    }
}
