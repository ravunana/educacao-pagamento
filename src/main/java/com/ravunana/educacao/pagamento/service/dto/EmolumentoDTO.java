package com.ravunana.educacao.pagamento.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pagamento.domain.Emolumento} entity.
 */
public class EmolumentoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal montante;

    @NotNull
    @DecimalMin(value = "0")
    private Double montanteMulta;

    @NotNull
    @Min(value = 0)
    private Integer tempoMulta;

    @DecimalMin(value = "0")
    private Double quantidade;

    private String turno;

    private Integer classe;

    private String curso;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getMontante() {
        return montante;
    }

    public void setMontante(BigDecimal montante) {
        this.montante = montante;
    }

    public Double getMontanteMulta() {
        return montanteMulta;
    }

    public void setMontanteMulta(Double montanteMulta) {
        this.montanteMulta = montanteMulta;
    }

    public Integer getTempoMulta() {
        return tempoMulta;
    }

    public void setTempoMulta(Integer tempoMulta) {
        this.tempoMulta = tempoMulta;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Integer getClasse() {
        return classe;
    }

    public void setClasse(Integer classe) {
        this.classe = classe;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmolumentoDTO emolumentoDTO = (EmolumentoDTO) o;
        if (emolumentoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emolumentoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmolumentoDTO{" +
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
