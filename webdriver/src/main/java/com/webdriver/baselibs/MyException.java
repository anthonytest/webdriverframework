package com.webdriver.baselibs;

/**
 * @author Anthony
 *
 */
@SuppressWarnings("serial")
public class MyException extends RuntimeException {

	public static void main(String[] args) {

		try {
			throw new MyException("TEST");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MyException() {
		super();
	}

	public MyException(String message) {
		super(message);
	}

	public MyException(Throwable cause) {
		super(cause);
	}

	public MyException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
