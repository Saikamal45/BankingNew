package com.banking.transactionService.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class TokenInterceptor implements RequestInterceptor {

	@Autowired
    private TokenHolder tokenHolder;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + tokenHolder.getToken());
    }
}
