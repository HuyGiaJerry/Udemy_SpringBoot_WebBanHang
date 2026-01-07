package com.project.shopapp.filters;

import com.project.shopapp.models.User;
import com.project.shopapp.utils.JwtToken;
import com.project.shopapp.utils.NormalizeUtil;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;
    private final JwtToken jwtToken;


    // Hàm lọc filter chính cho mỗi request implement from OncePerRequestFilter
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Nếu là endpoint public -> bỏ qua filter
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. Endpoint tồn tại và không public -> kiểm tra token
            final String authHeader = request.getHeader("Authorization");

            // Nếu không có token nhưng endpoint tồn tại -> 401
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                return;
            }

            // Kiểm tra token hợp lệ
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtToken.extractPhoneNumber(token);

            if (phoneNumber == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetail = (User) userDetailsService.loadUserByUsername(phoneNumber);

                if (!jwtToken.isValidateToken(token, userDetail)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(e.getMessage());
        }
    }

    // Kiểm tra endpoint public (bypassToken)
    private boolean isByPassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),

                Pair.of(String.format("%s/products/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/roles/**", apiPrefix), "GET"),

                // Healthcheck and Actuator
                Pair.of(String.format("%s/healthcheck/health", apiPrefix), "GET"),
                Pair.of(String.format("%s/actuator/**", apiPrefix), "GET"),

                // Swagger
                Pair.of("/api-docs", "GET"),
                Pair.of("/api-docs/**", "GET"),
                Pair.of("/swagger-resources", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of("/configuration/ui", "GET"),
                Pair.of("/configuration/security", "GET"),
                Pair.of("/swagger-ui/**", "GET" ),
                Pair.of("/swagger-ui.html", "GET" ),
                Pair.of("/swagger-ui/index.html", "GET")

        );

        String requestUri = request.getRequestURI();
        String context = request.getContextPath() == null ? "" : request.getContextPath();
        String requestPath = requestUri.startsWith(context) ? requestUri.substring(context.length()) : requestUri;
        String requestMethod = request.getMethod();

        for (Pair<String, String> pair : byPassTokens) {
            // Chuẩn hóa đường dẫn token
            String tokenPath = NormalizeUtil.normalizePath(pair.getFirst());
            String tokenMethod = pair.getSecond();

            if (tokenPath.endsWith("/**")) {
                String fixed = tokenPath.substring(0, tokenPath.length() - 3);
                String regex = "^" + Pattern.quote(fixed) + "(/.*)?$";
                if (Pattern.matches(regex, requestPath) && requestMethod.equalsIgnoreCase(tokenMethod)) {
                    return true;
                }
            } else {
                if (requestPath.equals(tokenPath) && requestMethod.equalsIgnoreCase(tokenMethod)) {
                    return true;
                }
            }
        }
        return false;
    }
}