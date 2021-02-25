/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment.fe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;
import com.workfusion.vds.sdk.api.nlp.model.NamedEntity;

/**
 * Determines if focus annotation is NER and the last NER in the document
 */
public class NerAbsolutePositionFE<T extends Element> implements FeatureExtractor<T> {

    /**
     * Name of {@link Feature} the feature extractor produces.
     */
    public static final String FEATURE_NAME = "lastnerFeature";

    /**
     * Determines if focus annotation is NER and the last NER in the document
     * @param document the Document containing the focus
     * @param element the focus being checked for it position in  the covering annotation
     * @return "lastnerFeature" if focus annotation is NER and the last NEM in the document, nothing otherwise
     */
    @Override
    public Collection<Feature> extract(Document document, T element) {

        List<Feature> result = new ArrayList<>();
        List<NamedEntity> neList = (List<NamedEntity>) document.findAll(NamedEntity.class);
        if(!neList.isEmpty()) {
            String id = neList.get(neList.size() - 1).getId();
            List<NamedEntity> coveredNe = document.findCovering(NamedEntity.class, element);

            coveredNe.forEach(c -> {
                if (c.getId().equals(id)) result.add(new Feature(FEATURE_NAME, 1.0));
            });
        }
        return result;

    }

}