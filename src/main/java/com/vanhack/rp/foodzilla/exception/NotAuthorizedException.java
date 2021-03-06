package com.vanhack.rp.foodzilla.exception;


@SuppressWarnings("serial")
public class NotAuthorizedException extends Exception {

	public NotAuthorizedException() {
		super();
	}

	public NotAuthorizedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAuthorizedException(String message) {
		super(message);
	}

	public NotAuthorizedException(Throwable cause) {
		super(cause);
	}

}
