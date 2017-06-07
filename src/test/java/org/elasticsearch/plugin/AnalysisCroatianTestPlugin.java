/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.plugin;

import java.util.Collection;
import java.util.Collections;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.CroatianAnalysisBinderProcessor;
import org.elasticsearch.indices.analysis.CroatianAnalysisModule;
import org.elasticsearch.plugins.Plugin;

/**
 *
 * @author Marijo
 */
public class AnalysisCroatianTestPlugin extends Plugin{
    
    @Override
    public String name() {
        return "analysis-croatian";
    }

    @Override
    public String description() {
        return "Croatian analysis support";
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module>singletonList(new CroatianAnalysisModule());
    }

    /**
     * Automatically called with the analysis module.
     */
    @Override
    public void onModule(AnalysisModule module) {
        module.addProcessor(new CroatianAnalysisBinderProcessor());
    }
    
}
