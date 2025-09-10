package se.ai.crypto.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import se.ai.crypto.configuration.properties.CryptoProperties;

@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties(CryptoProperties.class)
public class IpFilterInterceptor implements HandlerInterceptor {

    private CryptoProperties cryptoProperties;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        final var blockedIps = cryptoProperties.getBlockedIps();
        final var requestIp = getCorrectClientIp(request);

        if (blockedIps.contains(requestIp)) {
            // The end user are blocked, let him know
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: this IP are banned");
            log.warn("IP is blocked ({}), wont reach the endpoint", requestIp);
            return false;
        }

        log.info("IP is allowed ({}) to reach the endpoint", requestIp);
        return true;
    }

    private String getCorrectClientIp(HttpServletRequest request) {

        // If the request Ip is not the correct, we look for the header.
        // Probably needed if the app runs in a Kubernetes cluster
        final var xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {

            // If the request goes through multiple services, load balancers, etc it can be mulitple values
            String[] ips = xForwardedFor.split(",");

            // The first one is the correct end user ip
            return ips[0].trim();
        }

        return request.getRemoteAddr();
    }
}
