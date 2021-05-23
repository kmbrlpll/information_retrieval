package de.fuh.search;


import de.fuh.similarity.SimilarityCalculation;
import de.fuh.utilities.Utilities;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Engel
 *
 * A simple class to search
 */
public class Search {

    private static Utilities ut = new Utilities();
    //Load properties from main/resources folder
    private static Properties prop = null;

    public Search() throws IOException {
        prop = ut.getProperties();
    }

    /**
     *
     * @param args
     * @throws IOException
     * @throws ParseException
     */
    public static void main (String args[]) throws IOException, ParseException {

        Search search = new Search();


        Analyzer analyzer = new StandardAnalyzer();;
        // Choose a query in the configuration file
        ScoreDoc[] result = search.searchFiles(new File(prop.get("index").toString()), prop.get("field1").toString(), prop.get("query").toString(), analyzer);

        for (ScoreDoc strTemp : result){
            System.out.println(strTemp);
        }
    }

    /**
     *
     * @param indexDir
     * @param field
     * @param query
     * @param analyzer
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public ScoreDoc[] searchFiles(File indexDir, String field, String query, Analyzer analyzer) throws ParseException, IOException {

        Query q = new QueryParser(field, analyzer).parse(query);
        Directory directory = FSDirectory.open(indexDir.toPath());
        IndexReader indexReader = DirectoryReader
                .open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        // Choose a similarity measure from the configuration file
        // compare de.fuh.similarity.SimilarityCalculation() which measures are available
        searcher.setSimilarity(new SimilarityCalculation().getSim().get(prop.get("similarity").toString()));
        TopDocs result = searcher.search(q, 10);

        return result.scoreDocs;
    }
}
