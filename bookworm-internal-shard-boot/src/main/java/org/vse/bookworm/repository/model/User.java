package org.vse.bookworm.repository.model;

public class User {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String userName;
    private final int partition;

    public User(Builder b) {
        id = b.id;
        firstName = b.firstName;
        lastName = b.lastName;
        userName = b.userName;
        partition = b.partition;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public int getPartition() {
        return partition;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String firstName;
        private String lastName;
        private String userName;
        private int partition;

        public User build() {
            return new User(this);
        }

        public Builder setId(long id) {
            this.id = id;
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

        public Builder setPartition(int partition) {
            this.partition = partition;
            return this;
        }
    }
}
