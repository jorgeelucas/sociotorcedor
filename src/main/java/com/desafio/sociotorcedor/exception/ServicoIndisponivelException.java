package com.desafio.sociotorcedor.exception;
public class ServicoIndisponivelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServicoIndisponivelException(String msg) {
        super(msg);
    }

    public ServicoIndisponivelException(String msg, Throwable cause) {
        super(msg, cause);
    }

}