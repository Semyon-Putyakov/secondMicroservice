package com.example.SecondMicroservice.dto;

public class PersonDTO {
    private String username;
    private String password;
    private int id;

    public PersonDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonDTO(String username, String password, int id) {
        this.id = id;
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

    public static class PersonDTOBuilder {
        private String username;
        private String password;
        private int id;

        public PersonDTOBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public PersonDTOBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public PersonDTOBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public PersonDTO build() {
            return new PersonDTO(username, password, id);
        }

    }
}
