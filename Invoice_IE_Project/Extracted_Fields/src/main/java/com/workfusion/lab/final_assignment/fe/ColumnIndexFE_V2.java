package com.workfusion.lab.final_assignment.fe;

import com.workfusion.vds.sdk.api.nlp.annotation.DependsOn;
import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.FeatureName;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.Index;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.IndexType;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.Indexes;
import com.workfusion.vds.sdk.api.nlp.model.Cell;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;
import com.workfusion.vds.sdk.api.nlp.model.Token;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DependsOn({Cell.class, Token.class})
@Indexes({
        @Index(covering = Cell.class, covered = Token.class, type = IndexType.COVERING)
})
@FeatureName(ColumnIndexFE_V2.FEATURE_NAME)
public class ColumnIndexFE_V2<T extends Element> implements FeatureExtractor<T> {

    /**
     * Name of {@link Feature} the feature extractor produces.
     */
    public static final String FEATURE_NAME = "column_index";

    /**
     * Determines column index for the focus element
     *
     * @param document {@link Document} the Document containing the focus
     * @param element  {@link Element} the current focus
     * @return "column_index" if presents cell presents, empty list otherwise
     */
    @Override
    public Collection<Feature> extract(Document document, T element) {
        List<Feature> result = new ArrayList<>();

        List<Cell> cells = document.findCovering(Cell.class, element);
        for (Cell cell : cells) {
            result.add(new Feature(StringUtils.join(FEATURE_NAME, "=", cell.getColumnIndex()), 1));
        }

        return result;
    }

}
