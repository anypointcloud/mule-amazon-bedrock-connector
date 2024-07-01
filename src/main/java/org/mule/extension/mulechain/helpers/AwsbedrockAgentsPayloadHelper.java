package org.mule.extension.mulechain.helpers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.extension.mulechain.internal.AwsbedrockConfiguration;
import org.mule.extension.mulechain.internal.AwsbedrockParameters;
import org.mule.extension.mulechain.internal.AwsbedrockParams;
import org.mule.extension.mulechain.internal.AwsbedrockParamsModelDetails;
import org.mule.extension.mulechain.internal.agents.AwsbedrockAgentsParameters;
import software.amazon.awssdk.services.bedrockagent.model.Agent;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import javafx.util.Pair;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.waiters.Waiter;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.CustomModelSummary;
import software.amazon.awssdk.services.bedrock.model.FoundationModelDetails;
import software.amazon.awssdk.services.bedrock.model.FoundationModelSummary;
import software.amazon.awssdk.services.bedrock.model.GetCustomModelRequest;
import software.amazon.awssdk.services.bedrock.model.GetCustomModelResponse;
import software.amazon.awssdk.services.bedrock.model.GetFoundationModelRequest;
import software.amazon.awssdk.services.bedrock.model.GetFoundationModelResponse;
import software.amazon.awssdk.services.bedrock.model.ListCustomModelsResponse;
import software.amazon.awssdk.services.bedrock.model.ListFoundationModelsResponse;
import software.amazon.awssdk.services.bedrock.model.ValidationException;
import software.amazon.awssdk.services.bedrockagent.BedrockAgentClient;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentResponse;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentResponseHandler;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.CreateRoleRequest;
import software.amazon.awssdk.services.iam.model.CreateRoleResponse;
import software.amazon.awssdk.services.iam.model.PutRolePolicyRequest;
import software.amazon.awssdk.services.iam.model.Role;
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.bedrockagent.BedrockAgentClient;
import software.amazon.awssdk.services.bedrockagent.model.CreateAgentRequest;
import software.amazon.awssdk.services.bedrockagent.model.CreateAgentResponse;
import software.amazon.awssdk.services.bedrockagent.model.GetAgentRequest;
import software.amazon.awssdk.services.bedrockagent.model.GetAgentResponse;
import software.amazon.awssdk.services.bedrockagent.model.PrepareAgentRequest;
import software.amazon.awssdk.services.bedrockagent.model.PrepareAgentResponse;
import software.amazon.awssdk.services.bedrockagent.model.Agent;
import software.amazon.awssdk.services.bedrockagent.model.AgentAlias;
import software.amazon.awssdk.services.bedrockagent.model.AgentStatus;
import software.amazon.awssdk.services.bedrockagent.model.CreateAgentAliasRequest;
import software.amazon.awssdk.services.bedrockagent.model.CreateAgentAliasResponse;
import software.amazon.awssdk.services.bedrockagent.model.AgentAlias;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentResponse;
import software.amazon.awssdk.services.bedrockagent.model.AgentAlias;
import software.amazon.awssdk.services.bedrockagent.model.Agent;
import software.amazon.awssdk.core.SdkBytes;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.Scanner;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class AwsbedrockAgentsPayloadHelper {

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


private static String getStabilityTitanText(String prompt) {
    JSONObject jsonRequest = new JSONObject();
    JSONObject textGenerationConfig = new JSONObject();
    textGenerationConfig.put("text", prompt);

    jsonRequest.put("text_prompts", textGenerationConfig);

    return jsonRequest.toString();
}


private static String getAnthropicClaudeText(String prompt, AwsbedrockParameters awsBedrockParameters) {
    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("prompt", "\n\nHuman:" + prompt + "\n\nAssistant:");
    jsonRequest.put("temperature", awsBedrockParameters.getTemperature());
    jsonRequest.put("top_p", awsBedrockParameters.getTopP());
    jsonRequest.put("top_k", awsBedrockParameters.getTopK());
    jsonRequest.put("max_tokens_to_sample", awsBedrockParameters.getMaxTokenCount());

    return jsonRequest.toString();
}

private static String getMistralAIText(String prompt, AwsbedrockParameters awsBedrockParameters) {
    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("prompt", "\n\nHuman:" + prompt + "\n\nAssistant:");
    jsonRequest.put("temperature", awsBedrockParameters.getTemperature());
    jsonRequest.put("top_p", awsBedrockParameters.getTopP());
    jsonRequest.put("top_k", awsBedrockParameters.getTopK());
    jsonRequest.put("max_tokens", awsBedrockParameters.getMaxTokenCount());

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

private static String getCohereText(String prompt, AwsbedrockParameters awsBedrockParameters){
    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("prompt", prompt);
    jsonRequest.put("temperature", awsBedrockParameters.getTemperature());
    jsonRequest.put("p", awsBedrockParameters.getTopP());
    jsonRequest.put("k", awsBedrockParameters.getTopK());
    jsonRequest.put("max_tokens", awsBedrockParameters.getMaxTokenCount());
    
    return jsonRequest.toString();
}

private static String getLlamaText(String prompt, AwsbedrockParameters awsBedrockParameters){
    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("prompt", prompt);
    jsonRequest.put("temperature", awsBedrockParameters.getTemperature());
    jsonRequest.put("top_p", awsBedrockParameters.getTopP());
    jsonRequest.put("max_gen_len", awsBedrockParameters.getMaxTokenCount());
    
    return jsonRequest.toString();
}



  private static String identifyPayload(String prompt, AwsbedrockParameters awsBedrockParameters){
    if (awsBedrockParameters.getModelName().contains("amazon.titan-text")) {
        return getAmazonTitanText(prompt, awsBedrockParameters);
    } else if (awsBedrockParameters.getModelName().contains("anthropic.claude")) {
        return getAnthropicClaudeText(prompt, awsBedrockParameters);
    } else if (awsBedrockParameters.getModelName().contains("ai21.j2")) {
        return getAI21Text(prompt, awsBedrockParameters);
    } else if (awsBedrockParameters.getModelName().contains("mistral.mistral")) {
        return getMistralAIText(prompt, awsBedrockParameters);
    } else if (awsBedrockParameters.getModelName().contains("cohere.command")) {
        return getCohereText(prompt, awsBedrockParameters);
    } else if (awsBedrockParameters.getModelName().contains("meta.llama")) {
        return getLlamaText(prompt, awsBedrockParameters);
    } else if (awsBedrockParameters.getModelName().contains("mistral.")) {
        return getLlamaText(prompt, awsBedrockParameters);
    } else if (awsBedrockParameters.getModelName().contains("mistral.")) {
        return getStabilityTitanText(prompt);
    } else {
        return "Unsupported model";
    }

  }

  private static BedrockRuntimeClient InitiateClient(AwsbedrockConfiguration configuration, AwsbedrockParameters awsBedrockParameters){
        // Initialize the AWS credentials
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(configuration.getAwsAccessKeyId(), configuration.getAwsSecretAccessKey());
        // Create Bedrock Client 
        return createClient(awsCreds, AwsbedrockPayloadHelper.getRegion(awsBedrockParameters.getRegion()));

  }


  public static String invokeModel(String prompt, AwsbedrockConfiguration configuration, AwsbedrockParameters awsBedrockParameters) {

    // Create Bedrock Client 
    BedrockRuntimeClient client = InitiateClient(configuration, awsBedrockParameters);

    String nativeRequest = identifyPayload(prompt, awsBedrockParameters);

    try {
        // Encode and send the request to the Bedrock Runtime.
        InvokeModelRequest request = createInvokeRequest(awsBedrockParameters.getModelName(), nativeRequest);

        //System.out.println("Native request: " + nativeRequest);

        InvokeModelResponse response = client.invokeModel(request);
        

        // Decode the response body.
        JSONObject responseBody = new JSONObject(response.body().asUtf8String());


        //System.out.println(responseBody);
        // Retrieve the generated text from the model's response.
        //String text = new JSONPointer("/completions/0/data/text").queryFrom(responseBody).toString();
        //System.out.println(text);

        return responseBody.toString();

    } catch (SdkClientException e) {
        System.err.printf("ERROR: Can't invoke '%s'. Reason: %s", awsBedrockParameters.getModelName(), e.getMessage());
        throw new RuntimeException(e);
    }
}


private static BedrockClient createBedrockClient(AwsbedrockConfiguration configuration, AwsbedrockAgentsParameters awsBedrockParameters) {

    BedrockClient bedrockClient = BedrockClient.builder()
    .region(AwsbedrockPayloadHelper.getRegion(awsBedrockParameters.getRegion())) 
    .credentialsProvider(StaticCredentialsProvider.create(createAwsBasicCredentials(configuration)))
    .build();

    return bedrockClient;
}

private static BedrockAgentClient createBedrockAgentClient(AwsbedrockConfiguration configuration, AwsbedrockAgentsParameters awsBedrockParameters) {

    BedrockAgentClient bedrockAgentClient = BedrockAgentClient.builder()
    .region(AwsbedrockPayloadHelper.getRegion(awsBedrockParameters.getRegion())) 
    .credentialsProvider(StaticCredentialsProvider.create(createAwsBasicCredentials(configuration)))
    .build();

    return bedrockAgentClient;
}


private static BedrockAgentRuntimeClient createBedrockAgentRuntimeClient(AwsbedrockConfiguration configuration, AwsbedrockAgentsParameters awsBedrockParameters) {
    BedrockAgentRuntimeClient bedrockAgentRuntimeClient = BedrockAgentRuntimeClient.builder()
    .region(AwsbedrockPayloadHelper.getRegion(awsBedrockParameters.getRegion())) 
    .credentialsProvider(StaticCredentialsProvider.create(createAwsBasicCredentials(configuration)))
    .build();

    return bedrockAgentRuntimeClient;
}


private static BedrockAgentRuntimeAsyncClient createBedrockAgentRuntimeAsyncClient(AwsbedrockConfiguration configuration, AwsbedrockAgentsParameters awsBedrockParameters) {
    BedrockAgentRuntimeAsyncClient bedrockAgentRuntimeClient = BedrockAgentRuntimeAsyncClient.builder()
    .region(AwsbedrockPayloadHelper.getRegion(awsBedrockParameters.getRegion())) 
    .credentialsProvider(StaticCredentialsProvider.create(createAwsBasicCredentials(configuration)))
    .build();

    return bedrockAgentRuntimeClient;
}



private static AwsBasicCredentials createAwsBasicCredentials(AwsbedrockConfiguration configuration){
    return AwsBasicCredentials.create(
        configuration.getAwsAccessKeyId(), 
        configuration.getAwsSecretAccessKey()
    );

}



private static IamClient createIamClient(AwsbedrockConfiguration configuration, AwsbedrockAgentsParameters awsBedrockParameters) {
    return IamClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(createAwsBasicCredentials(configuration)))
        .region(AwsbedrockPayloadHelper.getRegion(awsBedrockParameters.getRegion()))
        .build();
}


private Pair<String, String> requestNameAndModelFromUser(BedrockAgentRuntimeAsyncClient bedrockWrapper) {
    List<String> existingAgentNames = bedrockWrapper.listAgents().stream()
            .map(agent -> agent.agentName())
            .collect(Collectors.toList());

    String name;
    while (true) {
        name = q.ask("Enter an agent name: ", this::isValidAgentName);
        if (!existingAgentNames.stream().anyMatch(n -> n.equalsIgnoreCase(name))) {
            break;
        }
        System.out.println("Agent " + name + " conflicts with an existing agent. Please use a different name.");
    }

    List<String> models = Arrays.asList("anthropic.claude-instant-v1", "anthropic.claude-v2");
    String modelId = models.get(q.choose("Which foundation model would you like to use? ", models));

    return new Pair<>(name, modelId);
}

private boolean isValidAgentName(String name) {
    // Add your validation logic here
    return true;
}


private Role createAgentRole(String modelId, String postfix, String RolePolicyName, AwsbedrockConfiguration configuration, AwsbedrockAgentsParameters awsBedrockParameters) {
    String roleName = "AmazonBedrockExecutionRoleForAgents_" + postfix;
    String modelArn = "arn:aws:bedrock:" + AwsbedrockPayloadHelper.getRegion(awsBedrockParameters.getRegion()) + "::foundation-model/" + awsBedrockParameters + "*";
    String ROLE_POLICY_NAME = RolePolicyName;

    System.out.println("Creating an execution role for the agent...");

    // Create an IAM client
    IamClient iamClient = createIamClient(configuration, awsBedrockParameters);
    try {
        CreateRoleResponse createRoleResponse = iamClient.createRole(CreateRoleRequest.builder()
                .roleName(roleName)
                .assumeRolePolicyDocument("{\"Version\": \"2012-10-17\",\"Statement\": [{\"Effect\": \"Allow\",\"Principal\": {\"Service\": \"bedrock.amazonaws.com\"},\"Action\": \"sts:AssumeRole\"}]}")
                .build());

        iamClient.putRolePolicy(PutRolePolicyRequest.builder()
                .roleName(roleName)
                .policyName(ROLE_POLICY_NAME)
                .policyDocument("{\"Version\": \"2012-10-17\",\"Statement\": [{\"Effect\": \"Allow\",\"Action\": \"bedrock:InvokeModel\",\"Resource\": \"" + modelArn + "\"}]}")
                .build());

        return Role.builder()
                .roleName(roleName)
                .arn(createRoleResponse.role().arn())
                .build();
    } catch (IamException e) {
        System.out.println("Couldn't create role " + roleName + ". Here's why: " + e.getMessage());
        throw e;
    }
}

private Agent createAgent(String name, String instruction, String modelId, Role agentRole, BedrockAgentClient bedrockAgentClient) {
    System.out.println("Creating the agent...");

    //String instruction = "You are a friendly chat bot. You have access to a function called that returns information about the current date and time. When responding with date or time, please make sure to add the timezone UTC.";
    CreateAgentResponse createAgentResponse = bedrockAgentClient.createAgent(CreateAgentRequest.builder()
            .agentName(name)
            .foundationModel(modelId)
            .instruction(instruction)
            .agentResourceRoleArn(agentRole.arn())
            .build());

    waitForAgentStatus(createAgentResponse.agent().agentId(), AgentStatus.NOT_PREPARED.toString(), bedrockAgentClient);

    return createAgentResponse.agent();
}


private void waitForAgentStatus(String agentId, String status, BedrockAgentClient bedrockAgentClient) {
    while (true) {
        GetAgentResponse response = bedrockAgentClient.getAgent(GetAgentRequest.builder()
                .agentId(agentId)
                .build());

        if (response.agent().agentStatus().toString().equals(status)) {

            break;
        }

        try {
            System.out.println("Waiting for agent get prepared...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}



private PrepareAgentResponse prepareAgent(String agentId, BedrockAgentClient bedrockAgentClient) {
    System.out.println("Preparing the agent...");

    //String agentId = agent.agentId();
    PrepareAgentResponse preparedAgentDetails = bedrockAgentClient.prepareAgent(PrepareAgentRequest.builder()
            .agentId(agentId)
            .build());
    waitForAgentStatus(agentId, "PREPARED", bedrockAgentClient);

    return preparedAgentDetails;
}


private AgentAlias createAgentAlias(String alias, String agentId, BedrockAgentClient bedrockAgentClient) {
    System.out.println("Creating an agent alias...");
    CreateAgentAliasRequest request = CreateAgentAliasRequest.builder()
            .agentId(agentId)
            .agentAliasName(alias)
            .build();   


    CreateAgentAliasResponse response = bedrockAgentClient.createAgentAlias(request);

    waitForAgentStatus(alias, "PREPARED", bedrockAgentClient);

    return response.agentAlias();
}





/* 
private void chatWithAgent(AgentAlias agentAlias, BedrockAgentRuntimeClient bedrockAgentRuntimeClient) {
    System.out.println(String.join("", Collections.nCopies(88, "-")));
    System.out.println("The agent is ready to chat.");
    System.out.println("Try asking for the date or time. Type 'exit' to quit.");

    // Create a unique session ID for the conversation
    String sessionId = UUID.randomUUID().toString();

    Scanner scanner = new Scanner(System.in);
    while (true) {
        System.out.print("Prompt: ");
        String prompt = scanner.nextLine();

        if (prompt.equals("exit")) {
            break;
        }

        String response = invokeAgent(agentAlias, prompt, sessionId, bedrockAgentRuntimeClient);

        System.out.println("Agent: " + response);
    }
}



private String invokeAgent(AgentAlias agentAlias, String agentId, String prompt, String sessionId, BedrockAgentRuntimeAsyncClient bedrockAgentRuntimeAsyncClient) {
    InvokeAgentRequest request = InvokeAgentRequest.builder()
            .agentId(agentId)
            .agentAliasId(agentAlias.agentAliasId())
            .sessionId(sessionId)
            .inputText(prompt)
            .build();

    CompletableFuture<String> completionFuture = bedrockAgentRuntimeAsyncClient.invokeAgent(request, InvokeAgentResponseHandler.builder().build())
            .thenApply(response -> {
                StringBuilder completion = new StringBuilder();

                for (InvokeAgentResponse.Event event : response.completion()) {
                    SdkBytes chunk = event.chunk().bytes();
                    completion.append(new String(chunk.asByteArray(), StandardCharsets.UTF_8));
                }

                return completion.toString();
            });

    return completionFuture.toString();
}

 */


}