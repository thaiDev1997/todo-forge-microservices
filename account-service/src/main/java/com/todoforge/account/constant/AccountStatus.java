package com.todoforge.account.constant;

public enum AccountStatus {
    PENDING, // not verified yet
    ACTIVE,
    IN_ACTIVE, // due to a period of inactivity or administrative action
    SUSPENDED; // policy violation
}
