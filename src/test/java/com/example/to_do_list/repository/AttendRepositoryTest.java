package com.example.to_do_list.repository;

import com.example.to_do_list.common.JPAConfig;
import com.example.to_do_list.common.queryDSL.QueryDslConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({JPAConfig.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("prod1")
public class AttendRepositoryTest {

    @Autowired
    private AttendRepository attendRepository;

    @Autowired
    private UsersRepository usersRepository;
}
