package com.ravunana.educacao.pagamento.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Caixa.
 */
@Entity
@Table(name = "caixa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "caixa")
public class Caixa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "descricao", nullable = false, unique = true)
    private String descricao;

    @NotNull
    @Column(name = "proprietario", nullable = false)
    private String proprietario;

    @NotNull
    @Column(name = "numero_conta", nullable = false, unique = true)
    private String numeroConta;

    
    @Column(name = "iban", unique = true)
    private String iban;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "bancoCaixa")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Deposito> depositos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Caixa descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getProprietario() {
        return proprietario;
    }

    public Caixa proprietario(String proprietario) {
        this.proprietario = proprietario;
        return this;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public Caixa numeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
        return this;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getIban() {
        return iban;
    }

    public Caixa iban(String iban) {
        this.iban = iban;
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public Caixa ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Set<Deposito> getDepositos() {
        return depositos;
    }

    public Caixa depositos(Set<Deposito> depositos) {
        this.depositos = depositos;
        return this;
    }

    public Caixa addDeposito(Deposito deposito) {
        this.depositos.add(deposito);
        deposito.setBancoCaixa(this);
        return this;
    }

    public Caixa removeDeposito(Deposito deposito) {
        this.depositos.remove(deposito);
        deposito.setBancoCaixa(null);
        return this;
    }

    public void setDepositos(Set<Deposito> depositos) {
        this.depositos = depositos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caixa)) {
            return false;
        }
        return id != null && id.equals(((Caixa) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Caixa{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", proprietario='" + getProprietario() + "'" +
            ", numeroConta='" + getNumeroConta() + "'" +
            ", iban='" + getIban() + "'" +
            ", ativo='" + isAtivo() + "'" +
            "}";
    }
}
