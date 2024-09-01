package com.todoforge.account.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "email_address")
    String emailAddress;

    @Column(name = "account_id", nullable = false,
            insertable = false, updatable = false) // read_only
    Long accountId;

    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "fk_account_profile"))
    AccountEntity account;
}
