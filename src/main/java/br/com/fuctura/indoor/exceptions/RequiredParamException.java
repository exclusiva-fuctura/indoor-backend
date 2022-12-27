package br.com.fuctura.indoor.exceptions;

public class RequiredParamException extends Exception {

	private static final long serialVersionUID = 1L;

	public RequiredParamException(String msg) {
		super(msg);
	}
}
