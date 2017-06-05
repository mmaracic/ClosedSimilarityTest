package org.elasticsearch.index.analysis.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.elasticsearch.plugin.ClosedSimilarityTestPlugin;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.search.SearchHit;
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
      return Arrays.asList(ClosedSimilarityTestPlugin.class);
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
     
     @Test
     public void testClosedSimilarity() throws IOException
     {
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
                ).execute().actionGet();

        client().prepareIndex("test", "Address").setSource("streetNumber", "21",
                                                            "streetNumberAlfa", null,
                                                            "countyName", "KANFANAR",
                                                            "streetName", "Ulica Dobriše Cesarića",
                                                            "settlementName", "Cista provo",
                                                            "postalCode", 52341)
                .execute().actionGet();

        SearchResponse response = client().prepareSearch().setQuery(matchQuery("_all", "kanfanar cista provo 52341 ulica dobriše cesarića 21")).execute().actionGet();
        System.out.println("\n\n Response: "+response.toString()+"\n\n");

        SearchResponse response2 = client().prepareSearch().setQuery(matchQuery("_all", "kanfanar cista provo 52341 ulica dobriše cesarića 21")).execute().actionGet();
        System.out.println("\n\n Response: "+response2.toString()+"\n\n");
     }     
}