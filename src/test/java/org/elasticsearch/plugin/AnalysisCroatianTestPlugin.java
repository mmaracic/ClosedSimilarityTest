/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.plugin;

import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.BackFrontTokenizerFactoryMockup;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.NumberTokenizerFactoryMockup;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

/**
 *
 * @author Marijo
 */
public class AnalysisCroatianTestPlugin extends Plugin implements AnalysisPlugin{
    
    /**
     * Override to add additional {@link Tokenizer}s.
     * @return 
     */
    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> tokenizers = new HashMap<>();        
        tokenizers.put("croatian_backfront_tokenizer", (AnalysisModule.AnalysisProvider<TokenizerFactory>) (IndexSettings indexSettings, Environment environment, String name, Settings settings) -> {
            return new BackFrontTokenizerFactoryMockup(indexSettings, name, settings);
        });
        tokenizers.put("croatian_number_tokenizer", (AnalysisModule.AnalysisProvider<TokenizerFactory>) (IndexSettings indexSettings, Environment environment, String name, Settings settings) -> {
            return new NumberTokenizerFactoryMockup(indexSettings, name, settings);
        });
        return tokenizers;
    }    
}
