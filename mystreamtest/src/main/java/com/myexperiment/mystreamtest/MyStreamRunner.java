package com.myexperiment.mystreamtest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.dataflow.core.ApplicationType;
import org.springframework.cloud.dataflow.rest.client.DataFlowOperations;
import org.springframework.cloud.dataflow.rest.client.dsl.DeploymentPropertiesBuilder;
import org.springframework.cloud.dataflow.rest.client.dsl.Stream;
import org.springframework.cloud.dataflow.rest.client.dsl.StreamApplication;
import org.springframework.cloud.dataflow.rest.client.dsl.StreamBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MyStreamRunner implements ApplicationRunner {

	@Autowired
	private StreamApplication source;

	@Autowired
	private StreamApplication processor;

	@Autowired
	private StreamApplication sink;

	@Autowired
	private DataFlowOperations dataFlowOperations;

	@Autowired
	private StreamBuilder builder;

	// Using @Bean defintions makes it easier to reuse an application in multiple streams
	@Bean
	public StreamApplication source() {
		return new StreamApplication("http").addProperty("server.port", 9900);
	}

	@Bean
	public StreamApplication processor() {
		return new StreamApplication("splitter")
				.addProperty("producer.partitionKeyExpression", "payload");
	}

	@Bean
	public StreamApplication sink() {
		return new StreamApplication("log")
				.addDeploymentProperty("count", 2);
	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		importApplications();
		if (applicationArguments.containsOption("style")) {
			String style = applicationArguments.getOptionValues("style").get(0);
			if (style.equalsIgnoreCase("definition")) {
				// DEFINITION STYLE
				definitionStyle(dataFlowOperations);
			} else if (style.equalsIgnoreCase("fluent")) {
				// FLUENT STYLE
				fluentStyle(dataFlowOperations);
			} else {
				log.info("Style [" + style + "] not supported");
			}
		} else {
			definitionStyle(dataFlowOperations);
		}
	}


	private void definitionStyle(DataFlowOperations dataFlowOperations) throws InterruptedException {
		Map<String, String> deploymentProperties = createDeploymentProperties();

		log.info("Deploying stream.");

		Stream woodchuck = builder
				.name("woodchuck")
				.definition(
						"http --server.port=9900 | splitter --expression=payload.split(' ') | log")
				.create()
				.deploy(deploymentProperties);

		waitAndDestroy(woodchuck);
	}

	private void importApplications() {
		this.dataFlowOperations.appRegistryOperations().register("log", ApplicationType.sink,
				"maven://org.springframework.cloud.stream.app:log-sink-kafka:3.2.1",
				null,
				true);
		this.dataFlowOperations.appRegistryOperations().register("splitter", ApplicationType.processor,
				"maven://org.springframework.cloud.stream.app:splitter-processor-kafka:3.2.1",
				null,
				true);
		this.dataFlowOperations.appRegistryOperations().register("http", ApplicationType.source,
				"maven://org.springframework.cloud.stream.app:http-source-kafka:3.2.1",
				null,
				true);
	}

	private void fluentStyle(DataFlowOperations dataFlowOperations) throws InterruptedException {

		log.info("Deploying stream.");

		Stream woodchuck = builder
				.name("woodchuck")
				.source(source)
				.processor(processor)
				.sink(sink)
				.create()
				.deploy();

		waitAndDestroy(woodchuck);
	}

	private Map<String, String> createDeploymentProperties() {
		DeploymentPropertiesBuilder propertiesBuilder = new DeploymentPropertiesBuilder();
		propertiesBuilder.memory("log", 512);
		propertiesBuilder.count("log",2);
		propertiesBuilder.put("app.splitter.producer.partitionKeyExpression", "payload");
		return propertiesBuilder.build();
	}


	private void waitAndDestroy(Stream stream) throws InterruptedException {
		while(!stream.getStatus().equals("deployed")){
			log.info("Wating for deployment of stream.");
			Thread.sleep(5000);
		}

		log.info("Letting the stream run for 2 minutes.");
		// Let woodchuck run for 2 minutes
		Thread.sleep(120000);

		log.info("Destroying stream");
		stream.destroy();
	}

}