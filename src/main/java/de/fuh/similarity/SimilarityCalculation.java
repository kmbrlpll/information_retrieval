package de.fuh.similarity;

import org.apache.lucene.search.similarities.*;

import java.util.HashMap;


/**
 * A class that provides instances for similarity calculations
 *
 * @author Engel
 */
public class SimilarityCalculation{

    private HashMap<String, Similarity> sim = null;
    private ClassicSimilarity cs = null;
    private BM25Similarity bm25 = null;
    private LMDirichletSimilarity diri = null;
    private BooleanSimilarity bool = null;
    private LMJelinekMercerSimilarity lmj = null;
    private AxiomaticF3LOG ax = null;
    private DFISimilarity dfi = null;
    private IBSimilarity ibs = null;

    public SimilarityCalculation(){
        this.sim = new HashMap<String, Similarity>();
        sim.put("cs", this.cs = new ClassicSimilarity());
        sim.put("bm25",this.bm25 = new BM25Similarity());
        sim.put("diri",this.diri = new LMDirichletSimilarity());
        sim.put("bool",this.bool = new BooleanSimilarity());
        sim.put("lmj",this.lmj = new LMJelinekMercerSimilarity(0.2F));
        sim.put("ax",this.ax = new AxiomaticF3LOG(0.5F,10));
        sim.put("dfi",this.dfi = new  DFISimilarity(new IndependenceChiSquared()));
        sim.put("ibs",this.ibs = new IBSimilarity(new DistributionLL(), new LambdaDF(), new NormalizationH3()));
     }

    public HashMap<String, Similarity> getSim() {
        return sim;
    }

    public ClassicSimilarity getCs() {
        return cs;
    }

    public BM25Similarity getBm25() {
        return bm25;
    }

    public LMDirichletSimilarity getDiri() {
        return diri;
    }

    public BooleanSimilarity getBool() {
        return bool;
    }

    public LMJelinekMercerSimilarity getLmj() {
        return lmj;
    }

    public AxiomaticF3LOG getAx() {
        return ax;
    }

    public DFISimilarity getDfi() {
        return dfi;
    }

    public IBSimilarity getIbs() {
        return ibs;
    }
}
