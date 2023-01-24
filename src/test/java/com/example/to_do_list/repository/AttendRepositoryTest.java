package com.example.to_do_list.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AttendRepositoryTest {

    @Autowired
    private AttendRepository attendRepository;

    @Autowired
    private UsersRepository usersRepository;
}
