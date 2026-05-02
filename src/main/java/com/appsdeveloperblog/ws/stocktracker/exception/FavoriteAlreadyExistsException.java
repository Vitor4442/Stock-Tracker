package com.appsdeveloperblog.ws.stocktracker.exception;

public class FavoriteAlreadyExistsException extends RuntimeException {
    public FavoriteAlreadyExistsException(String symbol){
        super("Symbol already saved as favorite: " + symbol);
    }
}
