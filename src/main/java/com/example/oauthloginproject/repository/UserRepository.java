package com.example.oauthloginproject.repository;

import com.example.oauthloginproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);


//    @Query(value =
//            "select count(*) " +
//                    "from members " +
//                    "where email = :email ", nativeQuery = true)
//    Integer countByEmail(@Param("email") String email);
}
