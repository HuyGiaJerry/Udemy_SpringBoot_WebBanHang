package com.project.shopapp.configurations;

import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.utils.NormalizeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtTokenFilter jwtTokenFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Chuẩn hóa apiPrefix
        String prefix = NormalizeUtil.normalizePrefix(apiPrefix);

        http
                .securityMatcher(prefix + "/**") // Chỉ áp dụng security cho các endpoint bắt đầu với /api/v1/
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            // Public endpoints - không cần token
                            .requestMatchers(
                                    String.format("%s/users/register", prefix),
                                    String.format("%s/users/login", prefix),
                                    String.format("%s/actuator/**", prefix),

                                    "/api-docs",
                                    "/api-docs/**",
                                    "/swagger-resources",
                                    "/swagger-resources/**",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/webjars/swagger-ui/**",
                                    "/swagger-ui/index.html"
                            ).permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/roles**", prefix)).permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/categories/**", prefix)).permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products/**", prefix)).permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products/images/*", prefix)).permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/orders/**", prefix)).permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/order_details/**", prefix)).permitAll()
                            
//                            // Orders endpoints
//                            .requestMatchers(HttpMethod.POST,
//                                    String.format("%s/orders/**", prefix)).hasAnyRole(Role.USER, Role.ADMIN)
//                            .requestMatchers(HttpMethod.PUT,
//                                    String.format("%s/orders/**", prefix)).hasRole(Role.ADMIN)
////                            .requestMatchers(HttpMethod.GET,
////                                    String.format("%s/orders/**", prefix)).permitAll()
//                            .requestMatchers(HttpMethod.DELETE,
//                                    String.format("%s/orders/**", prefix)).hasRole(Role.ADMIN)
//
//                            // Categories endpoints
//                            .requestMatchers(HttpMethod.GET,
//                                    String.format("%s/categories**", prefix)).permitAll()
//                            .requestMatchers(HttpMethod.PUT,
//                                    String.format("%s/categories/**", prefix)).hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.DELETE,
//                                    String.format("%s/categories/**", prefix)).hasRole(Role.ADMIN)
//
//                            // Products endpoints
//                            .requestMatchers(HttpMethod.GET, String.format("%s/products", prefix)).permitAll()
//                            .requestMatchers(HttpMethod.GET, String.format("%s/products/**", prefix)).permitAll()
//                            .requestMatchers(HttpMethod.POST,
//                                    String.format("%s/products/**", prefix)).hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.PUT,
//                                    String.format("%s/products/**", prefix)).hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.DELETE,
//                                    String.format("%s/products/**", prefix)).hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.GET,
//                                    String.format("%s/products/images/*", prefix)).permitAll()
//
//                            // Order details endpoints
//                            .requestMatchers(HttpMethod.GET,
//                                    String.format("%s/order_details**", prefix)).hasAnyRole(Role.USER, Role.ADMIN)
//                            .requestMatchers(HttpMethod.POST,
//                                    String.format("%s/order_details/**", prefix)).hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.PUT,
//                                    String.format("%s/order_details/**", prefix)).hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.DELETE,
//                                    String.format("%s/order_details/**", prefix)).hasRole(Role.ADMIN)

                            .anyRequest()
                            .authenticated();


                }).csrf(AbstractHttpConfigurer::disable);

//        http.cors(cors -> {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(List.of("*"));
//            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//            configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Auth-Token"));
//            configuration.setExposedHeaders(List.of("X-Auth-Token"));
//            configuration.setAllowCredentials(false);
//
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", configuration);
//            cors.configurationSource(source);
//        });

        return http.build();
    }

}