package br.com.fuctura.indoor.exceptions;

public class SituacaoNotExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public SituacaoNotExistsException(String msg) {
		super(msg);
	}
}
