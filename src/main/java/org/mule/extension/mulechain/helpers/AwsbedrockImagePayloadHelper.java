package org.mule.extension.mulechain.helpers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.json.JSONObject;
import org.mule.extension.mulechain.internal.AwsbedrockConfiguration;
import org.mule.extension.mulechain.internal.AwsbedrockParameters;
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

public class AwsbedrockImagePayloadHelper {

//   private static BedrockRuntimeClient createClient(AwsBasicCredentials awsCreds, Region region) {
//     return BedrockRuntimeClient.builder()
//     .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
//     .region(region)
//     .build();
//   }

//   private static InvokeModelRequest createInvokeRequest(String modelId, String nativeRequest) {
//     return InvokeModelRequest.builder()
//           .body(SdkBytes.fromUtf8String(nativeRequest))
//           .modelId(modelId)
//           .build();

//   }

//   private static Region getRegion(String region){
//     switch (region) {
//       case "us-east-1":
//           return Region.US_EAST_1;
//       case "us-east-2":
//           return Region.US_EAST_2;
//       case "us-west-1":
//           return Region.US_WEST_1;
//       case "us-west-2":
//           return Region.US_WEST_2;
//       case "af-south-1":
//           return Region.AF_SOUTH_1;
//       case "ap-east-1":
//           return Region.AP_EAST_1;
//       case "ap-south-1":
//           return Region.AP_SOUTH_1;
//       case "ap-south-2":
//           return Region.AP_SOUTH_2;
//       case "ap-southeast-1":
//           return Region.AP_SOUTHEAST_1;
//       case "ap-southeast-2":
//           return Region.AP_SOUTHEAST_2;
//       case "ap-southeast-3":
//           return Region.AP_SOUTHEAST_3;
//       case "ap-southeast-4":
//           return Region.AP_SOUTHEAST_4;
//       case "ap-northeast-1":
//           return Region.AP_NORTHEAST_1;
//       case "ap-northeast-2":
//           return Region.AP_NORTHEAST_2;
//       case "ap-northeast-3":
//           return Region.AP_NORTHEAST_3;
//       case "ca-central-1":
//           return Region.CA_CENTRAL_1;
//       case "eu-central-1":
//           return Region.EU_CENTRAL_1;
//       case "eu-central-2":
//           return Region.EU_CENTRAL_2;
//       case "eu-west-1":
//           return Region.EU_WEST_1;
//       case "eu-west-2":
//           return Region.EU_WEST_2;
//       case "eu-west-3":
//           return Region.EU_WEST_3;
//       case "eu-north-1":
//           return Region.EU_NORTH_1;
//       case "eu-south-1":
//           return Region.EU_SOUTH_1;
//       case "eu-south-2":
//           return Region.EU_SOUTH_2;
//       case "me-south-1":
//           return Region.ME_SOUTH_1;
//       case "me-central-1":
//           return Region.ME_CENTRAL_1;
//       case "sa-east-1":
//           return Region.SA_EAST_1;
//       case "us-gov-east-1":
//           return Region.US_GOV_EAST_1;
//       case "us-gov-west-1":
//           return Region.US_GOV_WEST_1;
//       default:
//           throw new IllegalArgumentException("Unknown region: " + region);
//   }
//  }
  
  


//   private static String getAmazonTitanImage(String prompt, String avoidInImage, AwsbedrockImageParameters awsBedrockParameters) {
//     JSONObject jsonRequest = new JSONObject();
//     jsonRequest.put("taskType", "TEXT_IMAGE");

//     JSONObject textToImageParams = new JSONObject();
//     textToImageParams.put("text", prompt);
//     textToImageParams.put("negativeText", avoidInImage);

//     JSONObject imageGenerationConfig = new JSONObject();
//     imageGenerationConfig.put("numberOfImages", awsBedrockParameters.getNumOfImages());
//     imageGenerationConfig.put("height", awsBedrockParameters.getHeight());
//     imageGenerationConfig.put("width", awsBedrockParameters.getWidth());
//     imageGenerationConfig.put("cfgScale", awsBedrockParameters.getCfgScale());
//     imageGenerationConfig.put("seed", awsBedrockParameters.getSeed());

//     jsonRequest.put("textToImageParams", textToImageParams);
//     jsonRequest.put("imageGenerationConfig", imageGenerationConfig);

//     return jsonRequest.toString();
// }





//   private static String identifyPayload(String prompt, String avoidInImage,AwsbedrockImageParameters awsBedrockParameters){
//     if (awsBedrockParameters.getModelName().contains("amazon.titan-image")) {
//         return getAmazonTitanImage(prompt, avoidInImage, awsBedrockParameters);
//     } else {
//         return "Unsupported model";
//     }

//   }

//   private static BedrockRuntimeClient InitiateClient(AwsbedrockConfiguration configuration, AwsbedrockImageParameters awsBedrockParameters){
//         // Initialize the AWS credentials
//         AwsBasicCredentials awsCreds = AwsBasicCredentials.create(configuration.getAwsAccessKeyId(), configuration.getAwsSecretAccessKey());
//         // Create Bedrock Client 
//         return createClient(awsCreds, getRegion(awsBedrockParameters.getRegion()));

//   }

//   public static String invokeModel(String prompt, String avoidInImage, AwsbedrockConfiguration configuration, AwsbedrockImageParameters awsBedrockParameters) {
//     // Initialize the AWS credentials
//     //AwsBasicCredentials awsCreds = AwsBasicCredentials.create(configuration.getAwsAccessKeyId(), configuration.getAwsSecretAccessKey());

//     // Create Bedrock Client 
//     BedrockRuntimeClient client = InitiateClient(configuration, awsBedrockParameters);

//     String nativeRequest = identifyPayload(prompt, avoidInImage, awsBedrockParameters);

//     try {
//         byte[] imageBytes = generateImage(awsBedrockParameters.getModelName(), nativeRequest, client);

//         // Convert byte array to BufferedImage
//         ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
//         BufferedImage bufferedImage = ImageIO.read(bis);
//         bis.close();

//         // Save the image to a file
//         String filePath = "/Users/amir.khan/Documents/Downloads/generated_image.jpg"; // Specify the path where you want to save the image
//         File outputImageFile = new File(filePath);
//         ImageIO.write(bufferedImage, "jpg", outputImageFile);

//         // Display image
//         if (bufferedImage != null) {
//             System.out.println("Successfully generated image.");
//             // You can add further processing like saving the image to file or displaying it in GUI
//         } else {
//             System.out.println("Failed to generate image.");
//         }

//     } catch (Exception e) {
//         System.err.println("Error: " + e.getMessage());
//         e.printStackTrace();
//     }
//     return "Image generated";
// }

// public static byte[] generateImage(String modelId, String body, BedrockRuntimeClient client) throws IOException {
//     InvokeModelRequest request = createInvokeRequest(modelId, body);
//     InvokeModelResponse response = client.invokeModel(request);

//     JSONObject responseBody = new JSONObject(response.body().asUtf8String());

//     String base64Image = responseBody.getJSONArray("images").getString(0);
//     byte[] imageBytes = Base64.getDecoder().decode(base64Image);

//     String finishReason = responseBody.optString("error", null);
//     if (finishReason != null) {
//         throw new RuntimeException("Image generation error. Error is " + finishReason);
//     }

//     return imageBytes;
// }


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

   public static byte[] invokeModel(String prompt, AwsbedrockConfiguration configuration, AwsbedrockImageParameters awsBedrockParameters) {
        // Replace with your actual AWS credentials and region
        Region region = Region.US_EAST_1;

        String modelId = "amazon.titan-image-generator-v1";

       // String prompt = "A photograph of a cup of coffee from the side.";

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
                .toString();

        System.out.println(body);

        try {
            byte[] imageBytes = generateImage(modelId, body, configuration, region);

            // Convert byte array to BufferedImage
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(bis);
            bis.close();

            // Save the image to a file
            String filePath = "/Users/amir.khan/Documents/Downloads/generated_image.jpg"; // Specify the path where you want to save the image
            File outputImageFile = new File(filePath);
            ImageIO.write(bufferedImage, "jpg", outputImageFile);
           
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
