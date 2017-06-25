package com.wyw.ljtds.biz.exception;

@SuppressWarnings("serial")
public class NetWorkNotAvailableException extends ZYException {

	public NetWorkNotAvailableException() {
		super();
	}

	public NetWorkNotAvailableException(String message) {
		super(message);
	}

	public NetWorkNotAvailableException(Throwable e) {
		super(e);
	}

}
