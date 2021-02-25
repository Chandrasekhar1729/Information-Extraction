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

public class TotalAmountPostProcessor implements Processor<IeDocument> {

    private static final String REGEX_TOTAL_AMOUNT_WRONG_CHARS  = "[,($]";

    @Override
    public void process(IeDocument document) {

        Collection<Field> fields = document.findFields("total_amount");
        ArrayList<String> test_initial= new ArrayList<>();

        if(!fields.isEmpty()){
            Iterator<Field> iterator = fields.iterator();
            OcrAmountNormalizer amountNormalizer = new OcrAmountNormalizer();
            while (iterator.hasNext()) {
                Field currentiterator = iterator.next();
                String value = currentiterator.getValue();
                test_initial.add(value);
                System.out.println("total_amount....."+value);
                currentiterator.setValue(amountNormalizer.normalize(value));
            }
        }

    }



}