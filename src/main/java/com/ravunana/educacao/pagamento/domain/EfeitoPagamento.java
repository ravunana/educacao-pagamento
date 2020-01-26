package com.ravunana.educacao.pagamento.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A EfeitoPagamento.
 */
@Entity
@Table(name = "efeito_pagamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "efeitopagamento")
public class EfeitoPagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montante", precision = 21, scale = 2, nullable = false)
    private BigDecimal montante;

    @DecimalMin(value = "0")
    @Column(name = "desconto")
    private Double desconto;

    @DecimalMin(value = "0")
    @Column(name = "multa")
    private Double multa;

    @DecimalMin(value = "0")
    @Column(name = "juro")
    private Double juro;

    @Column(name = "data")
    private ZonedDateTime data;

    @Column(name = "vencimento")
    private LocalDate vencimento;

    @NotNull
    @Column(name = "quitado", nullable = false)
    private Boolean quitado;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "efeito_pagamento_deposito",
               joinColumns = @JoinColumn(name = "efeito_pagamento_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "deposito_id", referencedColumnName = "id"))
    private Set<Deposito> depositos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("efeitoPagamentos")
    private Emolumento emolumento;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("efeitoPagamentos")
    private PagamentoEmolumento pagamento;

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

    public EfeitoPagamento descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public EfeitoPagamento quantidade(Integer quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getMontante() {
        return montante;
    }

    public EfeitoPagamento montante(BigDecimal montante) {
        this.montante = montante;
        return this;
    }

    public void setMontante(BigDecimal montante) {
        this.montante = montante;
    }

    public Double getDesconto() {
        return desconto;
    }

    public EfeitoPagamento desconto(Double desconto) {
        this.desconto = desconto;
        return this;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getMulta() {
        return multa;
    }

    public EfeitoPagamento multa(Double multa) {
        this.multa = multa;
        return this;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

    public Double getJuro() {
        return juro;
    }

    public EfeitoPagamento juro(Double juro) {
        this.juro = juro;
        return this;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public EfeitoPagamento data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public EfeitoPagamento vencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
        return this;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public Boolean isQuitado() {
        return quitado;
    }

    public EfeitoPagamento quitado(Boolean quitado) {
        this.quitado = quitado;
        return this;
    }

    public void setQuitado(Boolean quitado) {
        this.quitado = quitado;
    }

    public Set<Deposito> getDepositos() {
        return depositos;
    }

    public EfeitoPagamento depositos(Set<Deposito> depositos) {
        this.depositos = depositos;
        return this;
    }

    public EfeitoPagamento addDeposito(Deposito deposito) {
        this.depositos.add(deposito);
        deposito.getEfeitos().add(this);
        return this;
    }

    public EfeitoPagamento removeDeposito(Deposito deposito) {
        this.depositos.remove(deposito);
        deposito.getEfeitos().remove(this);
        return this;
    }

    public void setDepositos(Set<Deposito> depositos) {
        this.depositos = depositos;
    }

    public Emolumento getEmolumento() {
        return emolumento;
    }

    public EfeitoPagamento emolumento(Emolumento emolumento) {
        this.emolumento = emolumento;
        return this;
    }

    public void setEmolumento(Emolumento emolumento) {
        this.emolumento = emolumento;
    }

    public PagamentoEmolumento getPagamento() {
        return pagamento;
    }

    public EfeitoPagamento pagamento(PagamentoEmolumento pagamentoEmolumento) {
        this.pagamento = pagamentoEmolumento;
        return this;
    }

    public void setPagamento(PagamentoEmolumento pagamentoEmolumento) {
        this.pagamento = pagamentoEmolumento;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EfeitoPagamento)) {
            return false;
        }
        return id != null && id.equals(((EfeitoPagamento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EfeitoPagamento{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", quantidade=" + getQuantidade() +
            ", montante=" + getMontante() +
            ", desconto=" + getDesconto() +
            ", multa=" + getMulta() +
            ", juro=" + getJuro() +
            ", data='" + getData() + "'" +
            ", vencimento='" + getVencimento() + "'" +
            ", quitado='" + isQuitado() + "'" +
            "}";
    }
}
