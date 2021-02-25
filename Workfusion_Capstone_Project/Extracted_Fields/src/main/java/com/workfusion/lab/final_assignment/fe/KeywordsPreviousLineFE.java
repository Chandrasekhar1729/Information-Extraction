/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment.fe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;
import com.workfusion.vds.sdk.api.nlp.model.Line;

/**
 * Gets similarity of focus annotation to provided keyword
 */
public class KeywordsPreviousLineFE<T extends Element> implements FeatureExtractor<T> {

    /**
     * Name of {@link Feature} the feature extractor produces.
     */
    public static final String FEATURE_NAME = "keywordFeature";

    private String keyword;

    public boolean isStringBool(String s)
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

    public KeywordsPreviousLineFE(String keyword) {
        this.keyword = keyword.toLowerCase();

    }

    @Override
    public Collection<Feature> extract(Document document, T element) {
            List<Feature> features = new ArrayList<>();
            int k=0;

        List<Line> lines = document.findCovering(Line.class, element);
        List<Line> prevLines = document.findPrevious(Line.class, element, 1);
        if (!lines.isEmpty() && (!prevLines.isEmpty())) {
            String col_text2 = lines.get(0).getText().replace("$","").replace("\n","").replace(" ","").replace(",","");
            String temp = prevLines.get(0).getText().replace("\n","").replace(" ","");
            if(temp.toLowerCase().contains(keyword.toLowerCase()) & isStringBool(col_text2)){
                features.add(new Feature(FEATURE_NAME, 1));
            }

        }
//            String text = element.getText();
//            String[] items= document.getText().split("\n");
//            List<String> itemList = new ArrayList<String>(Arrays.asList(items));
//            itemList.removeAll(Arrays.asList(""));
//            for(int ij=0 ; ij<itemList.size() ; ++ij){
//                if( itemList.get(ij).toLowerCase().contains(element.getText().toLowerCase())){
//                    k  =  ij;
//                    break;
//                }
//
//            }
//            if(k!=0) {
//                if(itemList.get(k-1).toLowerCase().contains(keyword.toLowerCase())) {
//                features.add(new Feature(FEATURE_NAME, 1.0));
//            }}

        return features;


    }

}