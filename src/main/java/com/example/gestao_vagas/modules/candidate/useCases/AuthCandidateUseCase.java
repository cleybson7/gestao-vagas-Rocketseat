package com.example.gestao_vagas.modules.candidate.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.gestao_vagas.modules.candidate.CandidateRepository;
import com.example.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import com.example.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthCandidateUseCase {

    @Value("${secret.token.secret.candidate}")
    private String secretKey;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) throws AuthenticationException {
        var candidate = candidateRepository.findByUsername(authCandidateRequestDTO.username())
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário não encontrado")
                );
        var passwordMatches = passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

        if (!passwordMatches) {
            throw new BadCredentialsException("Senha inválida");
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(30));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(candidate.getId().toString())
                .withClaim("roles", Arrays.asList("candidate"))
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        var authCandidateResponse = AuthCandidateResponseDTO.builder()
                .access_token(token)
                .expires_in(expiresIn.toEpochMilli())
                .build();

        return authCandidateResponse;
    }
}
