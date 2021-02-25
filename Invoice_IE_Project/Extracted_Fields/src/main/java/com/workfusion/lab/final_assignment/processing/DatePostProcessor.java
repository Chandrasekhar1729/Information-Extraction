/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment.processing;

import com.workfusion.vds.sdk.api.nlp.model.Field;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;
import com.workfusion.vds.sdk.api.nlp.processing.Processor;
import com.workfusion.vds.sdk.nlp.component.processing.normalization.OcrDateNormalizer;

import java.util.Collection;
import java.util.Iterator;

/**
 * Assignment 1
 */
public class DatePostProcessor implements Processor<IeDocument> {

    /**
     * Name of {@link Field} representing a date.
     */
    public static final String FIELD_NAME = "invoice_date";

    /**
     * A format to which a date needs to be converted in the output.
     */
    private static final String OUTPUT_DATE_FORMAT = "MM/dd/yyyy";

    @Override
    public void process(IeDocument document) {
        Collection<Field> fields =document.findFields("invoice_date");
        Iterator<Field> iterator = fields.iterator();
        String text_frm_raw_field = "";
        OcrDateNormalizer normalizer = new OcrDateNormalizer("MM/dd/yyyy");
        System.out.println("text_frm_raw_field....."+text_frm_raw_field);
        String outputStr = "";
        while (iterator.hasNext()) {
            Field currentiterator = iterator.next();
            text_frm_raw_field = currentiterator.getText();
            System.out.println("text_frm_raw_field....."+text_frm_raw_field);
            outputStr =normalizer.normalize(text_frm_raw_field);
            currentiterator.setValue(outputStr);
        }

    }

}