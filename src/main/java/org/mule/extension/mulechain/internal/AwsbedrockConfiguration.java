package org.mule.extension.mulechain.internal;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.extension.mulechain.internal.embeddings.AwsbedrockEmbeddingOperations;
import org.mule.extension.mulechain.internal.image.AwsbedrockImageModelOperations;
import org.mule.runtime.extension.api.annotation.Configuration;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Configuration(name="aws-bedrock-configuration") 
@Operations({AwsbedrockOperations.class, AwsbedrockImageModelOperations.class,AwsbedrockEmbeddingOperations.class})
public class AwsbedrockConfiguration {

  @Parameter
  private String awsAccessKeyId;
  
  @Parameter
  private String awsSecretAccessKey;

  public String getAwsAccessKeyId(){
    return awsAccessKeyId;
  }

  public String getAwsSecretAccessKey(){
    return awsSecretAccessKey;
  }

}
