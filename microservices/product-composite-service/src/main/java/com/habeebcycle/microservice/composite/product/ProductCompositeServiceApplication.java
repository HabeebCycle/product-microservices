package com.habeebcycle.microservice.composite.product;

import com.habeebcycle.microservice.composite.product.integration.ProductCompositeIntegration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@SpringBootApplication
@ComponentScan("com.habeebcycle")
public class ProductCompositeServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceApplication.class);

	@Autowired
	ProductCompositeIntegration integration;

	@Value("${api.common.version}")           String apiVersion;
	@Value("${api.common.title}")             String apiTitle;
	@Value("${api.common.description}")       String apiDescription;
	@Value("${api.common.termsOfServiceUrl}") String apiTermsOfServiceUrl;
	@Value("${api.common.license}")           String apiLicense;
	@Value("${api.common.licenseUrl}")        String apiLicenseUrl;
	@Value("${api.common.contact.name}")      String apiContactName;
	@Value("${api.common.contact.url}")       String apiContactUrl;
	@Value("${api.common.contact.email}")     String apiContactEmail;

	/**
	 * Will be exposed on $HOST:$PORT/swagger-ui.html
	 *
	 * @return the documentation page.
	 */
	@Bean
	public Docket apiDocumentation() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.habeebcycle.microservice.composite.product"))
				.paths(PathSelectors.any())
				.build()
				.globalResponses(HttpMethod.GET, Collections.emptyList())
				.globalResponses(HttpMethod.POST, Collections.emptyList())
				.globalResponses(HttpMethod.DELETE, Collections.emptyList())
				.tags(new Tag("product-composite-service", "REST API for composite product information."))
				.apiInfo(new ApiInfo(
						apiTitle, apiDescription, apiVersion, apiTermsOfServiceUrl,
						new Contact(apiContactName, apiContactUrl, apiContactEmail),
						apiLicense, apiLicenseUrl, Collections.emptyList()
				));
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}

	// Configure the webclient to load balance. It calls the instances in a round robin version
	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}

}
