/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment.processing;

import com.workfusion.vds.sdk.api.nlp.model.Field;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;
import com.workfusion.vds.sdk.api.nlp.processing.Processor;
import com.workfusion.vds.sdk.nlp.component.processing.normalization.OcrAmountNormalizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Assignment 5
 */
public class PricePostProcessor_V2 implements Processor<IeDocument> {

    /**
     * Name of {@link Field} representing a price.
     */
    public static final String FIELD_NAME = "price";

    @Override
    public void process(IeDocument document) {
        Collection<Field> fields = document.findFields("price");
        ArrayList<String> test_initial= new ArrayList<>();
        if(!fields.isEmpty()){
            Iterator<Field> iterator = fields.iterator();
            String text_frm_raw_field = "";
            String outputStr = "";
            OcrAmountNormalizer amountNormalizer = new OcrAmountNormalizer();
            while (iterator.hasNext()) {
                Field currentiterator = iterator.next();
                String value = currentiterator.getValue();
                test_initial.add(value);
                String correctedValue = value
                        .replaceAll("G", "6")
                        .replaceAll("b","6")
                        .replaceAll("B", "8")
                        .replaceAll("O", "0")
                        .replaceAll("I", "1")
                        .replaceAll("l","1")
                        .replaceAll("\\|","1");
                currentiterator.setValue(amountNormalizer.normalize(correctedValue));
            }
        }
        System.out.println("test");

    }

}