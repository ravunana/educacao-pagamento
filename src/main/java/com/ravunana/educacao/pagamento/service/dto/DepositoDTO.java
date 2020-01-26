package com.ravunana.educacao.pagamento.service.dto;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.ravunana.educacao.pagamento.domain.Deposito} entity.
 */
public class DepositoDTO implements Serializable {

    private Long id;

    @NotNull
    private String numero;

    @NotNull
    private LocalDate dataDeposito;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal montante;

    @NotNull
    private ZonedDateTime data;

    @Lob
    private byte[] anexo;

    private String anexoContentType;
    @NotNull
    private String numeroProcesso;

    @NotNull
    private String meioLiquidacao;


    private Long bancoCaixaId;

    private String bancoCaixaDescricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDataDeposito() {
        return dataDeposito;
    }

    public void setDataDeposito(LocalDate dataDeposito) {
        this.dataDeposito = dataDeposito;
    }

    public BigDecimal getMontante() {
        return montante;
    }

    public void setMontante(BigDecimal montante) {
        this.montante = montante;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public byte[] getAnexo() {
        return anexo;
    }

    public void setAnexo(byte[] anexo) {
        this.anexo = anexo;
    }

    public String getAnexoContentType() {
        return anexoContentType;
    }

    public void setAnexoContentType(String anexoContentType) {
        this.anexoContentType = anexoContentType;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getMeioLiquidacao() {
        return meioLiquidacao;
    }

    public void setMeioLiquidacao(String meioLiquidacao) {
        this.meioLiquidacao = meioLiquidacao;
    }

    public Long getBancoCaixaId() {
        return bancoCaixaId;
    }

    public void setBancoCaixaId(Long caixaId) {
        this.bancoCaixaId = caixaId;
    }

    public String getBancoCaixaDescricao() {
        return bancoCaixaDescricao;
    }

    public void setBancoCaixaDescricao(String caixaDescricao) {
        this.bancoCaixaDescricao = caixaDescricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DepositoDTO depositoDTO = (DepositoDTO) o;
        if (depositoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), depositoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DepositoDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dataDeposito='" + getDataDeposito() + "'" +
            ", montante=" + getMontante() +
            ", data='" + getData() + "'" +
            ", anexo='" + getAnexo() + "'" +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            ", meioLiquidacao='" + getMeioLiquidacao() + "'" +
            ", bancoCaixaId=" + getBancoCaixaId() +
            ", bancoCaixaDescricao='" + getBancoCaixaDescricao() + "'" +
            "}";
    }
}
