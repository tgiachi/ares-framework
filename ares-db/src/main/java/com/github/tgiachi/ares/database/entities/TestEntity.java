package com.github.tgiachi.ares.database.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity di prova per ORM
 */
@Entity
@Table(name ="TEST_ENTITY")
@Data
public class TestEntity implements Serializable {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String field1;

    private int field2;

    private long field3;


}
