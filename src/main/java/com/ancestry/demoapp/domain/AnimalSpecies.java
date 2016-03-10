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
 * A AnimalSpecies.
 */
@Entity
@Table(name = "animal_species")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AnimalSpecies implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "animal_species_name", nullable = false)
    private String animalSpeciesName;
    
    @OneToMany(mappedBy = "animalSpecies")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Animal> animals = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnimalSpeciesName() {
        return animalSpeciesName;
    }
    
    public void setAnimalSpeciesName(String animalSpeciesName) {
        this.animalSpeciesName = animalSpeciesName;
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
        AnimalSpecies animalSpecies = (AnimalSpecies) o;
        if(animalSpecies.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, animalSpecies.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AnimalSpecies{" +
            "id=" + id +
            ", animalSpeciesName='" + animalSpeciesName + "'" +
            '}';
    }
}
