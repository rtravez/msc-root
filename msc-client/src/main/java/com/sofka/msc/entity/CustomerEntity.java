package com.sofka.msc.entity;

import com.sofka.msc.entity.common.BaseEntity;
import com.sofka.msc.entity.common.RoleCustomerEntity;
import com.sofka.msc.entity.view.AccountView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "customers")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", unique = true, nullable = false)
    private Long customerId;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<RoleCustomerEntity> roleCustomers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity person;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<AccountView> accounts;

}