package com.github.tobsinger.repository;


import com.github.tobsinger.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface CustomerRepository extends JpaRepository<Customer, Long>, QueryDslPredicateExecutor<Customer> {

//    List<Customer> findByFirstName(String lastName);
}