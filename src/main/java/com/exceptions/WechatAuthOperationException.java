package com.exceptions;

public class WechatAuthOperationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1150383834093371683L;

	public WechatAuthOperationException(String msg) {
		super(msg);
	}
}
