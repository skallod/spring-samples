//package ru.galuzin.springrest.conf;
//
//import com.fasterxml.classmate.TypeResolver;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import ru.galuzin.springrest.dto.ErrorResponse;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RepresentationBuilder;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.builders.ResponseBuilder;
//import springfox.documentation.service.Response;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger.web.*;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//@EnableWebMvc
//@Configuration(proxyBeanMethods = false)
//public class SwaggerConf implements WebMvcConfigurer {
//
//    @Autowired
//    private TypeResolver typeResolver;
//
//    @Bean
//    @ConditionalOnMissingBean
//    public UiConfiguration uiConfig() {
//        return UiConfigurationBuilder.builder()
//                .deepLinking(true)
//                .displayOperationId(false)
//                .defaultModelsExpandDepth(1)
//                .defaultModelExpandDepth(1)
//                .defaultModelRendering(ModelRendering.EXAMPLE)
//                .displayRequestDuration(false)
//                .docExpansion(DocExpansion.NONE)
//                .filter(false)
//                .maxDisplayedTags(null)
//                .operationsSorter(OperationsSorter.ALPHA)
//                .showExtensions(false)
//                .tagsSorter(TagsSorter.ALPHA)
//                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
//                .validatorUrl(null)
//                .build();
//    }
//
//    @Bean
//    public Docket docket() {
//        return new Docket(DocumentationType.OAS_30)
////                .securitySchemes(Collections.singletonList(new ApiKey("JWT", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("ru.galuzin.springrest"))
//                .paths(PathSelectors.any())
//                .build()
//                .forCodeGeneration(true)
//                .globalResponses(HttpMethod.POST,
//                        Collections.singletonList(responseMessage("500")))
//                .globalResponses(HttpMethod.GET,
//                        Arrays.asList(responseMessage("500")
//                                ,responseMessage("400")
//                                ,responseMessage("401")
//                                ,responseMessage("403")
//                        ))
//                .additionalModels(typeResolver.resolve(ErrorResponse.class)
////                        ,typeResolver.resolve(ErrorMessage.class),
////                        typeResolver.resolve(ApiErrorMessage.class),
////                        typeResolver.resolve(FieldErrorMessage.class)
//                );
//    }
//
//    private static Response responseMessage(String code) {
//        return new ResponseBuilder().code(code)
//                .description("error_message_" + code)
//                .representation(MediaType.APPLICATION_JSON)
//                .apply(SwaggerConf::buildDefaultModel)
//                .isDefault(true)
//                .build();
//    }
//
//    private static void buildDefaultModel(final RepresentationBuilder representationBuilder) {
//        representationBuilder.model(
//                msBuilder ->
//                        msBuilder.name("ErrorResponse")
//                                .referenceModel(rmsBuilder ->
//                                        rmsBuilder.key(
//                                                mkBuilder ->
//                                                        mkBuilder.isResponse(true)
//                                                                .qualifiedModelName(
//                                                                        qmnBuilder -> qmnBuilder.name("ErrorResponse")
//                                                                                //package of your model class
//                                                                                .namespace("ru.galuzin.springrest.dto")
//                                                                                .build())
//                                                                .build())
//                                                .build())
//                                .build());
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/swagger-ui/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
//                .resourceChain(false);
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/swagger-ui/")
//                .setViewName("forward:" + "/swagger-ui/index.html");
//    }
//}
