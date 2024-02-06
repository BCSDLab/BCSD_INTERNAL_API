package com.bcsdlab.internal.payment;

import java.time.LocalDateTime;

import com.bcsdlab.internal.global.RootEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Payment extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "account_holder")
    private String accountHolder;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "memo")
    private String memo;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "remain_money")
    private Long remainMoney;

    @Column(name = "is_delete")
    private boolean isDelete;
}
