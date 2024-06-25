package com.kerothan.synchronyapichallenge.user;

class UserDataNotFoundException extends RuntimeException {

    UserDataNotFoundException(Long id) {
        super("Could not find user " + id);
    }
    UserDataNotFoundException(String id) {
        super("Could not find user " + id);
    }
}