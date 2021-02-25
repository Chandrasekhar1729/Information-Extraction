package com.workfusion.lab.final_assignment.fe;

import com.workfusion.vds.nlp.similarity.StringSimilarityUtils;
import com.workfusion.vds.sdk.api.nlp.annotation.DependsOn;
import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.FeatureName;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.Index;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.IndexType;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.Indexes;
import com.workfusion.vds.sdk.api.nlp.model.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gets similarity of focus annotation to provided keyword
 */
@DependsOn({Line.class, Token.class})
@Indexes({
        @Index(covering = Line.class, covered = Token.class, type = IndexType.BIDIRECTIONAL)
})
@FeatureName(SimilarityKeyInvoiceFE.FEATURE_NAME)
public class SimilarityKeyInvoiceFE<T extends Element> implements FeatureExtractor<T> {

    /**
     * Name of {@link Feature} the feature extractor produces.
     */
    public static final String FEATURE_NAME =  "invoice_number";
    private final static String INVOICE_NUMBER_REGEX = "\\d";
    /**
     * The words to look for similarity.
     */
    private String keyword;

    public SimilarityKeyInvoiceFE(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    /**
     * Gets similarity of focus annotation to provided keyword
     * <p>
     * Cosine similarity is a measure of similarity between two vectors of an inner product space
     * that measures the cosine of the angle between them.
     * The cosine of 0° is 1, and it is less than 1 for any other angle.
     *
     * @param document the Document containing the focus
     * @param element  the annotation being checked for matching keyword
     * @return best similarity score to keyword
     */
    @Override
    public Collection<Feature> extract(Document document, T element) {
        String n = ".*[0-9].*";
        String a = ".*[A-Z].*";
        int found = 0 ;
        List result = new ArrayList<>();
        String ele_value = element.getText();
        List<Line> lines = document.findPrevious(Line.class, element, 1);
        if(!lines.isEmpty()) {
            Line ex = lines.get(0);
            String lineValue = ex.getText().toLowerCase();
            String[] spr = StringUtils.splitPreserveAllTokens(lineValue);
            double res = 0.0;
            for (String s : spr) {
                String test = s;
                double retValue = StringSimilarityUtils.cosine(keyword.toLowerCase(), s.toLowerCase());
                if (res < retValue) {
                    Pattern pattern = Pattern.compile(INVOICE_NUMBER_REGEX);
                    Matcher matcher = pattern.matcher(ele_value);
                    if (ele_value.matches(n) && ele_value.matches(a)) {
                        found=1;
                    }
                    else if (matcher.find() & ele_value.length()>5) {
                        found=1;
                    }
                    res = retValue;
                }
            }

            if (res > 0.0 & found ==1) {
                result.add(new Feature(FEATURE_NAME, res));
            }
        }
        return result;
    }

}

