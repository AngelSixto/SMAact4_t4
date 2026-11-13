package mx.edu.itoaxaca.smaact4.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import mx.edu.itoaxaca.smaact4.models.entity.Usuario;

/**
 * Genera tokens JWT firmados con HMAC-SHA256 (HS256) usando la clave secreta del servidor.
 * La VALIDACION del token la hace el JwtDecoder configurado en SecurityConfig.
 */
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expiracionMinutos;

    public JwtService(JwtEncoder jwtEncoder,
                      @Value("${jwt.issuer}") String issuer,
                      @Value("${jwt.expiracion-minutos}") long expiracionMinutos) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expiracionMinutos = expiracionMinutos;
    }

    public String generarToken(Usuario usuario) {
        Instant ahora = Instant.now();

        // Claims (datos) que viajan dentro del token
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)                                           // iss: quien emite
                .issuedAt(ahora)                                          // iat: cuando se emitio
                .expiresAt(ahora.plus(expiracionMinutos, ChronoUnit.MINUTES)) // exp: cuando expira
                .subject(usuario.getEmail())                              // sub: a quien pertenece
                .claim("uid", usuario.getId())
                .claim("nombre", usuario.getNombre())
                .claim("rol", usuario.getRol().name())
                .build();

        // Encabezado: algoritmo de firma HS256
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public long getExpiracionSegundos() {
        return expiracionMinutos * 60;
    }
}
