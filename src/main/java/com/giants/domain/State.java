package com.giants.domain;

//import javax.persistence.Entity;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Table;

//@Entity
//@Table(name = "STATE")
public class State {
//    @PersistenceContext
//    EntityManager entityManager;

    // How are we storing the relationships between states/districts/precincts now?
    private int id;
    private int population;
    private float compactness;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public float getCompactness() {
        return compactness;
    }

    public void setCompactness(float compactness) {
        this.compactness = compactness;
    }
}
