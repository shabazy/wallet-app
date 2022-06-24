package com.tech.api.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @CreationTimestamp
    @CreatedDate
    @Column(
            name = "created_at"
    )
    private Date createDate;

    @UpdateTimestamp
    @LastModifiedDate
    @Column(
            name = "updated_at"
    )
    private Date updateDate;

    public BaseEntity() {}

}
