package com.example.user_service.entity;

import com.example.user_service.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Entity
//@Table(name = "addresses")
//public class Address extends BaseEntity {
//    @Column
//    private String address;
//    @Column
//    private String city;
//    @Column
//    private String country;
//    @Column
//    private String zip;
//    @Column(name = "phone_number")
//    private String phoneNumber;
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//}
