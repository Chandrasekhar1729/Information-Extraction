package com.workfusion.lab.final_assignment.processing;

import com.workfusion.vds.sdk.api.nlp.model.Field;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;
import com.workfusion.vds.sdk.api.nlp.processing.Processor;

import java.util.Collection;
import java.util.Iterator;

public class QuantityPostProcessor_V2 implements Processor<IeDocument> {

    /**
     * Name of {@link Field} representing a price.
     */
    public static final String FIELD_NAME = "price";

    @Override
    public void process(IeDocument document) {
        Collection<Field> fields = document.findFields("quantity");
        if(!fields.isEmpty()){
            Iterator<Field> iterator = fields.iterator();
            while (iterator.hasNext()) {
                Field currentiterator = iterator.next();
                String value = currentiterator.getValue();
                currentiterator.setValue((value)+".00");
                System.out.println("test");
            }
        }
    }
}
