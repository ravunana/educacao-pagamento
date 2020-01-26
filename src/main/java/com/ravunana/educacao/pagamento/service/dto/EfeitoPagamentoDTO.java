package com.ravunana.educacao.pagamento.service.dto;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pagamento.domain.EfeitoPagamento} entity.
 */
public class EfeitoPagamentoDTO implements Serializable {

    private Long id;

    private String descricao;

    @NotNull
    @Min(value = 1)
    private Integer quantidade;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal montante;

    @DecimalMin(value = "0")
    private Double desconto;

    @DecimalMin(value = "0")
    private Double multa;

    @DecimalMin(value = "0")
    private Double juro;

    private ZonedDateTime data;

    private LocalDate vencimento;

    @NotNull
    private Boolean quitado;


    private Set<DepositoDTO> depositos = new HashSet<>();

    private Long emolumentoId;

    private String emolumentoNome;

    private Long pagamentoId;

    private String pagamentoNumero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getMontante() {
        return montante;
    }

    public void setMontante(BigDecimal montante) {
        this.montante = montante;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getMulta() {
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

    public Double getJuro() {
        return juro;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public Boolean isQuitado() {
        return quitado;
    }

    public void setQuitado(Boolean quitado) {
        this.quitado = quitado;
    }

    public Set<DepositoDTO> getDepositos() {
        return depositos;
    }

    public void setDepositos(Set<DepositoDTO> depositos) {
        this.depositos = depositos;
    }

    public Long getEmolumentoId() {
        return emolumentoId;
    }

    public void setEmolumentoId(Long emolumentoId) {
        this.emolumentoId = emolumentoId;
    }

    public String getEmolumentoNome() {
        return emolumentoNome;
    }

    public void setEmolumentoNome(String emolumentoNome) {
        this.emolumentoNome = emolumentoNome;
    }

    public Long getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(Long pagamentoEmolumentoId) {
        this.pagamentoId = pagamentoEmolumentoId;
    }

    public String getPagamentoNumero() {
        return pagamentoNumero;
    }

    public void setPagamentoNumero(String pagamentoEmolumentoNumero) {
        this.pagamentoNumero = pagamentoEmolumentoNumero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EfeitoPagamentoDTO efeitoPagamentoDTO = (EfeitoPagamentoDTO) o;
        if (efeitoPagamentoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), efeitoPagamentoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EfeitoPagamentoDTO{" +
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
            ", emolumentoId=" + getEmolumentoId() +
            ", emolumentoNome='" + getEmolumentoNome() + "'" +
            ", pagamentoId=" + getPagamentoId() +
            ", pagamentoNumero='" + getPagamentoNumero() + "'" +
            "}";
    }
}
