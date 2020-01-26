package com.ravunana.educacao.pagamento.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ContaAluno.
 */
@Entity
@Table(name = "conta_aluno")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contaaluno")
public class ContaAluno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "debito", precision = 21, scale = 2, nullable = false)
    private BigDecimal debito;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "credito", precision = 21, scale = 2, nullable = false)
    private BigDecimal credito;

    @NotNull
    @Column(name = "numero_processo", nullable = false)
    private String numeroProcesso;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDebito() {
        return debito;
    }

    public ContaAluno debito(BigDecimal debito) {
        this.debito = debito;
        return this;
    }

    public void setDebito(BigDecimal debito) {
        this.debito = debito;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public ContaAluno credito(BigDecimal credito) {
        this.credito = credito;
        return this;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public ContaAluno numeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
        return this;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaAluno)) {
            return false;
        }
        return id != null && id.equals(((ContaAluno) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ContaAluno{" +
            "id=" + getId() +
            ", debito=" + getDebito() +
            ", credito=" + getCredito() +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            "}";
    }
}
