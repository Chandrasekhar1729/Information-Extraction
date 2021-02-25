package com.workfusion.lab.final_assignment.fe;

import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.FeatureName;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.Index;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.IndexType;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.Indexes;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;
import com.workfusion.vds.sdk.api.nlp.model.NamedEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//feature extractor logic analyze Token->Named Entity relations, to increase performance index is recommended
//@Indexes({@Index(covering = NamedEntity.class, type = IndexType.COVERING),})
//@FeatureName(TargetFE.FEATURE_NAME)
public class DateTargetFE<T extends Element> implements FeatureExtractor<T> {

    public static final String FEATURE_NAME = "invoice_date";

    private final String mentionType;

    public DateTargetFE(String mentionType) {
        // specify required named entity type
        this.mentionType = mentionType;
    }

    @Override
    public Collection<Feature> extract(Document document, T element) {
        // find all named entities inside token
        List<Feature> result = new ArrayList<>();

        List<NamedEntity> namedEntity = document.findCovering(NamedEntity.class, element);
        for(int ik=0;ik< namedEntity.size();++ik){
            if((namedEntity.get(ik).getType().equalsIgnoreCase(mentionType))){
                System.out.println(namedEntity.get(ik).getText());
                result.add(new Feature(FEATURE_NAME, 1));
                return result;
            }

        }
        return Collections.emptyList();
    }
}



//        if (namedEntity.stream()
//                .filter(n -> mentionType.equalsIgnoreCase(n.getType()))
//                .findAny()
//                .isPresent()) {
//            result.add(new Feature(FEATURE_NAME, 1));
// if named entity with required type exist, then return a feature
