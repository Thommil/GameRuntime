package com.thommil.libgdx.runtime;

/**
 * Generic exception for GameRuntime FWK
 *
 * Created by thommil on 2/11/16.
 */
public class GameRuntimeException extends RuntimeException{

    public GameRuntimeException(String message) {
        super(message);
    }

    public GameRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
