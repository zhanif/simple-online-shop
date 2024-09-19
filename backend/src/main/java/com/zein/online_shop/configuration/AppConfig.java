package com.zein.online_shop.configuration;

import com.zein.online_shop.dto.response.OrderResponse;
import com.zein.online_shop.dto.response.SearchOptionResponse;
import com.zein.online_shop.model.Customer;
import com.zein.online_shop.model.Item;
import com.zein.online_shop.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableJpaAuditing
@EnableMethodSecurity
public class AppConfig {
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPreferNestedProperties(false);

        modelMapper.typeMap(Order.class, OrderResponse.class).addMappings(mapper -> {
           mapper.map(src -> src.getCustomer().getId(), OrderResponse::setCustomerId);
           mapper.map(src -> src.getItem().getId(), OrderResponse::setItemId);
        });

        modelMapper.typeMap(Customer.class, SearchOptionResponse.class).addMappings(mapper -> {
            mapper.map(Customer::getId, SearchOptionResponse::setId);
            mapper.map(Customer::getName, SearchOptionResponse::setValue);
        });

        modelMapper.typeMap(Item.class, SearchOptionResponse.class).addMappings(mapper -> {
            mapper.map(Item::getId, SearchOptionResponse::setId);
            mapper.map(Item::getName, SearchOptionResponse::setValue);
        });

        return modelMapper;
    }
}
