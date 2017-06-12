/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexSettings;

/**
 *
 * @author marijom
 */
public class NumberTokenizerFactoryMockup extends AbstractTokenizerFactory{
    @Inject
    public NumberTokenizerFactoryMockup(IndexSettings indexSettings, String name, Settings settings) {
        super(indexSettings, name, settings);
    }

    @Override
    public Tokenizer create() {
        return new NumberTokenizer();
    }    
}
