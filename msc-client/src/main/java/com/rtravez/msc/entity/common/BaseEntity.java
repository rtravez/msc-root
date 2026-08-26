package com.rtravez.msc.entity.common;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "creation_host", updatable = false, length = 50, nullable = false)
    protected String creationHost;

    @Column(name = "modification_host", updatable = true, length = 50)
    protected String modificationHost;

    @Column(name = "creation_user", updatable = false, length = 50, nullable = false)
    @CreatedBy
    protected String creationUser;

    @Column(name = "modification_user", updatable = true, length = 50)
    @LastModifiedBy
    protected String modificationUser;

    @Column(name = "creation_date", updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected Date creationDate;

    @Column(name = "modification_date", updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Date modificationDate;

    @Column(name = "status", nullable = false)
    protected Boolean status;

}
