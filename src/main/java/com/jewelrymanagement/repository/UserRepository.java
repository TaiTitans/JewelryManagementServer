package com.jewelrymanagement.repository;

import com.jewelrymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Integer> {
}
