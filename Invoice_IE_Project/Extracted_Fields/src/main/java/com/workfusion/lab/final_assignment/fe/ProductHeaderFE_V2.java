package com.workfusion.lab.final_assignment.fe;

import com.workfusion.nlp.uima.types.CellAnnotation;
import com.workfusion.vds.nlp.uima.model.UimaCell;
import com.workfusion.vds.sdk.api.nlp.annotation.OnDocumentComplete;
import com.workfusion.vds.sdk.api.nlp.annotation.OnDocumentStart;
import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.Cell;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProductHeaderFE_V2<T extends Element> implements FeatureExtractor<T> {

    private Set<T> productElements = new HashSet<>();

    @OnDocumentStart
    public void onStart(IeDocument document, Class<T> focusClass) {
        document.findAll(Cell.class)
                .stream()
                .filter(c -> c.getText().toLowerCase().contains("description") || c.getText().toLowerCase().contains("product"))
                .forEach(c -> {
                    CellAnnotation currentCell = getCellAnnotation((UimaCell) c);
                    while ((currentCell = currentCell.getNeighborDown()) != null) {
                        productElements.addAll(document.findCovered(focusClass, currentCell.getBegin(), currentCell.getEnd()));
                    }
                });
    }

    private CellAnnotation getCellAnnotation(UimaCell c) {
        return (CellAnnotation) c.getAnnotation();
    }


    @OnDocumentComplete
    public void onComplete() {
        productElements.clear();
    }

    @Override
    public Collection<Feature> extract(Document document, T t) {
        return productElements.contains(t) ? Collections.singletonList(new Feature("ProductHeader", 1.0)) : Collections.emptyList();
    }

}
