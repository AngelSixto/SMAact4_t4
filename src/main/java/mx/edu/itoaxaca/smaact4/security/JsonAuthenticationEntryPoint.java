package mx.edu.itoaxaca.smaact4.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Se ejecuta cuando alguien llama a un endpoint protegido SIN token o con un token
 * invalido/expirado. Responde 401 Unauthorized en formato JSON.
 */
@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        boolean traeToken = request.getHeader(HttpHeaders.AUTHORIZATION) != null;
        String mensaje = traeToken
                ? "El token JWT es invalido o ya expiro. Vuelve a hacer login."
                : "Se requiere autenticacion. Envia el header Authorization: Bearer <token>.";

        String json = """
                {"timestamp":"%s","status":401,"error":"Unauthorized","mensaje":"%s","ruta":"%s","errores":null}
                """.formatted(OffsetDateTime.now(), mensaje, request.getRequestURI()).trim();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(json);
    }
}
