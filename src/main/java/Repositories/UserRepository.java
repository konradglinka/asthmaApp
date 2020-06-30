package Repositories;

import Objects.User;

public class UserRepository {
    private User user;

    public UserRepository(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
