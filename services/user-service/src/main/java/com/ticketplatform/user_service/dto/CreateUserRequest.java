package com.ticketplatform.user_service.dto;

public record CreateUserRequest(String name,
                                String email,
                                String password) {

}
