/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment.config;

import java.util.ArrayList;
import java.util.List;

import com.workfusion.lab.final_assignment.annotators.EmailAnnotator;
import com.workfusion.lab.final_assignment.fe.*;
import com.workfusion.lab.final_assignment.processing.DatePostProcessor;
import com.workfusion.lab.final_assignment.processing.PricePostProcessor_V2;
import com.workfusion.lab.final_assignment.processing.QuantityPostProcessor_V2;
import com.workfusion.lab.final_assignment.processing.TotalAmountPostProcessor;
import com.workfusion.nlp.uima.fe.adapter.FeatureExtractorAdapter;
import com.workfusion.nlp.uima.fe.token.AdvancedLowerCaseFE;
import com.workfusion.vds.nlp.fe.context.ContextFE;
import com.workfusion.vds.nlp.fe.context.Preceding;
import com.workfusion.vds.nlp.uima.annotator.ner.DateNerAnnotator;
import com.workfusion.vds.sdk.api.hypermodel.annotation.ModelConfiguration;
import com.workfusion.vds.sdk.api.hypermodel.annotation.Named;
import com.workfusion.vds.sdk.api.nlp.annotator.Annotator;
import com.workfusion.vds.sdk.api.nlp.configuration.IeConfigurationContext;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;
import com.workfusion.vds.sdk.api.nlp.model.Field;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;
import com.workfusion.vds.sdk.api.nlp.model.NamedEntity;
import com.workfusion.vds.sdk.api.nlp.model.Token;
import com.workfusion.vds.sdk.api.nlp.processing.Processor;
import com.workfusion.vds.sdk.nlp.component.annotator.EntityBoundaryAnnotator;
import com.workfusion.vds.sdk.nlp.component.annotator.ner.BaseRegexNerAnnotator;
import com.workfusion.vds.sdk.nlp.component.annotator.tokenizer.SplitterTokenAnnotator;
import com.workfusion.vds.sdk.nlp.component.statistics.IeStatisticsCalculator;
import com.workfusion.vds.sdk.nlp.component.statistics.StatisticsEvaluationType;
import com.workfusion.vds.sdk.nlp.component.statistics.printer.IeGoldVsExtractedCSVPrinter;
import com.workfusion.vds.sdk.nlp.component.statistics.printer.IeGoldVsExtractedPerGroupCSVPrinter;
import com.workfusion.vds.sdk.nlp.component.statistics.printer.IePerFieldCSVPrinter;
import com.workfusion.vds.sdk.nlp.component.statistics.printer.IePerGroupFieldCSVPrinter;
import org.jsoup.Connection;

/**
 * The model configuration class.
 * Here you can configure set of Feature Extractors, Annotators.
 */
@ModelConfiguration

//@Import(configurations = {
//        @Import.Configuration(GenericIeHypermodelConfiguration.class)
//})
public class FinalModelConfiguration {

    /**
     * Regex pattern to use for matching {@link Token} elements.
     */
    private final static String TOKEN_REGEX = "([$\\$\\s:#_;'])";

    /**
     * Name of {@link Field} representing a total amount.
     */
    public final static String FIELD_TOTAL_AMOUNT = "total_amount";
    public final static String FIELD_EMAIL = "email";
    public final static String FIELD_INVOICE_NUMBER = "invoice_number";
    public static final String FIELD_SUPPLIER_NAME = "supplier_name";
    public static final String FIELD_DATE = "invoice_date";
    /**
     * Regex pattern to match an invoice number.
     */
    private final static String INVOICE_NUMBER_REGEX = "\\d{10}";
    private static final String TOTAL_AMOUNT_REGEX = "[0-9l]{1,3}[,\\.]?[0-9]{2,3}[\\.][0-9]*";
    private final static String STATE_REGEX = "[A-Z]{2}";
    /**
     * Type for {@link NamedEntity} to use for total amount NER.
     */
    private static final String NER_TYPE_TOTAL_AMOUNT = "total_amount";
    private static final String NER_INVOICE_NUMBER = "invoice_number";
    ///change
    private static final String NER_DATE = "invoice_date";

