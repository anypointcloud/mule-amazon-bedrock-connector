package org.mule.extension.mulechain.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.json.JSONObject;
import org.json.JSONPointer;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class AwsbedrockOperations {



  /**
   * Example of an operation that uses the configuration and a connection instance to perform some action.
   */
  @MediaType(value = ANY, strict = false)
  @Alias("CHAT-answer-prompt")
  public String retrieveInfo(String prompt, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockParameters awsBedrockParameters){
      String response = invokeModel(prompt, configuration, awsBedrockParameters);
    return response;
  }


  
  private static BedrockRuntimeClient createClient(AwsBasicCredentials awsCreds, Region region) {
    return BedrockRuntimeClient.builder()
    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
    .region(region)
    .build();
  }

  private static InvokeModelRequest createInvokeRequest(String modelId, String nativeRequest) {
    return InvokeModelRequest.builder()
          .body(SdkBytes.fromUtf8String(nativeRequest))
          .modelId(modelId)
          .build();

  }

  private static Region getRegion(String region){
    switch (region) {
      case "us-east-1":
          return Region.US_EAST_1;
      case "us-east-2":
          return Region.US_EAST_2;
      case "us-west-1":
          return Region.US_WEST_1;
      case "us-west-2":
          return Region.US_WEST_2;
      case "af-south-1":
          return Region.AF_SOUTH_1;
      case "ap-east-1":
          return Region.AP_EAST_1;
      case "ap-south-1":
          return Region.AP_SOUTH_1;
      case "ap-south-2":
          return Region.AP_SOUTH_2;
      case "ap-southeast-1":
          return Region.AP_SOUTHEAST_1;
      case "ap-southeast-2":
          return Region.AP_SOUTHEAST_2;
      case "ap-southeast-3":
          return Region.AP_SOUTHEAST_3;
      case "ap-southeast-4":
          return Region.AP_SOUTHEAST_4;
      case "ap-northeast-1":
          return Region.AP_NORTHEAST_1;
      case "ap-northeast-2":
          return Region.AP_NORTHEAST_2;
      case "ap-northeast-3":
          return Region.AP_NORTHEAST_3;
      case "ca-central-1":
          return Region.CA_CENTRAL_1;
      case "eu-central-1":
          return Region.EU_CENTRAL_1;
      case "eu-central-2":
          return Region.EU_CENTRAL_2;
      case "eu-west-1":
          return Region.EU_WEST_1;
      case "eu-west-2":
          return Region.EU_WEST_2;
      case "eu-west-3":
          return Region.EU_WEST_3;
      case "eu-north-1":
          return Region.EU_NORTH_1;
      case "eu-south-1":
          return Region.EU_SOUTH_1;
      case "eu-south-2":
          return Region.EU_SOUTH_2;
      case "me-south-1":
          return Region.ME_SOUTH_1;
      case "me-central-1":
          return Region.ME_CENTRAL_1;
      case "sa-east-1":
          return Region.SA_EAST_1;
      case "us-gov-east-1":
          return Region.US_GOV_EAST_1;
      case "us-gov-west-1":
          return Region.US_GOV_WEST_1;
      default:
          throw new IllegalArgumentException("Unknown region: " + region);
  }
 }
  
  


  private static String getAmazonTitanText(String prompt, AwsbedrockParameters awsBedrockParameters) {
    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("inputText", prompt);

    JSONObject textGenerationConfig = new JSONObject();
    textGenerationConfig.put("temperature", awsBedrockParameters.getTemperature());
    textGenerationConfig.put("topP", awsBedrockParameters.getTopP());
    textGenerationConfig.put("maxTokenCount", awsBedrockParameters.getMaxTokenCount());

    jsonRequest.put("textGenerationConfig", textGenerationConfig);

    return jsonRequest.toString();
}

private static String getAnthropicClaudeText(String prompt, AwsbedrockParameters awsBedrockParameters) {
    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("prompt", "\n\nHuman:" + prompt + "\n\nAssistant:");
    jsonRequest.put("temperature", awsBedrockParameters.getTemperature());
    jsonRequest.put("top_p", awsBedrockParameters.getTopP());
    jsonRequest.put("max_tokens_to_sample", awsBedrockParameters.getMaxTokenCount());

    return jsonRequest.toString();
}


  private static String getAI21Text(String prompt, AwsbedrockParameters awsBedrockParameters){
    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("prompt", prompt);
    jsonRequest.put("temperature", awsBedrockParameters.getTemperature());
    jsonRequest.put("topP", awsBedrockParameters.getTopP());
    jsonRequest.put("maxTokens", awsBedrockParameters.getMaxTokenCount());
    
    return jsonRequest.toString();
}

  private static String identifyPayload(String prompt, AwsbedrockParameters awsBedrockParameters){
    if (awsBedrockParameters.getModelName().contains("amazon.titan-text")) {
        return getAmazonTitanText(prompt, awsBedrockParameters);
    } else if (awsBedrockParameters.getModelName().contains("anthropic.claude")) {
        return getAnthropicClaudeText(prompt, awsBedrockParameters);
    } else if (awsBedrockParameters.getModelName().contains("ai21.j2")) {
        return getAI21Text(prompt, awsBedrockParameters);
    } else {
        return "Unsupported model";
    }

  }

  private static BedrockRuntimeClient InitiateClient(AwsbedrockConfiguration configuration, AwsbedrockParameters awsBedrockParameters){
        // Initialize the AWS credentials
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(configuration.getAwsAccessKeyId(), configuration.getAwsSecretAccessKey());
        // Create Bedrock Client 
        return createClient(awsCreds, getRegion(awsBedrockParameters.getRegion()));

  }


  private static String invokeModel(String prompt, AwsbedrockConfiguration configuration, AwsbedrockParameters awsBedrockParameters) {

    // Create Bedrock Client 
    BedrockRuntimeClient client = InitiateClient(configuration, awsBedrockParameters);

    String nativeRequest = identifyPayload(prompt, awsBedrockParameters);

    try {
        // Encode and send the request to the Bedrock Runtime.
        InvokeModelRequest request = createInvokeRequest(awsBedrockParameters.getModelName(), nativeRequest);

        System.out.println("Native request: " + nativeRequest);

        InvokeModelResponse response = client.invokeModel(request);
        

        // Decode the response body.
        JSONObject responseBody = new JSONObject(response.body().asUtf8String());


        System.out.println(responseBody);
        // Retrieve the generated text from the model's response.
        //String text = new JSONPointer("/completions/0/data/text").queryFrom(responseBody).toString();
        //System.out.println(text);

        return responseBody.toString();

    } catch (SdkClientException e) {
        System.err.printf("ERROR: Can't invoke '%s'. Reason: %s", awsBedrockParameters.getModelName(), e.getMessage());
        throw new RuntimeException(e);
    }
}


  
}
