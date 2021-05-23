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

    /**
     * @param args
     * @throws IOException
     */
    public static void main (String args[]) throws IOException {
        Utilities ut = new Utilities();
        Properties prop = ut.getProperties();

        LuceneAnalyzer la = new LuceneAnalyzer();
        Analyzer analyzer = new StandardAnalyzer();
        WhitespaceAnalyzer wa = new WhitespaceAnalyzer();

        Analyzer ca = CustomAnalyzer.builder()
                .withTokenizer("standard")
                .addTokenFilter("lowercase")
                .addTokenFilter("stop")
               // .addTokenFilter("ngram", "minGramSize", "1", "maxGramSize", "25")
                .build();

        List<String> list = la.analyze(prop.get("sample_text5").toString(), ca);
        System.out.println(list);
    }

    /**
     * A generic analyzer method that analyse a given text with the given Analyzer
     *
     * @param text
     * @param analyzer
     * @return
     * @throws IOException
     */
    public List<String> analyze(String text, Analyzer analyzer) throws IOException {
        List<String> tokenList = new ArrayList<String>();

        TokenStream tokenStream = analyzer.tokenStream("content", text);
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();

        while(tokenStream.incrementToken()) {
            tokenList.add(attribute.toString());
        }
        return tokenList;
    }
}
