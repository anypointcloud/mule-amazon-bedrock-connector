package org.mule.extension.mulechain.internal.agents;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.extension.mulechain.helpers.AwsbedrockAgentsPayloadHelper;

import org.mule.extension.mulechain.internal.AwsbedrockConfiguration;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class AwsbedrockAgentsOperations {



  /**
   * Lists all agents for the configuration
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-list")
  public String listAgents(@Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.ListAgents(configuration, awsBedrockParameters);
    return response;
  }


  /**
   * Get agent by its Id
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-get-by-id")
  public String getAgentById(String agentId, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.getAgentbyAgentId(agentId, configuration, awsBedrockParameters);
    return response;
  }

  

  /**
   * Get agent by its Name
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-get-by-name")
  public String getAgentByName(String agentName, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.getAgentbyAgentName(agentName, configuration, awsBedrockParameters);
    return response;
  }


  /**
   * Delete agent by its Id
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-delete-by-id")
  public String deleteAgentById(String agentId, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.deleteAgentByAgentId(agentId, configuration, awsBedrockParameters);
    return response;
  }


  
  /**
   * Creates an agent with alias
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-create")
  public String createAgentWithAlias(String agentName, String instructions, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.createAgentOperation(agentName, instructions, configuration, awsBedrockParameters);
    return response;
  }

  /**
   * Creates an agent alias
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-create-alias")
  public String createAgentAlias(String agentAlias, String agentId, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.createAgentAlias(agentAlias, agentId, configuration, awsBedrockParameters);
    return response;
  }



  /**
   * Get agent alias by its Id
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-get-alias-by-agent-id")
  public String getAgentAliasById(String agentId, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.listAllAgentAliases(agentId, configuration, awsBedrockParameters);
    return response;
  }


   /**
   * Get agent alias by its Id
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-delete-agent-aliases")
  public String deleteAgentAlias(String agentId, String agentAliasName, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.deleteAgentAliasesByAgentId(agentId, agentAliasName, configuration, awsBedrockParameters);
    return response;
  }


   /**
   * Chat with an agent
   */
  @MediaType(value = ANY, strict = false)
  @Alias("AGENT-chat")
  public String chatWithAgent(String agentId, String agentAliasId, String prompt, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.chatWithAgent(agentAliasId, agentId, prompt, configuration, awsBedrockParameters);
    return response;
  }
  

}
