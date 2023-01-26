package com.example.to_do_list.repository;


import com.example.to_do_list.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsersId(Long userId);
    Optional<Users> findByEmail(String email);

//    @Query("select u.refreshToken from Users u where u.usersId= :id")
//    String getRefreshTokenById(@Param("id") Long id);
//
//    @Transactional
//    @Modifying
//    @Query("update Users u set u.refreshToken= :token where u.usersId =:id")
//    void updateRefreshToken(@Param("id") Long id, @Param("token") String token);
}