    /**
     * Keyword that needs be found in previous line
     */
    private static final String KEYWORD_SIMILARITY = "total";


    /**
     * Type for {@link NamedEntity} to use for state NER.
     */


    @Named("annotators")
    public List<Annotator<Document>> getAnnotators(IeConfigurationContext context) {
        List<Annotator<Document>> annotators = new ArrayList<>();
        annotators.add(new SplitterTokenAnnotator(TOKEN_REGEX));
        annotators.add(new EntityBoundaryAnnotator());
        annotators.add(new DateNerAnnotator(NER_DATE));
        annotators.add(new EmailAnnotator());
        annotators.add(BaseRegexNerAnnotator.getJavaPatternRegexNerAnnotator(NER_TYPE_TOTAL_AMOUNT,TOTAL_AMOUNT_REGEX));
        annotators.add(BaseRegexNerAnnotator.getJavaPatternRegexNerAnnotator(NER_DATE,"\\d{1,2}/\\d{1,2}/\\d{2,4}"));
        annotators.add(BaseRegexNerAnnotator.getJavaPatternRegexNerAnnotator(NER_INVOICE_NUMBER,INVOICE_NUMBER_REGEX));
        return annotators;
    }

    @Named("featureExtractors")
    public List<FeatureExtractor<Element>> getFeatureExtractors(IeConfigurationContext context) {
        List<FeatureExtractor<Element>> featuresExtractors = new ArrayList<>();

        featuresExtractors.add(new DateTargetFE(NER_DATE));
        featuresExtractors.add(new NerAbsolutePositionFE());
        featuresExtractors.add(new KeywordsPreviousLineFE(KEYWORD_SIMILARITY));
        featuresExtractors.add( new Email_FE());
        featuresExtractors.add(new FeatureExtractorAdapter(new ContextFE<>(org.cleartk.token.type.Token.class, new AdvancedLowerCaseFE<>(), new Preceding(3))));
         featuresExtractors.add(new InvoiceIsNerPresentFE("invoice_number"));
        featuresExtractors.add(new Supplier_NameFE());


        List<String> keywords = new ArrayList<>();
        keywords.add("product");
        keywords.add("description");

        FeatureExtractor testfe1 = new QuantityBasedFE_V2();
        FeatureExtractor testfe3 = new PriceBasedFE_V2();
        FeatureExtractor testfe4 = new ProductHeaderFE_V2();
        FeatureExtractor testfe5 = new CellColumnHeaderKeywordFE_V2(keywords);
        featuresExtractors.add(testfe1);
        featuresExtractors.add(testfe3);
        featuresExtractors.add(testfe4);
        featuresExtractors.add(testfe5);

        return featuresExtractors;
    }

    @Named("processors")
    public List<Processor<IeDocument>> getProcessors() {
        List<Processor<IeDocument>> list_feat_ex = new ArrayList<>();
        TotalAmountPostProcessor tpp = new TotalAmountPostProcessor();
        DatePostProcessor dpp = new DatePostProcessor() ;
        PricePostProcessor_V2 ppp = new PricePostProcessor_V2();
        QuantityPostProcessor_V2 qpp  = new QuantityPostProcessor_V2();
        list_feat_ex.add(ppp);
        list_feat_ex.add(qpp);
        list_feat_ex.add(dpp);
        list_feat_ex.add(tpp);
        return list_feat_ex;
    }



    @Named("statsCalculator")
    public IeStatisticsCalculator statisticsCalculator(){
        return new IeStatisticsCalculator()
                .enable(StatisticsEvaluationType.VALUE_BASED)
                .enable(StatisticsEvaluationType.TEXT_BASED)
                .printer(new IeGoldVsExtractedCSVPrinter())
                .printer(new IeGoldVsExtractedPerGroupCSVPrinter())
                .printer(new IePerFieldCSVPrinter())
                .printer(new IePerGroupFieldCSVPrinter());
        //   .printer(new IeEstimatedAccuracyStatisticsPrinter());
    }

}

