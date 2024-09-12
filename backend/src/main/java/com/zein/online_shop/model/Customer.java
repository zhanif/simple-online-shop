package com.zein.online_shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String address;

    @Column(unique = true)
    @NotBlank
    private String code;

    @Column(unique = true)
    @NotBlank
    private String phone;

    @Column
    private Boolean isActive;

    @Column(columnDefinition = "DATE")
    private Date lastOrderDate;

    @Column
    private String pic;

    @Column(name = "created_time")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "last_modified_time")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedTime;
}
