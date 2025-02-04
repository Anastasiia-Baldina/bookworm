package org.vse.bookworm.dto.internal;

public class LoginRequest {
    private long userId;
    private String firstName;
    private String lastName;
    private String userName;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long userId;
        private String firstName;
        private String lastName;
        private String userName;

        public LoginRequest build() {
            LoginRequest rq = new LoginRequest();
            rq.setUserId(userId);
            rq.setFirstName(firstName);
            rq.setLastName(lastName);
            rq.setUserName(userName);

            return rq;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }
    }
}
