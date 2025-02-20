package com.example.database.user.controller;

import com.example.database.user.dto.response.UserResponse;
import com.example.database.user.entity.UserEntity;
import com.example.database.user.repository.UserRepository;
import com.example.database.user.routes.UserRoutes;
import com.example.database.user.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping(UserRoutes.INIT)
    public boolean init() {
        return userService.init();
    }

    @GetMapping(UserRoutes.ROOT)
    public List<UserResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        ExampleMatcher ignoring = ExampleMatcher.matchingAny()
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<UserEntity> example = Example.of(
                UserEntity.builder().firstName(query).lastName(query).build(), ignoring
        );

        return userRepository.findAll(example, pageable)
                .stream()
                .map(UserResponse::of)
                .collect(Collectors.toList());
    }
}
