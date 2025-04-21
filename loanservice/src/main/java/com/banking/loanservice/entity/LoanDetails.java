package com.banking.loanservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LoanDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int loanDetailsId;
	
	@NotNull(message = "interest is must")
	private double interestRate;
	
	@NotNull(message = "tenture is must")
	private int tenure;
	
	@NotNull(message = "emiAmount is must")
	private double emiAmount;
	
	@OneToOne
	@JoinColumn(name = "loan_application_id",nullable = false,unique = true)
	@JsonBackReference
	private LoanApplication loanApplication;
}
