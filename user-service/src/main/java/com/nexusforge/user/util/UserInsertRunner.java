package com.nexusforge.user.util;

import com.nexusforge.user.entity.User;
import com.nexusforge.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInsertRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserInsertRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        var user1 = new User();
        user1.setId(1);
        user1.setName("Sam");
        user1.setBalance(1000);

        var user2 = new User();
        user2.setId(2);
        user2.setName("Mike");
        user2.setBalance(1000);

        var user3 = new User();
        user3.setId(3);
        user3.setName("John");
        user3.setBalance(1000);
        userRepository.saveAll(List.of(user1,user2,user3));
    }
}
