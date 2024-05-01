package id.ac.ui.cs.advprog.bookku.bookku_userauth.service;

import id.ac.ui.cs.advprog.bookku.bookku_userauth.exceptions.*;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.model.Account;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.AccountRepository;
import id.ac.ui.cs.advprog.bookku.bookku_userauth.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final String SECRET_KEY = "theSecretKeyIsTawakkulAndDoMyBestToGetTheBestResult";

    public String generateAccessToken(Account account) {
        var expirationDate = getExpirationDateForAccessToken();
        return (generateToken(account, expirationDate));
    }

    public String generateRefreshToken(Account account) {
        var expirationDate = getExpirationDateForRefreshToken();
        return (generateToken(account, expirationDate));
    }

    private String generateToken(Account account, Date expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", account.getUsername());
        claims.put("role", account.getRole());

        var currentDate = getCurrentDate();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(Integer.toString(account.getId()))
                .setIssuedAt(currentDate)
                .setExpiration(expirationTime)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String accessToken){
        extractAllClaimsFromToken(accessToken);

        if(isTokenClaimValid(accessToken)){
            return true;
        }

        throw new InvalidTokenException();

    }

    public boolean validateRefreshToken(String refreshToken){
        extractAllClaimsFromToken(refreshToken);

        if (refreshTokenExists(refreshToken)){
            return true;
        }

        throw new InvalidTokenException();
    }

    private boolean isTokenClaimValid(String token){

        var account = getAccountFromToken(token);
        if (account == null){
            return  false;
        }

        String username = account.getUsername();
        if (!username.equals(extractUsername(token))){
            return false;
        }

        String role = account.getRole();
        if (!role.equals(extractRole(token))){
            return false;
        }

        return true;
    }

    private boolean refreshTokenExists(String token){
        return refreshTokenRepository.existsByToken(token);
    }

    public String extractId(String token) {
        return extractAllClaimsFromToken(token).getSubject();
    }

    public String extractUsername(String token) {
        return extractAllClaimsFromToken(token).get("username").toString();
    }

    public String extractRole(String token) {
        return extractAllClaimsFromToken(token).get("role").toString();
    }

    private Claims extractAllClaimsFromToken(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            throw new ExpiredTokenException();
        } catch (RuntimeException e){
            throw new InvalidTokenException();
        }
    }

    public Account getAccountFromToken(String token) {
        return accountRepository
                .findById(Integer.parseInt(extractId(token)))
                .orElse(null);
    }

    public void deleteRefreshTokenByAccount(Account account) {
        refreshTokenRepository.findByAccount(account)
                .ifPresent(refreshTokenRepository::delete);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date getExpirationDateForAccessToken() {
        return Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC).plusSeconds( 60L * 30L)); // 30 minutes
    }

    private Date getExpirationDateForRefreshToken() {
        return Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC).plusSeconds( 60L * 60L * 2L)); // 2 hour
    }

    private Date getCurrentDate() {
        return Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    }

}