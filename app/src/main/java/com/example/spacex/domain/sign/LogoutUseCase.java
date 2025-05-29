package com.example.spacex.domain.sign;

public class LogoutUseCase {

    private final SignRepository repo;

    public LogoutUseCase(SignRepository repo) {
        this.repo = repo;
    }

    public void execute(){
        repo.logout();
    }
}
