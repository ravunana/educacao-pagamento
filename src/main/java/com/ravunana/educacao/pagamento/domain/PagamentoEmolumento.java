package com.ravunana.educacao.pagamento.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A PagamentoEmolumento.
 */
@Entity
@Table(name = "pagamento_emolumento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pagamentoemolumento")
public class PagamentoEmolumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "data")
    private ZonedDateTime data;

    @NotNull
    @Column(name = "numero", nullable = false, unique = true)
    private String numero;

    @NotNull
    @Column(name = "numero_processo", nullable = false)
    private String numeroProcesso;

    @NotNull
    @Column(name = "nome_aluno", nullable = false)
    private String nomeAluno;

    @NotNull
    @Column(name = "turma_aluno", nullable = false)
    private String turmaAluno;

    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "pagamento")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EfeitoPagamento> efeitoPagamentos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("pagamentoEmolumentos")
    private FormaLiquidacao formaLiquidacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public PagamentoEmolumento data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public String getNumero() {
        return numero;
    }

    public PagamentoEmolumento numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public PagamentoEmolumento numeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
        return this;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public PagamentoEmolumento nomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
        return this;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getTurmaAluno() {
        return turmaAluno;
    }

    public PagamentoEmolumento turmaAluno(String turmaAluno) {
        this.turmaAluno = turmaAluno;
        return this;
    }

    public void setTurmaAluno(String turmaAluno) {
        this.turmaAluno = turmaAluno;
    }

    public String getEstado() {
        return estado;
    }

    public PagamentoEmolumento estado(String estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Set<EfeitoPagamento> getEfeitoPagamentos() {
        return efeitoPagamentos;
    }

    public PagamentoEmolumento efeitoPagamentos(Set<EfeitoPagamento> efeitoPagamentos) {
        this.efeitoPagamentos = efeitoPagamentos;
        return this;
    }

    public PagamentoEmolumento addEfeitoPagamento(EfeitoPagamento efeitoPagamento) {
        this.efeitoPagamentos.add(efeitoPagamento);
        efeitoPagamento.setPagamento(this);
        return this;
    }

    public PagamentoEmolumento removeEfeitoPagamento(EfeitoPagamento efeitoPagamento) {
        this.efeitoPagamentos.remove(efeitoPagamento);
        efeitoPagamento.setPagamento(null);
        return this;
    }

    public void setEfeitoPagamentos(Set<EfeitoPagamento> efeitoPagamentos) {
        this.efeitoPagamentos = efeitoPagamentos;
    }

    public FormaLiquidacao getFormaLiquidacao() {
        return formaLiquidacao;
    }

    public PagamentoEmolumento formaLiquidacao(FormaLiquidacao formaLiquidacao) {
        this.formaLiquidacao = formaLiquidacao;
        return this;
    }

    public void setFormaLiquidacao(FormaLiquidacao formaLiquidacao) {
        this.formaLiquidacao = formaLiquidacao;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagamentoEmolumento)) {
            return false;
        }
        return id != null && id.equals(((PagamentoEmolumento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PagamentoEmolumento{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", numero='" + getNumero() + "'" +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            ", nomeAluno='" + getNomeAluno() + "'" +
            ", turmaAluno='" + getTurmaAluno() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
