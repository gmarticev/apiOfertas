package com.mycompany.apiofertas.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Oferta.
 */
@Entity
@Table(name = "oferta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "oferta")
public class Oferta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 50)
    @Column(name = "titulo", length = 50)
    private String titulo;

    @Size(min = 1, max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Size(min = 1, max = 50)
    @Column(name = "empresa", length = 50)
    private String empresa;

    @Min(value = 1L)
    @Column(name = "salario")
    private Long salario;

    @Size(min = 1, max = 50)
    @Column(name = "ciudad", length = 50)
    private String ciudad;

    @Size(min = 1, max = 50)
    @Column(name = "email", length = 50)
    private String email;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Oferta titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Oferta descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEmpresa() {
        return empresa;
    }

    public Oferta empresa(String empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Long getSalario() {
        return salario;
    }

    public Oferta salario(Long salario) {
        this.salario = salario;
        return this;
    }

    public void setSalario(Long salario) {
        this.salario = salario;
    }

    public String getCiudad() {
        return ciudad;
    }

    public Oferta ciudad(String ciudad) {
        this.ciudad = ciudad;
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEmail() {
        return email;
    }

    public Oferta email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Oferta)) {
            return false;
        }
        return id != null && id.equals(((Oferta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Oferta{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", empresa='" + getEmpresa() + "'" +
            ", salario=" + getSalario() +
            ", ciudad='" + getCiudad() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
