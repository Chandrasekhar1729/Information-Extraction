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
import com.workfusion.vds.sdk.api.nlp.model.Line;
import com.workfusion.vds.sdk.api.nlp.model.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DependsOn({Line.class, Token.class})
@Indexes({
        @Index(covering = Line.class, covered = Token.class, type = IndexType.BIDIRECTIONAL)
})
@FeatureName(PriceBasedFE_V2.FEATURE_NAME)
public class PriceBasedFE_V2 implements FeatureExtractor<Token> {

    public final static String FEATURE_NAME = "price";


    public static boolean isStringOnlyAlphabet(String str) {
        return ((!str.equals(""))
                && (str != null)
                && (str.matches("^[a-zA-Z]*$")));
    }


    @Override
    public Collection<Feature> extract(Document document, Token element) {
        boolean isProductPrice = false;
        List<Feature> features = new ArrayList<>();// Lines to be taken from cache
        List<Cell> lines = document.findCovering(Cell.class, element);
        List<Cell> nextcells = document.findNext(Cell.class, element, 5);
        if (!lines.isEmpty()) {
            int col_index2 = lines.get(0).getColumnIndex();
            int row_index2 = lines.get(0).getRowIndex();
            String col_text = lines.get(0).getText().replace("\n", "");
            if (col_index2 != 0 && row_index2 != 0 && (col_text.contains("$"))) {
                for(int ij=0;ij<nextcells.size();++ij){
                    if(nextcells.get(ij).getColumnIndex()>col_index2){
                        String temp = nextcells.get(ij).getText();
                        if(temp.contains("$")){
                            isProductPrice = true;
                        }
                    }
                    else{
                        break;
                    }
                }
                if(isProductPrice) {
                    features.add(new Feature(FEATURE_NAME, 1));
                }
            }
        }
        return features;
    }
}