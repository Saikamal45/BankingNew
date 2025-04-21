package com.banking.loanservice.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int loanApplicationId;
	
	@NotNull(message = "userId is Must")
	private int userId;
	
	@NotNull(message = "accountid is must")
	private int accountId;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "loantype is must")
    private LoanType loanType;
	
	@Positive(message = "amount must be positive")
	private double loanAmount;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "loanstatus is must")
	private LoanStatus loanStatus;
	private LocalDate loanIssueDate;
	
	@OneToOne(mappedBy = "loanApplication",cascade = CascadeType.ALL)
	@JsonManagedReference
	private LoanDetails loanDetails;
}
