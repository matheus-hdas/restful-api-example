package com.matheushdas.restfulapi.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.origin-patterns:default}")
    private String corsPatterns = "";

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("yml", MediaType.valueOf("application/x-yaml"));
    }

//   @Override
//   public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//       converters.add(new YamlHttpMessageConverter());
//   }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());

        converters.add(new MappingJackson2XmlHttpMessageConverter(new XmlMapper()));

        MappingJackson2HttpMessageConverter yamlConverter = new MappingJackson2HttpMessageConverter(new YAMLMapper());
        yamlConverter.setSupportedMediaTypes(List.of(MediaType.parseMediaType("application/x-yaml")));
        converters.add(yamlConverter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = corsPatterns.split(",");

        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns(allowedOrigins)
                .allowCredentials(true);
    }
}
