package org.mule.extension.mulechain.helpers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.json.JSONObject;
import org.mule.extension.mulechain.internal.AwsbedrockConfiguration;
import org.mule.extension.mulechain.internal.image.AwsbedrockImageParameters;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;
import java.awt.image.BufferedImage;

public class AwsbedrockImagePayloadHelperBackup {


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
  
  


  private static String getAmazonTitanImage(String prompt) {

    return new JSONObject()
                .put("taskType", "TEXT_IMAGE")
                .put("textToImageParams", new JSONObject()
                        .put("text", prompt))
                .put("imageGenerationConfig", new JSONObject()
                        .put("numberOfImages", 1)
                        .put("height", 1024)
                        .put("width", 1024)
                        .put("cfgScale", 8.0)
                        .put("seed", 0))
                .toString();
}





  private static String identifyPayload(String prompt, AwsbedrockImageParameters awsBedrockParameters){
    if (awsBedrockParameters.getModelName().contains("amazon.titan-image")) {
        return getAmazonTitanImage(prompt);
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
            .modelId(modelId)
            .build();
}

public static byte[] generateImage(String modelId, String body, AwsbedrockConfiguration configuration, Region region) throws IOException {
    BedrockRuntimeClient bedrock = createClient(configuration, region);

    InvokeModelRequest request = createInvokeRequest(modelId, body);

    InvokeModelResponse response = bedrock.invokeModel(request);

    JSONObject responseBody = new JSONObject(response.body().asUtf8String());

    String base64Image = responseBody.getJSONArray("images").getString(0);
    byte[] imageBytes = Base64.getDecoder().decode(base64Image);

    String finishReason = responseBody.optString("error", null);
    if (finishReason != null) {
        throw new RuntimeException("Image generation error. Error is " + finishReason);
    }

    return imageBytes;
}

   public static byte[] invokeModel(String prompt, String fullPath, AwsbedrockConfiguration configuration, AwsbedrockImageParameters awsBedrockParameters) {
        // Replace with your actual AWS credentials and region
        Region region = getRegion(awsBedrockParameters.getRegion()); //Region.US_EAST_1;

        String modelId = awsBedrockParameters.getModelName(); //"amazon.titan-image-generator-v1";

        String body = new JSONObject()
                    .put("taskType", "TEXT_IMAGE")
                    .put("textToImageParams", new JSONObject()
                            .put("text", prompt))
                    .put("imageGenerationConfig", new JSONObject()
                            .put("numberOfImages", 1)
                            .put("height", 1024)
                            .put("width", 1024)
                            .put("cfgScale", 8.0)
                            .put("seed", 0))
                    .toString();//identifyPayload(prompt, awsBedrockParameters);

        System.out.println(body);

        try {
            byte[] imageBytes = generateImage(modelId, body, configuration, region);

            // Convert byte array to BufferedImage
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(bis);
            bis.close();

            // Save the image to a file
            String filePath = fullPath; // Specify the path where you want to save the image
            File outputImageFile = new File(filePath);
            ImageIO.write(bufferedImage, "png", outputImageFile);
           
            // Display image
            if (bufferedImage != null) {
                System.out.println("Successfully generated image.");
                // You can add further processing like saving the image to file or displaying it in GUI
            } else {
                System.out.println("Failed to generate image.");
            }
            return imageBytes;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;

        }
    }

}
