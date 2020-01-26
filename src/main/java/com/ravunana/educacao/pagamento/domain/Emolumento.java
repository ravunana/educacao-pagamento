package com.ravunana.educacao.pagamento.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Emolumento.
 */
@Entity
@Table(name = "emolumento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "emolumento")
public class Emolumento implements Serializable {

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
    @Column(name = "montante", precision = 21, scale = 2, nullable = false)
    private BigDecimal montante;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montante_multa", nullable = false)
    private Double montanteMulta;

    @NotNull
    @Min(value = 0)
    @Column(name = "tempo_multa", nullable = false)
    private Integer tempoMulta;

    @DecimalMin(value = "0")
    @Column(name = "quantidade")
    private Double quantidade;

    @Column(name = "turno")
    private String turno;

    @Column(name = "classe")
    private Integer classe;

    @Column(name = "curso")
    private String curso;

    @OneToMany(mappedBy = "emolumento")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EfeitoPagamento> efeitoPagamentos = new HashSet<>();

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

    public Emolumento nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getMontante() {
        return montante;
    }

    public Emolumento montante(BigDecimal montante) {
        this.montante = montante;
        return this;
    }

    public void setMontante(BigDecimal montante) {
        this.montante = montante;
    }

    public Double getMontanteMulta() {
        return montanteMulta;
    }

    public Emolumento montanteMulta(Double montanteMulta) {
        this.montanteMulta = montanteMulta;
        return this;
    }

    public void setMontanteMulta(Double montanteMulta) {
        this.montanteMulta = montanteMulta;
    }

    public Integer getTempoMulta() {
        return tempoMulta;
    }

    public Emolumento tempoMulta(Integer tempoMulta) {
        this.tempoMulta = tempoMulta;
        return this;
    }

    public void setTempoMulta(Integer tempoMulta) {
        this.tempoMulta = tempoMulta;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public Emolumento quantidade(Double quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public String getTurno() {
        return turno;
    }

    public Emolumento turno(String turno) {
        this.turno = turno;
        return this;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Integer getClasse() {
        return classe;
    }

    public Emolumento classe(Integer classe) {
        this.classe = classe;
        return this;
    }

    public void setClasse(Integer classe) {
        this.classe = classe;
    }

    public String getCurso() {
        return curso;
    }

    public Emolumento curso(String curso) {
        this.curso = curso;
        return this;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Set<EfeitoPagamento> getEfeitoPagamentos() {
        return efeitoPagamentos;
    }

    public Emolumento efeitoPagamentos(Set<EfeitoPagamento> efeitoPagamentos) {
        this.efeitoPagamentos = efeitoPagamentos;
        return this;
    }

    public Emolumento addEfeitoPagamento(EfeitoPagamento efeitoPagamento) {
        this.efeitoPagamentos.add(efeitoPagamento);
        efeitoPagamento.setEmolumento(this);
        return this;
    }

    public Emolumento removeEfeitoPagamento(EfeitoPagamento efeitoPagamento) {
        this.efeitoPagamentos.remove(efeitoPagamento);
        efeitoPagamento.setEmolumento(null);
        return this;
    }

    public void setEfeitoPagamentos(Set<EfeitoPagamento> efeitoPagamentos) {
        this.efeitoPagamentos = efeitoPagamentos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Emolumento)) {
            return false;
        }
        return id != null && id.equals(((Emolumento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Emolumento{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", montante=" + getMontante() +
            ", montanteMulta=" + getMontanteMulta() +
            ", tempoMulta=" + getTempoMulta() +
            ", quantidade=" + getQuantidade() +
            ", turno='" + getTurno() + "'" +
            ", classe=" + getClasse() +
            ", curso='" + getCurso() + "'" +
            "}";
    }
}
