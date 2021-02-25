/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.workfusion.lab.final_assignment.fe.*;
import com.workfusion.lab.final_assignment.processing.DatePostProcessor;
import org.junit.Test;

import com.workfusion.lab.final_assignment.config.FinalModelConfiguration;
import com.workfusion.lab.final_assignment.processing.TotalAmountPostProcessor;
import com.workfusion.lab.final_assignment.run.FinalModelExecutionRunner;
import com.workfusion.lab.final_assignment.run.FinalModelTrainingRunner;
import com.workfusion.lab.model.TestTokenFeatures;
import com.workfusion.lab.utils.BaseLessonTest;
import com.workfusion.vds.nlp.model.configuration.ConfigurationData;
import com.workfusion.vds.sdk.api.nlp.annotator.Annotator;
import com.workfusion.vds.sdk.api.nlp.configuration.FieldInfo;
import com.workfusion.vds.sdk.api.nlp.configuration.FieldType;
import com.workfusion.vds.sdk.api.nlp.model.Field;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;
import com.workfusion.vds.sdk.api.nlp.model.NamedEntity;
import com.workfusion.vds.sdk.api.nlp.model.Token;

public class FinalAssignmentTest extends BaseLessonTest {

    @Test
    public void assignment1() throws Exception {
        // Creates ML-SDK Document to process
        IeDocument document = getDocument("data/train/invoice174.html");

        // Obtains model configuration for field "total_amount"
        ConfigurationData configurationData = buildConfiguration(FinalModelConfiguration.class,
                new FieldInfo.Builder("total_amount").type(FieldType.FREE_TEXT).build());

        ConfigurationData configurationData2 = buildConfiguration(FinalModelConfiguration.class,
                new FieldInfo.Builder("email").type(FieldType.EMAIL).build());

        ConfigurationData configurationData3 = buildConfiguration(FinalModelConfiguration.class,
                new FieldInfo.Builder("invoice_number").type(FieldType.INVOICE_NUMBER).build());


        // Obtains defined annotators list for field "total_amount".
        List<Annotator> annotators = getAnnotatorsFromConfiguration(configurationData, 7);
        // Process annotators for field "total_amount"
        processAnnotators(document, annotators);

        // Gets all Tokens provided by the annotator to check for field "total_amount"
        List<Token> tokens = new ArrayList<>(document.findAll(Token.class));
        // Checks the provided token with the assignment 3 pattern for field "total_amount"
       // checkElements(tokens, "lesson_8_assignment_1_check_token.json");

        // Checks the provided ners with the assignment 3 pattern for field "total_amount"
        List<NamedEntity> ners = new ArrayList<>(document.findAll(NamedEntity.class));
       // checkElements(ners, "lesson_8_assignment_1_check_ners.json");

        // Process FEs list
        List<TestTokenFeatures> providedElementFeatures = processFeatures(document,
               new KeywordsPreviousLineFE("total"), new Supplier_NameFE(), new NerAbsolutePositionFE(),new Email_FE(),new InvoiceIsNerPresentFE("invoice_number"),new DateTargetFE("invoice_date") //Assignment FE to check
        );
        // Checks the provided Features with the assignment 3 pattern
        //checkElementFeatures(providedElementFeatures, "lesson_8_assignment_1_check_fe.json");

        // Adds Fields into document based on gold tagging
        addFields(document, FinalModelConfiguration.FIELD_TOTAL_AMOUNT);
        addFields(document, FinalModelConfiguration.FIELD_EMAIL);
        addFields(document, FinalModelConfiguration.FIELD_INVOICE_NUMBER);
        addFields(document, FinalModelConfiguration.FIELD_DATE);
        addFields(document, FinalModelConfiguration.FIELD_SUPPLIER_NAME);
        // Process postprocessor
        processPostProcessor(document, new DatePostProcessor(), new TotalAmountPostProcessor()); //Assignment post-processor

        // Gets all Fields provided by the Processor to check
        List<Field> fields = new ArrayList<>(document.findFields(FinalModelConfiguration.FIELD_TOTAL_AMOUNT));
        List<Field> fields2 = new ArrayList<>(document.findFields(FinalModelConfiguration.FIELD_EMAIL));
        List<Field> fields3 = new ArrayList<>(document.findFields(FinalModelConfiguration.FIELD_INVOICE_NUMBER));
        List<Field> fields4 = new ArrayList<>(document.findFields(FinalModelConfiguration.FIELD_SUPPLIER_NAME));
        // Checks the provided fields with the assignment's pattern
        //checkElements(fields, "lesson_8_assignment_1_check_pp.json");

        // Obtains training statistics
        executeRunner(FinalModelTrainingRunner.class);
        Map<String, FieldStatistic> trainingStatistics = getTrainingFieldStatistics(FinalModelTrainingRunner.OUTPUT_DIR_PATH);

        // Check the field statistics
       // checkFieldStatistics(trainingStatistics, FinalModelConfiguration.FIELD_TOTAL_AMOUNT, 0.8, 0.5);
        //checkFieldStatistics(trainingStatistics, FinalModelConfiguration.FIELD_EMAIL, 0.9, 0.7);
        //checkFieldStatistics(trainingStatistics, FinalModelConfiguration.FIELD_INVOICE_NUMBER, 0.1, 0.1);

        executeRunner(FinalModelExecutionRunner.class);
        Map<String, FieldStatistic> executionStatistics = getExecutionFieldStatistics(FinalModelTrainingRunner.OUTPUT_DIR_PATH + "/extract");

        // Check the field statistics
   //     checkFieldStatistics(executionStatistics, FinalModelConfiguration.FIELD_TOTAL_AMOUNT, 0.8, 0.5);
   //     checkFieldStatistics(executionStatistics, FinalModelConfiguration.FIELD_EMAIL, 0.8, 0.5);
    //    checkFieldStatistics(executionStatistics, FinalModelConfiguration.FIELD_INVOICE_NUMBER, 0.1, 0.1);
    }


}