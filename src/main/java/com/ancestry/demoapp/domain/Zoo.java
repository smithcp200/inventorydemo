package com.ancestry.demoapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Zoo.
 */
@Entity
@Table(name = "zoo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Zoo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "zoo_name", nullable = false)
    private String zooName;
    
    @OneToMany(mappedBy = "zoo")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Inventory> inventorys = new HashSet<>();

    @OneToMany(mappedBy = "zoo")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Animal> animals = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZooName() {
        return zooName;
    }
    
    public void setZooName(String zooName) {
        this.zooName = zooName;
    }

    public Set<Inventory> getInventorys() {
        return inventorys;
    }

    public void setInventorys(Set<Inventory> inventorys) {
        this.inventorys = inventorys;
    }

    public Set<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Zoo zoo = (Zoo) o;
        if(zoo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, zoo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Zoo{" +
            "id=" + id +
            ", zooName='" + zooName + "'" +
            '}';
    }
}
