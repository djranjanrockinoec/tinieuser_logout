package com.tinie.logout.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tinie.logout.exceptions.UnauthorisedException;
import com.tinie.logout.repositories.LoginEntryRepository;
import com.tinie.logout.util.EnvConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JWTProcessor {

    @Autowired
    EnvConstants envConstants;
    @Autowired
    LoginEntryRepository loginEntryRepository;

    public DecodedJWT verifyAndDecodeToken(long phoneNumber, String token){
        var decodedJwt =  JWT.require(Algorithm.HMAC512(envConstants.getTokenSecret()))
                .build()
                .verify(token);

        var loginEntryOptional = loginEntryRepository
                .findByPhoneNumber(Long.parseLong(decodedJwt.getSubject()));
        if (loginEntryOptional.isEmpty() || decodedJwt.getIssuedAt().getTime() < loginEntryOptional.get().getLastLogin())
            throw new UnauthorisedException("Invalid Token", phoneNumber);

        return decodedJwt;
    }
}
