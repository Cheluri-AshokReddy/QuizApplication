package com.quizserver.quizserver.Service;

import com.quizserver.quizserver.Model.User;

public interface UserService {
    User createUser(User user);
    Boolean hasUserWithEmail(String email);
    User login(User user);

}
