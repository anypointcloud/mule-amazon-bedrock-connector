package org.mule.extension.mulechain.internal.image;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import java.nio.charset.StandardCharsets;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;

import org.mule.extension.mulechain.internal.AwsbedrockConfiguration;
import org.mule.extension.mulechain.helpers.AwsbedrockImagePayloadHelper;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class AwsbedrockImageModelOperations {



  /**
   * Generates an image based on text.
   */
  @MediaType(value = ANY, strict = false)
  @Alias("IMAGE-generate")
  public String generateImage(String TextToImage, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockImageParameters awsBedrockParameters){
      byte[] imagebytes = AwsbedrockImagePayloadHelper.invokeModel(TextToImage, configuration, awsBedrockParameters);
      String response = new String(imagebytes, StandardCharsets.UTF_8);
    return response;
  }

  


}
