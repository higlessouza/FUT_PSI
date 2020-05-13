package com.puc.psi.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Plataforma.
 */
@Entity
@Table(name = "plataforma")
public class Plataforma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "logo")
    private String logo;

    @Column(name = "marca")
    private String marca;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Plataforma nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogo() {
        return logo;
    }

    public Plataforma logo(String logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMarca() {
        return marca;
    }

    public Plataforma marca(String marca) {
        this.marca = marca;
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plataforma)) {
            return false;
        }
        return id != null && id.equals(((Plataforma) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Plataforma{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", logo='" + getLogo() + "'" +
            ", marca='" + getMarca() + "'" +
            "}";
    }
}
