package com.aftership.tracking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoshipConfiguration {

    @Value("${aftership-api-key}")
    private String apiKey;

    @Value("${aftership-api-url}")
    private String apiServiceUrl;

//    @Bean
//    public AfterShip afterShip() throws SdkException {
//        return new AfterShip(apiKey, aftershipOption());
//    }
//
//    private AftershipOption aftershipOption() {
//        AftershipOption option = new AftershipOption();
//        option.setEndpoint(apiServiceUrl);
//        return option;
//    }
}
