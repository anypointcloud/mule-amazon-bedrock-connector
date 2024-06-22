package org.mule.extension.mulechain.internal.embeddings;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.extension.mulechain.helpers.AwsbedrockEmbeddingPayloadHelper;
import org.mule.extension.mulechain.internal.AwsbedrockConfiguration;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class AwsbedrockEmbeddingOperations {



  /**
   * Implements a simple Chat agent
   */
  @MediaType(value = ANY, strict = false)
  @Alias("GENERATE-embedding")
  public String answerPrompt(String prompt, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockParametersEmbedding awsBedrockParameters){
      String response = AwsbedrockEmbeddingPayloadHelper.invokeModel(prompt, configuration, awsBedrockParameters);
    return response;
  }

  
}
