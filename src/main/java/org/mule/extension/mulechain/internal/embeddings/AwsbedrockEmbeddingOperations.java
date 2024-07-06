package org.mule.extension.mulechain.internal.embeddings;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.xml.sax.SAXException;
import org.mule.extension.mulechain.helpers.AwsbedrockEmbeddingPayloadHelper;
import org.mule.extension.mulechain.internal.AwsbedrockConfiguration;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class AwsbedrockEmbeddingOperations {



  /**
   * Generate embeddings for text
   */
  @MediaType(value = ANY, strict = false)
  @Alias("GENERATE-embedding")
  public String generateEmbeddings(String prompt, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockParametersEmbedding awsBedrockParameters){
      String response = AwsbedrockEmbeddingPayloadHelper.invokeModel(prompt, configuration, awsBedrockParameters);
    return response;
  }

  /**
   * Performs retrieval augmented generation based on files
   * @throws TikaException 
   * @throws SAXException 
   * @throws IOException 
   */
  @MediaType(value = ANY, strict = false)
  @Alias("RAG-embedding-text-score")
  public String ragEmbeddingTextScore(String prompt, String filePath, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockParametersEmbeddingDocument awsBedrockParameters) throws IOException, SAXException, TikaException{
      String response = AwsbedrockEmbeddingPayloadHelper.InvokeAdhocRAG(prompt, filePath, configuration, awsBedrockParameters);
    return response;
  }

}
