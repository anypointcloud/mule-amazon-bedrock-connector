package org.mule.extension.mulechain.internal.memory;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.extension.mulechain.helpers.AwsbedrockChatMemoryHelper;
import org.mule.extension.mulechain.internal.AwsbedrockConfiguration;
import org.mule.extension.mulechain.internal.AwsbedrockParameters;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class AwsbedrockMemoryOperations {



  /**
   * Implements a simple Chat agent
   */
  @MediaType(value = ANY, strict = false)
  @Alias("CHAT-memory-answer-prompt")
  public String answerPrompt(String prompt, String memoryPath, String memoryName, Integer keepLastMessages, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockParameters awsBedrockParameters){
      String response = AwsbedrockChatMemoryHelper.invokeModel(prompt, memoryPath, memoryName, keepLastMessages, configuration, awsBedrockParameters);
    return response;
  }

  
}