/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.apache.log4j.Logger;
import org.elasticsearch.index.IndexSettings;

/**
 *
 * @author marijom
 */
public class BackFrontTokenizerFactoryMockup extends AbstractTokenizerFactory{
    
    private static Logger log = Logger.getLogger(BackFrontTokenizerFactory.class);

    protected int takeBack=0;
    protected int takeFront=0;
    
    @Inject
    public BackFrontTokenizerFactoryMockup(IndexSettings indexSettings, String name, Settings settings) {
        super(indexSettings, name, settings);
        takeBack = settings.getAsInt("takeBack", 0);
        takeFront = settings.getAsInt("takeFront", 0);
//        log.info("Factory takeBack: "+takeBack+" takeFront: "+takeFront);
//        log.info("Factory Settings: "+settings.toDelimitedString('#'));
    }

    @Override
    public Tokenizer create() {
//        log.info("Tokenizer created by factory");
        return new BackFrontTokenizer(takeBack, takeFront);
    }        
}
