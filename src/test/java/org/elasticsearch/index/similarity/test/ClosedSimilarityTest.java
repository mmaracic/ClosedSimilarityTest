package org.elasticsearch.index.similarity.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.event.ActionListener;
import org.elasticsearch.plugin.ClosedSimilarityTestPlugin;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.explain.ExplainRequest;
import org.elasticsearch.action.explain.ExplainResponse;
import org.elasticsearch.action.fieldstats.FieldStats;
import org.elasticsearch.action.fieldstats.FieldStatsRequest;
import org.elasticsearch.action.fieldstats.FieldStatsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import org.elasticsearch.plugin.AnalysisCroatianTestPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.test.ESIntegTestCase;
import static org.elasticsearch.test.ESIntegTestCase.client;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Marijo
 */
public class ClosedSimilarityTest extends ESIntegTestCase{
    
    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
      return Arrays.asList(ClosedSimilarityTestPlugin.class, AnalysisCroatianTestPlugin.class);
    }
    
//     @Test
//     public void testBm25Similarity() throws IOException
//     {
//        try {
//            client().admin().indices().prepareDelete("test").execute().actionGet();
//        } catch (Exception e) {
//            // ignore
//        }
//
//        client().admin().indices().prepareCreate("test")
//                .addMapping("type1", jsonBuilder().startObject()
//                        .startObject("type1")
//                            .startObject("properties")
//                                .startObject("field1")
//                                    .field("similarity", "custom")
//                                    .field("type", "text")
//                                .endObject()
//                                .startObject("field2")
//                                    .field("similarity", "classic")
//                                    .field("type", "text")
//                            .endObject()
//                        .endObject()
//                    .endObject().endObject())
//                .setSettings(Settings.builder()
//                        .put("index.number_of_shards", 1)
//                        .put("index.number_of_replicas", 0)
//                        .put("similarity.custom.type", "BM25")
//                        .put("similarity.custom.k1", 2.0f)
//                        .put("similarity.custom.b", 0.5f)
//                ).execute().actionGet();
//
//        client().prepareIndex("test", "type1", "1").setSource("field1", "the quick brown fox jumped over the lazy dog",
//                                                            "field2", "the quick brown fox jumped over the lazy dog")
//                .execute().actionGet();
//
//        SearchResponse bm25SearchResponse = client().prepareSearch().setQuery(matchQuery("field1", "quick brown fox")).execute().actionGet();
//        //assertThat(bm25SearchResponse.getHits().totalHits(), equalTo(1l));
//        float bm25Score = bm25SearchResponse.getHits().hits()[0].score();
//
//        SearchResponse defaultSearchResponse = client().prepareSearch().setQuery(matchQuery("field2", "quick brown fox")).execute().actionGet();
//        //assertThat(defaultSearchResponse.getHits().totalHits(), equalTo(1l));
//        float defaultScore = defaultSearchResponse.getHits().hits()[0].score();
//
//        assertThat(bm25Score, not(equalTo(defaultScore)));         
//     }
     
    
    public void fillIndex() throws IOException {
        try {
            client().admin().indices().prepareDelete("test").execute().actionGet();
        } catch (Exception e) {
            // ignore
        }

        client().admin().indices().prepareCreate("test")
                .addMapping("Address", jsonBuilder().startObject()
                        .startObject("Address")
                            .startObject("properties")
                                .startObject("streetNumber")
                                    .field("type", "text")
                                    .field("term_vector", "with_positions_offsets")
                                    .field("store", true)
                                .endObject()
                                .startObject("streetNumberAlfa")
                                    .field("type", "text")
                                    .field("term_vector", "with_positions_offsets")
                                    .field("store", true)
                                .endObject()
                                .startObject("streetName")
                                    .field("type", "text")
                                    .field("term_vector", "with_positions_offsets")
                                    .field("store", true)
                                .endObject()
                                .startObject("settlementName")
                                    .field("type", "text")
                                    .field("term_vector", "with_positions_offsets")
                                    .field("store", true)
                                .endObject()
                                .startObject("postalCode")
                                    .field("type", "integer")
                                    .field("store", true)
                                    .field("index", true)
                                .endObject()
                                .startObject("countyName")
                                    .field("type", "text")
                                    .field("term_vector", "with_positions_offsets")
                                    .field("store", true)
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject())
                .setSettings(Settings.builder()
                        .put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 0)
                        .put("similarity.default.type", "closed-similarity")
                        .put("similarity.default.attributeWeights.streetNumberAlfa", 1)
                        .put("similarity.default.attributeWeights.streetNumber", 2)
                        .put("similarity.default.attributeWeights.streetName", 4)
                        .put("similarity.default.attributeWeights.postalCode", 8)
                        .put("similarity.default.attributeWeights.settlementName", 16)
                        .put("similarity.default.attributeWeights.countyName", 32)
                        .put("similarity.default.attributeTypes.streetNumberAlfa", "String")
                        .put("similarity.default.attributeTypes.streetNumber", "String")
                        .put("similarity.default.attributeTypes.streetName", "String")
                        .put("similarity.default.attributeTypes.postalCode", "Integer")
                        .put("similarity.default.attributeTypes.settlementName", "String")
                        .put("similarity.default.attributeTypes.countyName", "String")

                        .put("analysis.tokenizer.FrontBackTokenizer.type", "croatian_backfront_tokenizer")
                        .put("analysis.tokenizer.FrontBackTokenizer.takeBack", 0)
                        .put("analysis.tokenizer.FrontBackTokenizer.takeFront", 3)
                        
                        .put("analysis.analyzer.cro_analyzer.type","custom")
                        .put("analysis.analyzer.cro_analyzer.tokenizer","FrontBackTokenizer")
                        .put("analysis.analyzer.cro_analyzer.filter","lowercase")
                        
                        .put("analysis.analyzer.text_number_analyzer.type","custom")
                        .put("analysis.analyzer.text_number_analyzer.tokenizer","croatian_number_tokenizer")
                ).execute().actionGet();
        
                Map<String, Object> attributeMap  = new HashMap<>();
                attributeMap.put("streetNumber", "21");
                attributeMap.put("streetNumberAlfa", null);
                attributeMap.put("countyName", "KANFANAR");
                attributeMap.put("streetName", "Ulica Dobriše Cesarića");
                attributeMap.put("settlementName", "Cista provo");
                attributeMap.put("postalCode", 52341);

                Map<String, Object> attributeMap2  = new HashMap<>();
                attributeMap.put("streetNumber", "22");
                attributeMap.put("streetNumberAlfa", null);
                attributeMap.put("countyName", "KANFANAR");
                attributeMap.put("streetName", "Ulica Dobriše Cesarića");
                attributeMap.put("settlementName", "Cista provo");
                attributeMap.put("postalCode", 52341);
        
        IndexResponse iResponse = client().prepareIndex("test", "Address", "1").setSource(attributeMap)
                .execute().actionGet();
        iResponse = client().prepareIndex("test", "Address", "2").setSource(attributeMap2)
                .execute().actionGet();
        
        System.out.println("\n\n Index response: "+iResponse.toString()+"\n\n");
    }
    
     @Test
     public void testClosedSimilarity() throws IOException
     {
        fillIndex();
         
        try {
            System.out.println("Waiting for the cluster to stabilize.");
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClosedSimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClusterHealthRequestBuilder healthRequest = client().admin().cluster().prepareHealth();
        
        String[] fields = {"postalCode"};
        FieldStatsRequest fsr = new FieldStatsRequest();
        fsr.setFields(fields);
        FieldStatsResponse fsrsp = client().fieldStats(fsr).actionGet();
        Map<String, FieldStats> fsm = fsrsp.getAllFieldStats();
        FieldStats fs =  fsm.get(fields[0]);
        String max = fs.getMaxValueAsString();
        String min = fs.getMinValueAsString();
        
        System.out.println("\n\n Field stats min: "+min+" max: "+max+" searchable: "+fs.isSearchable());

//        SearchResponse response = client().prepareSearch().setIndices("test").setTypes("Address").setQuery(matchAllQuery()).execute().actionGet();
//        System.out.println("\n\n All response: "+response.toString()+"\n\n");
//
        SearchResponse response2 = client().prepareSearch().setQuery(boolQuery().should(matchQuery("_all", "kanfanar cista provo ulica dobriše cesarića 21"))).execute().actionGet();
        System.out.println("\n\n Response2: "+response2.toString()+"\n\n");

        SearchResponse response3 = client().prepareSearch().setQuery(boolQuery()
                .should(matchQuery("countyName", "kanfanar"))
                .should(matchQuery("settlementName", "cista provo"))
                .should(matchQuery("streetName", "ulica dobriše cesarića"))
                .should(matchQuery("streetNumber", "21"))
        ).execute().actionGet();
        System.out.println("\n\n Response3: "+response3.toString()+"\n\n");
        
        ExplainResponse response4 = client().explain(new ExplainRequest("test","Address","1").query(boolQuery()
                .should(matchQuery("countyName", "kanfanar"))
                .should(matchQuery("settlementName", "cista provo"))
                .should(matchQuery("streetName", "ulica dobriše cesarića"))
                .should(matchQuery("streetNumber", "21"))
        )).actionGet();
        
        System.out.println("\n\n Response4: "+response4.getExplanation().toString()+"\n\n");
        ExplainResponse response5 = client().explain(new ExplainRequest("test","Address","2").query(boolQuery()
                .should(matchQuery("countyName", "kanfanar"))
                .should(matchQuery("settlementName", "cista provo"))
                .should(matchQuery("streetName", "ulica dobriše cesarića"))
                .should(matchQuery("streetNumber", "21"))
        )).actionGet();
        System.out.println("\n\n Response5: "+response5.getExplanation().toString()+"\n\n");
     }
     
//     @Test
//     public void testIndexing() throws IOException
//     {
//        fillIndex();
//         
//        try {
//            System.out.println("Waiting for the cluster to stabilize.");
//            Thread.sleep(5000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ClosedSimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        String[] fields = {"postalCode"};
//        FieldStatsRequest fsr = new FieldStatsRequest();
//        fsr.setFields(fields);
//        FieldStatsResponse fsrsp = client().fieldStats(fsr).actionGet();
//        Map<String, FieldStats> fsm = fsrsp.getAllFieldStats();
//        FieldStats fs =  fsm.get(fields[0]);
//        String max = fs.getMaxValueAsString();
//        String min = fs.getMinValueAsString();
//        
//        System.out.println("\n\n Field stats min: "+min+" max: "+max);
//    }     
}
