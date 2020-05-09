package com.puc.psi.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A Partida.
 */
@Entity
@Table(name = "partida")
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gols_visitante")
    private Integer golsVisitante;

    @Column(name = "gols_mandante")
    private Integer golsMandante;

    @Column(name = "local")
    private String local;

    @Column(name = "data")
    private LocalDate data;

    @OneToOne
    @JoinColumn(unique = true)
    private Time mandante;

    @OneToOne
    @JoinColumn(unique = true)
    private Time visitante;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public Partida golsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
        return this;
    }

    public void setGolsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public Partida golsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
        return this;
    }

    public void setGolsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
    }

    public String getLocal() {
        return local;
    }

    public Partida local(String local) {
        this.local = local;
        return this;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDate getData() {
        return data;
    }

    public Partida data(LocalDate data) {
        this.data = data;
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Time getMandante() {
        return mandante;
    }

    public Partida mandante(Time time) {
        this.mandante = time;
        return this;
    }

    public void setMandante(Time time) {
        this.mandante = time;
    }

    public Time getVisitante() {
        return visitante;
    }

    public Partida visitante(Time time) {
        this.visitante = time;
        return this;
    }

    public void setVisitante(Time time) {
        this.visitante = time;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Partida)) {
            return false;
        }
        return id != null && id.equals(((Partida) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Partida{" +
            "id=" + getId() +
            ", golsVisitante=" + getGolsVisitante() +
            ", golsMandante=" + getGolsMandante() +
            ", local='" + getLocal() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
