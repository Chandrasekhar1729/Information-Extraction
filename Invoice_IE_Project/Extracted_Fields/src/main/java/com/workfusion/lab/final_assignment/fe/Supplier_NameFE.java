/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment.fe;

import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Determines if focus annotation is NER and the last NER in the document
 */
public class Supplier_NameFE<T extends Element> implements FeatureExtractor<T> {

    /**
     * Name of {@link Feature} the feature extractor produces.
     */
    public static final String FEATURE_NAME = "supplier_name";

    /**
     * Determines if focus annotation is NER and the last NER in the document
     * @param document the Document containing the focus
     * @param element the focus being checked for it position in  the covering annotation
     * @return "lastnerFeature" if focus annotation is NER and the last NEM in the document, nothing otherwise
     */
    @Override
    public Collection<Feature> extract(Document document, T element) {

        List<Feature> features = new ArrayList<>();// Lines to be taken from cache
        List<Line> lines = document.findCovering(Line.class, element);
        List<Line> nextcells = document.findNext(Line.class, element, 1);
        if (!lines.isEmpty() && (!nextcells.isEmpty())) {
            String col_text = lines.get(0).getText().replace("\n", "").replace(" ", "");
            String temp = nextcells.get(0).getText().replace("\n","").replace(" ","");
            if(temp.toLowerCase().contains("invoice")){
                features.add(new Feature(FEATURE_NAME, 1));
            }
        }
        return features;
    }
}