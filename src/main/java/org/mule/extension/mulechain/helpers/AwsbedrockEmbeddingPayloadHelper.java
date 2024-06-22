package org.mule.extension.mulechain.helpers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.extension.mulechain.internal.AwsbedrockConfiguration;
import org.mule.extension.mulechain.internal.AwsbedrockParameters;
import org.mule.extension.mulechain.internal.embeddings.AwsbedrockParametersEmbedding;
import org.mule.extension.mulechain.internal.image.AwsbedrockImageParameters;
import org.mule.runtime.extension.internal.MuleDsqlParser.relation_return;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;
import java.awt.image.BufferedImage;

public class AwsbedrockEmbeddingPayloadHelper {


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
  
  


  private static String getAmazonTitanEmbeddingG1(String prompt) {

    return new JSONObject()
                .put("inputText", prompt)
                .toString();
}

private static String getAmazonTitanEmbeddingG2(String prompt, AwsbedrockParametersEmbedding awsBedrockParameters) {

    return new JSONObject()
                .put("inputText", prompt)
                .put("dimensions", awsBedrockParameters.getDimension())
                .put("normalize",awsBedrockParameters.getNormalize())
                .toString();
}



  private static String identifyPayload(String prompt, AwsbedrockParametersEmbedding awsBedrockParameters){
    if (awsBedrockParameters.getModelName().contains("amazon.titan-embed-text-v1")) {
        return getAmazonTitanEmbeddingG1(prompt);
    } else if (awsBedrockParameters.getModelName().contains("amazon.titan-embed-text-v2:0")) {
        return getAmazonTitanEmbeddingG2(prompt, awsBedrockParameters);
    } else {

        return "Unsupported model";
    }

  }


private static BedrockRuntimeClient createClient(AwsbedrockConfiguration configuration, Region region) {

    // Initialize the AWS credentials
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(configuration.getAwsAccessKeyId(), configuration.getAwsSecretAccessKey());



    return BedrockRuntimeClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .region(region)
            .build();
}

private static InvokeModelRequest createInvokeRequest(String modelId, String nativeRequest) {

    return InvokeModelRequest.builder()
            .body(SdkBytes.fromUtf8String(nativeRequest))
            .accept("application/json")
            .contentType("application/json")
            .modelId(modelId)
            .build();
}

public static JSONObject generateEmbedding(String modelId, String body, AwsbedrockConfiguration configuration, Region region) throws IOException {
    BedrockRuntimeClient bedrock = createClient(configuration, region);

    InvokeModelRequest request = createInvokeRequest(modelId, body);

    InvokeModelResponse response = bedrock.invokeModel(request);

    String responseBody = new String(response.body().asByteArray(), StandardCharsets.UTF_8);

    return new JSONObject(responseBody);

}

   public static String invokeModel(String prompt, AwsbedrockConfiguration configuration, AwsbedrockParametersEmbedding awsBedrockParameters) {

        Region region = getRegion(awsBedrockParameters.getRegion());

        String modelId = awsBedrockParameters.getModelName();


        String body = identifyPayload(prompt, awsBedrockParameters); 

        System.out.println(body);

        try {
            JSONObject response = generateEmbedding(modelId, body, configuration, region);

            return response.toString();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;

        }
    }

}
