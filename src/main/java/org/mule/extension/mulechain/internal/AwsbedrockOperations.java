package org.mule.extension.mulechain.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;

import org.mule.extension.mulechain.helpers.AwsbedrockPayloadHelper;
import org.mule.extension.mulechain.helpers.AwsbedrockPromptTemplateHelper;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class AwsbedrockOperations {



  /**
   * Implements a simple Chat agent
   */
  @MediaType(value = ANY, strict = false)
  @Alias("CHAT-answer-prompt")
  public String answerPrompt(String prompt, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockParameters awsBedrockParameters){
      String response = AwsbedrockPayloadHelper.invokeModel(prompt, configuration, awsBedrockParameters);
    return response;
  }

  
  /**
   * Helps defining an AI Agent with a prompt template
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-define-prompt-template")  
  public String definePromptTemplate(String template, String instructions, String dataset, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockParameters awsBedrockParameters) {


          String finalPromptTemplate = AwsbedrockPromptTemplateHelper.definePromptTemplate(template, instructions, dataset);
          System.out.println(finalPromptTemplate);

          String response = AwsbedrockPayloadHelper.invokeModel(finalPromptTemplate, configuration, awsBedrockParameters);

          System.out.println(response);
      	return response;
      }


   
   /**
   * Example of a sentiment analyzer, which accepts text as input.
   */
  @MediaType(value = ANY, strict = false)
  @Alias("SENTIMENT-analyze")  
  public String extractSentiments(String TextToAnalyze,  @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockParameters awsBedrockParameters) {
  
	  String SentimentTemplate = "Analyze sentiment of: " + TextToAnalyze + ". Does it have a positive sentiment? Respond in JSON with Sentiment (value of POSITIVE, NEGATIVE, NEUTRAL) and IsPositive (true or false)";
      
    String response = AwsbedrockPayloadHelper.invokeModel(SentimentTemplate, configuration, awsBedrockParameters);
      
    return response;
  }


   /**
   * Get foundational models by Id.
   */
  @MediaType(value = ANY, strict = false)
  @Alias("FOUNDATIONAL-model-details")  
  public String getFoundationalModelByModelId(@Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockParameters awsBedrockParameters) {
  
    String response = AwsbedrockPayloadHelper.getFoundationModel(configuration, awsBedrockParameters);
    
    return response;
  }




}
