package com.banking.loanservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.loanservice.entity.LoanDetails;

@Repository
public interface LoanDetailsRepository extends JpaRepository<LoanDetails, Integer>{

}
