package com.example.SecondMicroservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {

    @NotEmpty(message = "Пустой логин")
    @Size(min = 3, max = 100, message = "Минимальное количество символов - 3, Максимальное количество символов - 100")
    private String username;
    @NotEmpty(message = "Пустой пароль")
    @Size(min = 3, max = 100, message = "Минимальное количество символов - 3, Максимальное количество символов - 100")
    private String password;

    public PersonDTO() {}

    public PersonDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class PersonDTOBuilder{

        @NotEmpty(message = "Пустой логин")
        @Size(min = 3, max = 100, message = "Минимальное количество символов - 3, Максимальное количество символов - 100")
        private String username;


        @NotEmpty(message = "Пустой пароль")
        @Size(min = 3, max = 100, message = "Минимальное количество символов - 3, Максимальное количество символов - 100")
        private String password;

        public PersonDTOBuilder setUsername(String username) {
            this.username = username;
            return this;
        }
        public PersonDTOBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public PersonDTO build() {
            return new PersonDTO(username, password);
        }

    }

    @Override
    public String toString() {
        return "PersonDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}