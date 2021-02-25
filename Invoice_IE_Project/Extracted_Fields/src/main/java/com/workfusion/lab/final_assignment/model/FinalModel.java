/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package com.workfusion.lab.final_assignment.model;

import com.workfusion.lab.final_assignment.config.FinalModelConfiguration;
import com.workfusion.vds.nlp.hypermodel.ie.generic.GenericIeHypermodel;
import com.workfusion.vds.sdk.api.hypermodel.ModelType;
import com.workfusion.vds.sdk.api.hypermodel.annotation.HypermodelConfiguration;
import com.workfusion.vds.sdk.api.hypermodel.annotation.ModelDescription;

/**
 * The model class. Define here you model details like code, version etc.
 */
@ModelDescription(
        code = "wf-lab-ml-sdk-lesson-8-model",
        title = "WF Lab ML-SDK Lesson 8 Model (1.0)",
        description = "WF Lab ML-SDK Lesson 8 Model (1.0)",
        version = "1.0",
        type = ModelType.IE
)
@HypermodelConfiguration(FinalModelConfiguration.class)
public class FinalModel extends GenericIeHypermodel {

    public FinalModel() throws Exception {

    }

}