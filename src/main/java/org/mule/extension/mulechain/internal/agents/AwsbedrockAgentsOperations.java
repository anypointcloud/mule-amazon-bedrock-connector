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
  @Alias("AGENT-create-with-alias")
  public String createAgentWithAlias(String agentName, String agentAlias, String instructions, @Config AwsbedrockConfiguration configuration, @ParameterGroup(name= "Additional properties") AwsbedrockAgentsParameters awsBedrockParameters){
      String response = AwsbedrockAgentsPayloadHelper.createAgentOperation(agentName, agentAlias, instructions, configuration, awsBedrockParameters);
    return response;
  }

  

}
