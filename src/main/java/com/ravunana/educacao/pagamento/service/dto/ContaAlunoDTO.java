package com.ravunana.educacao.pagamento.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pagamento.domain.ContaAluno} entity.
 */
public class ContaAlunoDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal debito;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal credito;

    @NotNull
    private String numeroProcesso;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDebito() {
        return debito;
    }

    public void setDebito(BigDecimal debito) {
        this.debito = debito;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContaAlunoDTO contaAlunoDTO = (ContaAlunoDTO) o;
        if (contaAlunoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contaAlunoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContaAlunoDTO{" +
            "id=" + getId() +
            ", debito=" + getDebito() +
            ", credito=" + getCredito() +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            "}";
    }
}
