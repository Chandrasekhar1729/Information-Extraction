/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment.run;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.workfusion.lab.final_assignment.model.FinalModel;
import com.workfusion.vds.sdk.api.nlp.configuration.FieldInfo;
import com.workfusion.vds.sdk.api.nlp.configuration.FieldType;
import com.workfusion.vds.sdk.run.ModelRunner;
import com.workfusion.vds.sdk.run.config.LocalTrainingConfiguration;

/**
 * This runner allows you to start model training on your local machine.
 * Paths to training set and output folders, fields configuration are required for the launch.
 */
public class FinalModelTrainingRunner {

    /**
     * Input directory path to use.
     */
    private final static String INPUT_DIR_PATH = "data/train";
    /**
     * Output directory path to use.
     */
    public final static String OUTPUT_DIR_PATH = "results_assignment1";

    public static void main(String[] args) throws Exception {
        System.setProperty("WORKFLOW_LOG_FOLDER", "./logs/");
        //TODO Configure input/output
        Path inputDirPath = Paths.get(INPUT_DIR_PATH);
        Path outputDirPath = Paths.get(OUTPUT_DIR_PATH);

        //TODO Configure fields according to your use-case
        List<FieldInfo> fields = new ArrayList<>();
        // TODO:  PUT YOU CODE HERE

        fields.add(new FieldInfo.Builder("invoice_date").type(FieldType.INVOICE_DATE).build());
        fields.add(new FieldInfo.Builder("total_amount").type(FieldType.CURRENCY).build());
        fields.add(new FieldInfo.Builder("email").type(FieldType.EMAIL).build());
        fields.add(new FieldInfo.Builder("supplier_name").type(FieldType.FREE_TEXT).build());
        fields.add(new FieldInfo.Builder("invoice_number").type(FieldType.INVOICE_NUMBER).build());

        fields.add(new FieldInfo.Builder("product").multiValue(true).type(FieldType.FREE_TEXT).build());
        fields.add(new FieldInfo.Builder("price").multiValue(true).type(FieldType.FREE_TEXT).build());
        fields.add(new FieldInfo.Builder("quantity").multiValue(true).type(FieldType.NUMBER).build());

        Map<String, Object> parameters = new HashMap<>();

        LocalTrainingConfiguration configuration = LocalTrainingConfiguration.builder()
                .inputDir(inputDirPath)
                .outputDir(outputDirPath)
                .fields(fields)
                .parameters(parameters)
                .build();

        ModelRunner.run(FinalModel.class, configuration);
    }

}