package com.desafio.sociotorcedor.exception;
public class UsuarioJaExisteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsuarioJaExisteException(String msg) {
        super(msg);
    }

    public UsuarioJaExisteException(String msg, Throwable cause) {
        super(msg, cause);
    }

}