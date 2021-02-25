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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

//feature extractor logic analyze Token->Named Entity relations, to increase performance index is recommended
@Indexes({@Index(covering = NamedEntity.class, type = IndexType.COVERING),})
@FeatureName(TargetFE_V2.FEATURE_NAME)
public class TargetFE_V2<T extends Element> implements FeatureExtractor<T> {

    public static final String FEATURE_NAME = "date";

    private final String mentionType;

    public TargetFE_V2(String mentionType) {
        // specify required named entity type
        this.mentionType = mentionType;
    }

    @Override
    public Collection<Feature> extract(Document document, T element) {
        // find all named entities inside token
        System.out.println("train start");
        List<NamedEntity> namedEntity = document.findCovering(NamedEntity.class, element);
        if (namedEntity.stream()
                .filter(n -> mentionType.equalsIgnoreCase(n.getType()))
                .findAny()
                .isPresent()) {
            // if named entity with required type exist, then return a feature
            return Collections.singletonList(new Feature(FEATURE_NAME, 1));
        }
        return Collections.emptyList();
    }
}