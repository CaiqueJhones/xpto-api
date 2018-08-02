package com.xptosystems.api.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    @Column(name = "optlock", columnDefinition = "BIGINT DEFAULT 0", nullable = false)  
    @Version  
    private long version = 0L;
}
