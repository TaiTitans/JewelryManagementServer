package com.jewelrymanagement.repository;

import com.jewelrymanagement.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GetUsernameRepository extends JpaRepository<User, String>  {
    @Query("SELECT u FROM User u WHERE u.Username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
