package com.ravunana.educacao.pagamento.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Deposito.
 */
@Entity
@Table(name = "deposito")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "deposito")
public class Deposito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false, unique = true)
    private String numero;

    @NotNull
    @Column(name = "data_deposito", nullable = false)
    private LocalDate dataDeposito;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montante", precision = 21, scale = 2, nullable = false)
    private BigDecimal montante;

    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @Lob
    @Column(name = "anexo")
    private byte[] anexo;

    @Column(name = "anexo_content_type")
    private String anexoContentType;

    @NotNull
    @Column(name = "numero_processo", nullable = false)
    private String numeroProcesso;

    @NotNull
    @Column(name = "meio_liquidacao", nullable = false)
    private String meioLiquidacao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("depositos")
    private Caixa bancoCaixa;

    @ManyToMany(mappedBy = "depositos")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<EfeitoPagamento> efeitos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public Deposito numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDataDeposito() {
        return dataDeposito;
    }

    public Deposito dataDeposito(LocalDate dataDeposito) {
        this.dataDeposito = dataDeposito;
        return this;
    }

    public void setDataDeposito(LocalDate dataDeposito) {
        this.dataDeposito = dataDeposito;
    }

    public BigDecimal getMontante() {
        return montante;
    }

    public Deposito montante(BigDecimal montante) {
        this.montante = montante;
        return this;
    }

    public void setMontante(BigDecimal montante) {
        this.montante = montante;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public Deposito data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public byte[] getAnexo() {
        return anexo;
    }

    public Deposito anexo(byte[] anexo) {
        this.anexo = anexo;
        return this;
    }

    public void setAnexo(byte[] anexo) {
        this.anexo = anexo;
    }

    public String getAnexoContentType() {
        return anexoContentType;
    }

    public Deposito anexoContentType(String anexoContentType) {
        this.anexoContentType = anexoContentType;
        return this;
    }

    public void setAnexoContentType(String anexoContentType) {
        this.anexoContentType = anexoContentType;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public Deposito numeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
        return this;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getMeioLiquidacao() {
        return meioLiquidacao;
    }

    public Deposito meioLiquidacao(String meioLiquidacao) {
        this.meioLiquidacao = meioLiquidacao;
        return this;
    }

    public void setMeioLiquidacao(String meioLiquidacao) {
        this.meioLiquidacao = meioLiquidacao;
    }

    public Caixa getBancoCaixa() {
        return bancoCaixa;
    }

    public Deposito bancoCaixa(Caixa caixa) {
        this.bancoCaixa = caixa;
        return this;
    }

    public void setBancoCaixa(Caixa caixa) {
        this.bancoCaixa = caixa;
    }

    public Set<EfeitoPagamento> getEfeitos() {
        return efeitos;
    }

    public Deposito efeitos(Set<EfeitoPagamento> efeitoPagamentos) {
        this.efeitos = efeitoPagamentos;
        return this;
    }

    public Deposito addEfeito(EfeitoPagamento efeitoPagamento) {
        this.efeitos.add(efeitoPagamento);
        efeitoPagamento.getDepositos().add(this);
        return this;
    }

    public Deposito removeEfeito(EfeitoPagamento efeitoPagamento) {
        this.efeitos.remove(efeitoPagamento);
        efeitoPagamento.getDepositos().remove(this);
        return this;
    }

    public void setEfeitos(Set<EfeitoPagamento> efeitoPagamentos) {
        this.efeitos = efeitoPagamentos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deposito)) {
            return false;
        }
        return id != null && id.equals(((Deposito) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Deposito{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dataDeposito='" + getDataDeposito() + "'" +
            ", montante=" + getMontante() +
            ", data='" + getData() + "'" +
            ", anexo='" + getAnexo() + "'" +
            ", anexoContentType='" + getAnexoContentType() + "'" +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            ", meioLiquidacao='" + getMeioLiquidacao() + "'" +
            "}";
    }
}
