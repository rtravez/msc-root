package com.rtravez.msc.entity.view;

import java.math.BigDecimal;
import java.util.List;

import com.rtravez.msc.entity.PersonEntity;
import com.rtravez.msc.entity.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "accounts")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountView extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", unique = true, nullable = false)
    private Long accountId;

    @Column(name = "account_number", nullable = false, length = 11)
    private Long accountNumber;

    @Column(name = "account_type", nullable = false, length = 11)
    private String accountType;

    @Column(name = "initial_balance", nullable = false)
    private BigDecimal initialBalance;

    @Column(name = "person_id", nullable = false)
    private Long personId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", insertable = false, updatable = false)
    private PersonEntity person;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<MovementView> movements;

}