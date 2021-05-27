package de.fuh.index;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import de.fuh.utilities.Utilities;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author Engel
 *
 *
 */
public class Index {

    private static Utilities ut = null;
    private static Properties prop = null;

    /**
     *  Run the application
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Index indexer = new Index();
        ut = new Utilities();
        //Load properties from main/resources folder
        prop = ut.getProperties();
        File dataDir = new File(prop.get("data").toString());
        File indexDir  = new File(prop.get("index").toString());
        // initiate indexing
        indexer.index(indexDir, dataDir, "txt");
    }

    /**
     * Index a text collection
     *
     * @param indexDir
     * @param dataDir
     * @param suffix
     */

    public void index(File indexDir, File dataDir, String suffix) {
        try {
            System.out.println("Indexing to " + indexDir);
            // The index is stored in the filesystem
            Directory dir = FSDirectory.open(Paths.get(indexDir.getAbsolutePath()));
            // A standard analyser is used to evaluate the text
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE);

            IndexWriter writer = new IndexWriter(dir, iwc);
            //Triggers the indexing process of a collection with directories
            indexDirectory(writer, dataDir, suffix);

            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Traverse directory structure
     * @param indexWriter
     * @param dataDir
     * @param suffix
     * @throws IOException
     */
    private void indexDirectory(IndexWriter indexWriter, File dataDir, String suffix) throws IOException {
        File[] files = dataDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                indexDirectory(indexWriter, f, suffix);
            } else {
                indexFileWithIndexWriter(indexWriter, f, suffix);
            }
        }
    }

    /**
     * Indexing files
     *  @param indexWriter
     * @param file
     * @param suffix
     * @throws IOException
     */
    private void indexFileWithIndexWriter(IndexWriter indexWriter, File file, String suffix) throws IOException {
        if (file.isHidden() || file.isDirectory() || !file.canRead() || !file.exists()) {
            return;
        }

        if (suffix != null && !file.getName().endsWith(suffix)) {
            return;
        }

        try (InputStream stream = Files.newInputStream(file.toPath())) {
            Document doc = new Document();

            doc.add(new TextField(prop.get("field1").toString(), new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
            doc.add(new StringField(prop.get("field2").toString(), file.toString(), Field.Store.YES));
            doc.add(new StringField(prop.get("field3").toString(), file.getName().replace(".txt", ""), Field.Store.YES));

            if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) {
                indexWriter.addDocument(doc);
            } else {
                indexWriter.updateDocument(new Term("path", file.toString()), doc);
            }
        }
    }
}