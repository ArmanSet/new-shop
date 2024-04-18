package com.example.demo.repository;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    Users findByName(String username);
   Users findUserByName(String username);
   List<Users> findUserByRole(String role);
    List<Users> findByRole(String role);

    Users findUsersByEmail(String email);
//   List<Users> findUsersWhereRole(String role);

}
