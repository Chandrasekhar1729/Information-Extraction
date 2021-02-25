/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment.fe;

import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.Cell;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Gets similarity of focus annotation to provided keyword
 */
public class QuantityBasedFE_V2<T extends Element> implements FeatureExtractor<T> {


    private static final String QUANTITY_FEATURE_NAME = "quantity";


    public boolean isStringInt(String s)
    {
        try
        {
            Double.parseDouble(s);
//            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }


    @Override
    public Collection<Feature> extract(Document document, T element) {

        List<Feature> features = new ArrayList<>();
        List<Cell> cell_output = document.findCovering(Cell.class, element);
        if(!cell_output.isEmpty()) {
            String QuantityCheck  = cell_output.get(0).getText().replace("\n", "").replace(" ","");
            if(isStringInt(QuantityCheck)&&(!QuantityCheck.contains("$"))){
                features.add(new Feature(QUANTITY_FEATURE_NAME, 1));
            }
        }
        return features;
    }
}