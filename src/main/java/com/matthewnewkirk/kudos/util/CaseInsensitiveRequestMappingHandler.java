package com.matthewnewkirk.kudos.util;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Matt Newkirk 1/21/2016
 */

@Configuration
public class CaseInsensitiveRequestMappingHandler  extends WebMvcConfigurationSupport {
  /**
   * These beans provide case insensitivity.
   */
  @Bean
  public PathMatcher pathMatcher(){
    return new AntPathMatcher() {
      @Override
      protected boolean doMatch(String pattern, String path, boolean fullMatch,
                                Map<String, String> uriTemplateVariables) {
        return super.doMatch(pattern.toLowerCase(), path.toLowerCase(),
          fullMatch, uriTemplateVariables);
      }
    };
  }
  @Bean
  public RequestMappingHandlerMapping requestMappingHandlerMapping() {
    RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
    handlerMapping.setOrder(0);
    handlerMapping.setInterceptors(getInterceptors());
    handlerMapping.setPathMatcher(pathMatcher());
    return handlerMapping;
  }
}