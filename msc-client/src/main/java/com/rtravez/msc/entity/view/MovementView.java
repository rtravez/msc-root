package com.rtravez.msc.entity.view;

import com.rtravez.msc.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "movements")
@Table(name = "movements", indexes = {
    @jakarta.persistence.Index(name = "idx_movements_account_date", columnList = "account_id, movement_date")
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementView extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id", unique = true, nullable = false)
    private Long movementId;

    @Column(name = "movement_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime movementDate;

    @Column(name = "movement_type", nullable = false, length = 1)
    private Character movementType;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "available_balance", nullable = false)
    private BigDecimal availableBalance;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private AccountView account;
}