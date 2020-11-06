package com.giants.domain;

import lombok.Getter;
import lombok.Setter;
//import javax.persistence.Entity;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Table;

//@Entity
//@Table(name = "STATE")
@Getter
@Setter
public class State {
//    @PersistenceContext
//    EntityManager entityManager;

    // How are we storing the relationships between states/districts/precincts now?
    private int id;
    private int population;
    private float compactness;
}
