package com.ryan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ryan.domain.User;

public interface UserRepository extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {

	User findByUsernameAndType(String username,Integer type);

	User findByEmailAndType(String email,Integer type);
}
