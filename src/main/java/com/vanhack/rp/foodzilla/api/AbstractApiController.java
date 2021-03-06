package com.vanhack.rp.foodzilla.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.jr.ob.JSONObjectException;
import com.vanhack.rp.foodzilla.exception.AuthenticationException;
import com.vanhack.rp.foodzilla.exception.DuplicatedException;
import com.vanhack.rp.foodzilla.exception.NotFoundException;
import com.vanhack.rp.foodzilla.exception.UnmetRequirementException;
import com.vanhack.rp.foodzilla.service.ConfigService;

public abstract class AbstractApiController {

	private Logger log = Logger.getLogger(AbstractApiController.class);
	
	@Autowired
	private ConfigService configService;

	protected void validateAuthenticationToken(String token) throws JSONObjectException, IOException {
		log.trace(String.format("Auth token is '%s', args)", token));
		if (token == null || token.trim().isEmpty()) {
			String msg = String.format("Invalid authentication token (%s)", token);
			log.debug(msg);
			throw new AuthenticationException(msg);
		}
		String authToken = configService.getAuthToken();
		if (!token.equals(authToken)) {
			String msg = String.format("Invalid authentication token (%s)", token);
			log.debug(msg);
			throw new AuthenticationException(msg);
		}
	}

	@ExceptionHandler(Exception.class)
	public void handleError(HttpServletRequest req, HttpServletResponse res, Exception e) {
		if (e instanceof AuthenticationException) {
			String msg = String.format("Authentication failure: %s", e.getMessage());
			sendError(res,HttpServletResponse.SC_UNAUTHORIZED, msg);
			
		} else if (e instanceof NotFoundException) {
			String msg = String.format("Not found: %s", e.getMessage());
			sendError(res,HttpServletResponse.SC_NOT_FOUND, msg);
			
		} else if (e instanceof DuplicatedException) {
			String msg = String.format("Duplicated: %s", e.getMessage());
			sendError(res,HttpServletResponse.SC_CONFLICT, msg);
			
		} else if (e instanceof UnmetRequirementException) {
			String msg = String.format("Requirement not met to perform operation: %s", e.getMessage());
			sendError(res,HttpServletResponse.SC_BAD_REQUEST, msg);
			
		} else if (e instanceof IllegalArgumentException) {
			String msg = String.format("Bad request: %s", e.getMessage());
			sendError(res,HttpServletResponse.SC_BAD_REQUEST, msg);
			
		} else if (e instanceof HttpMessageConversionException) {
			String msg = String.format("Http message format unexpected: %s", e.getMessage());
			sendError(res,HttpServletResponse.SC_BAD_REQUEST, msg);
			
		} else if (e instanceof ServletRequestBindingException) {
			String msg = String.format("Missing parameter: %s", e.getMessage());
			sendError(res,HttpServletResponse.SC_BAD_REQUEST, msg);
			
		} else {
			String msg = String.format("Unexpected exception: %s", e.getMessage());
			log.error("Exception wasn't dealt with by the business controller", e);
			sendError(res,HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
		}
	}
	
	private void sendError(HttpServletResponse res, int code, String msg) {
		try {
			log.debug(msg);
			res.sendError(code, msg);
		} catch (IOException ioe) {
			log.error("Error sending response", ioe);
		}
	}
}
