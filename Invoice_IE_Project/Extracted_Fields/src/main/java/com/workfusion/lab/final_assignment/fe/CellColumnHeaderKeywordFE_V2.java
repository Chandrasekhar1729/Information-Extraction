package com.workfusion.lab.final_assignment.fe;

import com.workfusion.vds.sdk.api.nlp.annotation.DependsOn;
import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.FeatureName;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.Index;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.IndexType;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.Indexes;
import com.workfusion.vds.sdk.api.nlp.model.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@DependsOn({Table.class, Cell.class, Row.class, Token.class})
@Indexes({
        @Index(covering = Cell.class, covered = Token.class, type = IndexType.BIDIRECTIONAL),
        @Index(covering = Table.class, covered = Token.class, type = IndexType.COVERING),
        @Index(covering = Row.class, covered = Table.class, type = IndexType.COVERED),
        @Index(covering = Cell.class, covered = Row.class, type = IndexType.COVERED)
})
@FeatureName(CellColumnHeaderKeywordFE_V2.FEATURE_NAME)
public class CellColumnHeaderKeywordFE_V2<T extends Element> implements FeatureExtractor<T> {

    /**
     * Name of {@link Feature} the feature extractor produces.
     */
    public static final String FEATURE_NAME = "column_header_keyword";

    /**
     * Collection of keywords with which cells' text of the focus column are compared.
     */
    private Collection<String> keywords;

    public CellColumnHeaderKeywordFE_V2(List<String> keywords) {
        this.keywords = keywords.stream().map(String::toLowerCase).collect(Collectors.toSet());
    }

    /**
     * Determines if one of the keywords occur in the same column as the focus element
     * @param document {@link Document} the Document containing the focus
     * @param element {@link Element} the current focus
     * @return "feature" if keyword presents in the same column, empty list otherwise
     */
    @Override
    public Collection<Feature> extract(Document document, T element){
        Collection<Cell> cells = document.findCovering(Cell.class, element);
        for (Cell cell : cells) {
            int columnIndex = cell.getColumnIndex();
            Collection<Table> tables = document.findCovering(Table.class, cell);
            for (Table table : tables) {
                Collection<Row> rows = document.findCovered(Row.class, table);
                for (Row row : rows) {
                    Collection<Cell> coveredCells = document.findCovered(Cell.class, row);
                    for (Cell coveredCell : coveredCells) {
                        if (coveredCell.getColumnIndex() == columnIndex && isPresentKey(coveredCell.getText().toLowerCase())) {
                            return Collections.singletonList(new Feature(FEATURE_NAME, 1));
                        }
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private boolean isPresentKey(String text) {
        return keywords.stream()
                .anyMatch(text::contains);
    }

}
