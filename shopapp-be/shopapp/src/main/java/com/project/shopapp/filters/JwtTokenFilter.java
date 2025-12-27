package com.project.shopapp.filters;

import com.project.shopapp.models.User;
import com.project.shopapp.utils.JwtToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {


    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtToken jwtToken;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authenticationHeader = request.getHeader("Authorization");
            if(authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

                final String token = authenticationHeader.substring(7);
                // Lấy ra sdt từ token
                final String phoneNumber = jwtToken.extractPhoneNumber(token);

                if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User userDetail = (User) userDetailsService.loadUserByUsername(phoneNumber);
                    if (jwtToken.isValidateToken(token, userDetail)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetail, null, userDetail.getAuthorities());

                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                    filterChain.doFilter(request, response);

                }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }


    }

    private boolean isByPassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(

                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),

                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/roles", apiPrefix), "GET")

        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        if(requestPath.equals(String.format("%s/orders", apiPrefix))
           && requestMethod.equals("GET")) {
            // allow access api/v1/orders without JWT
            return true;
        }

        for (Pair<String, String> pair : byPassTokens) {
            if (request.getServletPath().contains(pair.getFirst()) &&
                    request.getMethod().equals(pair.getSecond())) {
                // Bỏ qua xác thực JWT cho các endpoint này
                return true;
            }
        }
        return false;
    }


}
