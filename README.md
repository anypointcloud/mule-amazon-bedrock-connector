# AWS Bedrock Overview

[**AWS Bedrock**](https://aws.amazon.com/bedrock) is a fully managed service that offers a choice of high-performing foundation models (FMs) from leading AI companies including AI21 Labs, Anthropic, Cohere, Meta, Stability AI, and Amazon, along with a broad set of capabilities that you need to build generative AI applications, simplifying development while maintaining privacy and security.
- Amazon Bedrock offers a choice of leading FM’s through a Single API
- Using Amazon Bedrock, you can easily experiment with and evaluate top FMs for your use case, privately customize them with your data using techniques such as fine-tuning and Retrieval Augmented Generation (RAG)
- Build agents that execute multistep tasks using your enterprise systems and data sources
- Since Amazon Bedrock is serverless, you don't have to manage any infrastructure, and you can securely integrate and deploy generative AI capabilities into your applications using the AWS services

## Why MAC AWS Bedrock Connector?

AWS Bedrock provides an unified AI Platform to design, build and manage autonomous agents and the needed architecture. While AWS Bedrock is very strong in connecting multiple LLM providers, the way to interact varies from LLM to LLM. The MAC AWS Bedrock Connector provides the ability to connect to all supported LLMs through an unification layer. 


## Learn more at [mac-project.ai](https://mac-project.ai/docs/aws-bedrock/connector-overview)


Add this dependency to your application pom.xml

```
<dependency>
    <groupId>com.mule.mulechain</groupId>
    <artifactId>mulechain-bedrock</artifactId>
    <version>0.1.0</version>
    <classifier>mule-plugin</classifier>
</dependency>
```
