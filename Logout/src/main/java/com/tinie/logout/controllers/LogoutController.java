package com.tinie.logout.controllers;

import com.tinie.logout.config.JWTProcessor;
import com.tinie.logout.repositories.LoginEntryRepository;
import com.tinie.logout.requests.LogoutRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class LogoutController {

    @Autowired
    JWTProcessor jwtProcessor;
    @Autowired
    LoginEntryRepository loginEntryRepository;

    /**
     * Verify token is valid and invalidate all existing tokens
     * @param httpServletRequest An object of type {@link HttpServletRequest} containing all the information about the request.
     * @param requestBody {@link LogoutRequest} containing phone number
     * @return A {@link Response} whose payload is a {@link Map}.
     * */
    @PostMapping("user-logout")
    @ApiOperation(value = "Verify Token still valid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESSFUL"),
            @ApiResponse(code = 401, message = "INVALID TOKEN"),
            @ApiResponse(code = 500, message = "LOGOUT FAILED")
    })
    public ResponseEntity<?> userLogout(HttpServletRequest httpServletRequest,
                                        @RequestBody LogoutRequest requestBody) {
        var decodedJWT = jwtProcessor
                .verifyAndDecodeToken(requestBody.phonenumber(), httpServletRequest.getHeader("token"));

        var entryOptional = loginEntryRepository.findByPhoneNumber(Long.parseLong(decodedJWT.getSubject()));

        var response = new HashMap<String, String>();
        if (entryOptional.isPresent()){
            var entry = entryOptional.get();
            entryOptional.get().setLastLogin(new Date().getTime());

            loginEntryRepository.save(entry);

            response.put("phonenumber", decodedJWT.getSubject());
            response.put("event", "loggedout");
        } else {
            response.put("phonenumber", decodedJWT.getSubject());
            response.put("event", "logoutfailed");
        }

        return new ResponseEntity<>(response, entryOptional.isPresent() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
