package com.project.shopapp.configurations;

import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtTokenFilter jwtTokenFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix)
                                    )
                            .permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/roles**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/categories**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products**", apiPrefix)).permitAll()

                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products/images/*", apiPrefix)).permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/order_details**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)


                            .anyRequest().authenticated();

                }).csrf(AbstractHttpConfigurer::disable);

        http.cors(cors -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Auth-Token"));
            configuration.setExposedHeaders(List.of("X-Auth-Token"));
            configuration.setAllowCredentials(false);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            cors.configurationSource(source);
        });


        return http.build();

    }


}
