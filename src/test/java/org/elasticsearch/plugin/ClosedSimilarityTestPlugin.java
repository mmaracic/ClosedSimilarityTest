/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.plugin;

import org.elasticsearch.index.IndexModule;
import org.elasticsearch.index.similarity.ClosedSimilarityProvider;
import org.elasticsearch.plugins.Plugin;

/**
 *
 * @author Marijo
 */
public class ClosedSimilarityTestPlugin extends Plugin {
    @Override
    public void onIndexModule(IndexModule indexModule) {
        indexModule.addSimilarity("closed-similarity", ClosedSimilarityProvider::new);
}    
}
