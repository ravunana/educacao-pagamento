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
 * A FormaLiquidacao.
 */
@Entity
@Table(name = "forma_liquidacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "formaliquidacao")
public class FormaLiquidacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "juro", nullable = false)
    private Double juro;

    @NotNull
    @Min(value = 0)
    @Column(name = "prazo_liquidacao", nullable = false)
    private Integer prazoLiquidacao;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "icon")
    private String icon;

    @OneToMany(mappedBy = "formaLiquidacao")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PagamentoEmolumento> pagamentoEmolumentos = new HashSet<>();

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

    public FormaLiquidacao nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getJuro() {
        return juro;
    }

    public FormaLiquidacao juro(Double juro) {
        this.juro = juro;
        return this;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public Integer getPrazoLiquidacao() {
        return prazoLiquidacao;
    }

    public FormaLiquidacao prazoLiquidacao(Integer prazoLiquidacao) {
        this.prazoLiquidacao = prazoLiquidacao;
        return this;
    }

    public void setPrazoLiquidacao(Integer prazoLiquidacao) {
        this.prazoLiquidacao = prazoLiquidacao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public FormaLiquidacao quantidade(Integer quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getIcon() {
        return icon;
    }

    public FormaLiquidacao icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<PagamentoEmolumento> getPagamentoEmolumentos() {
        return pagamentoEmolumentos;
    }

    public FormaLiquidacao pagamentoEmolumentos(Set<PagamentoEmolumento> pagamentoEmolumentos) {
        this.pagamentoEmolumentos = pagamentoEmolumentos;
        return this;
    }

    public FormaLiquidacao addPagamentoEmolumento(PagamentoEmolumento pagamentoEmolumento) {
        this.pagamentoEmolumentos.add(pagamentoEmolumento);
        pagamentoEmolumento.setFormaLiquidacao(this);
        return this;
    }

    public FormaLiquidacao removePagamentoEmolumento(PagamentoEmolumento pagamentoEmolumento) {
        this.pagamentoEmolumentos.remove(pagamentoEmolumento);
        pagamentoEmolumento.setFormaLiquidacao(null);
        return this;
    }

    public void setPagamentoEmolumentos(Set<PagamentoEmolumento> pagamentoEmolumentos) {
        this.pagamentoEmolumentos = pagamentoEmolumentos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormaLiquidacao)) {
            return false;
        }
        return id != null && id.equals(((FormaLiquidacao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FormaLiquidacao{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", juro=" + getJuro() +
            ", prazoLiquidacao=" + getPrazoLiquidacao() +
            ", quantidade=" + getQuantidade() +
            ", icon='" + getIcon() + "'" +
            "}";
    }
}
