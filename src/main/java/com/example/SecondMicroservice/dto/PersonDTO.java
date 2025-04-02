package com.example.SecondMicroservice.dto;

public class PersonDTO {

    private String username;
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
        private String username;
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

}
