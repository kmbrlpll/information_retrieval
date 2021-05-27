package de.fuh.index.analyzer;

import de.fuh.utilities.Utilities;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Engel
 *
 * A simple class to play around with Lucene Analyzer
 *
 */
public class LuceneAnalyzer {

    String text = "";
    Analyzer ca = null;
    Properties prop = null;

    public LuceneAnalyzer() throws IOException {
        ca = CustomAnalyzer.builder()
                .withTokenizer("standard")
                .addTokenFilter("lowercase")
                .addTokenFilter("stop")
                .addTokenFilter("porterstem")
                .addTokenFilter("capitalization")
                //.addTokenFilter("ngram", "minGramSize", "1", "maxGramSize", "25")
                .build();
        Utilities ut = new Utilities();
        prop = ut.getProperties();
    }

    /**
     * A generic analyzer method that analyse a given text with the given Analyzer
     *
     * @param text
     * @return
     * @throws IOException
     */
    public List<String> analyze(String text) throws IOException {
        List<String> tokenList = new ArrayList<String>();
        TokenStream tokenStream = ca.tokenStream("content", text);
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();

        while(tokenStream.incrementToken()) {
            tokenList.add(attribute.toString());
        }
        return tokenList;
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main (String args[]) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        WhitespaceAnalyzer wa = new WhitespaceAnalyzer();

        LuceneAnalyzer la = new LuceneAnalyzer();
        List<String> list = la.analyze(la.prop.get("sample_text5").toString());
        System.out.println(list);
    }
}
