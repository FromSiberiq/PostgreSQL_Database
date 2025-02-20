package com.example.database.user.service;

import com.example.database.user.entity.UserEntity;
import com.example.database.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final NamesGenerator namesGenerator = new NamesGenerator();

    public boolean init() {
        userRepository.deleteAll();

        int repeat = 10, dataPerLoop = 10000;

        for (int i = 0; i < repeat; i++) {

            List<UserEntity> all = new ArrayList<>();
            for (int dataIndex = 0; dataIndex < dataPerLoop; dataIndex++) {
                NamesGenerator.Name name = namesGenerator.generate();
                UserEntity user = UserEntity.builder()
                        .lastName(name.getLastName())
                        .firstName((name.getFirstName()))
                        .build();
                all.add(user);
            }
            userRepository.saveAll(all);
        }
        return true;
    }
}
