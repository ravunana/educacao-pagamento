package com.ravunana.educacao.pagamento.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pagamento.domain.PagamentoEmolumento} entity.
 */
public class PagamentoEmolumentoDTO implements Serializable {

    private Long id;

    private ZonedDateTime data;

    @NotNull
    private String numero;

    @NotNull
    private String numeroProcesso;

    @NotNull
    private String nomeAluno;

    @NotNull
    private String turmaAluno;

    private String estado;


    private Long formaLiquidacaoId;

    private String formaLiquidacaoNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getTurmaAluno() {
        return turmaAluno;
    }

    public void setTurmaAluno(String turmaAluno) {
        this.turmaAluno = turmaAluno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getFormaLiquidacaoId() {
        return formaLiquidacaoId;
    }

    public void setFormaLiquidacaoId(Long formaLiquidacaoId) {
        this.formaLiquidacaoId = formaLiquidacaoId;
    }

    public String getFormaLiquidacaoNome() {
        return formaLiquidacaoNome;
    }

    public void setFormaLiquidacaoNome(String formaLiquidacaoNome) {
        this.formaLiquidacaoNome = formaLiquidacaoNome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PagamentoEmolumentoDTO pagamentoEmolumentoDTO = (PagamentoEmolumentoDTO) o;
        if (pagamentoEmolumentoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pagamentoEmolumentoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PagamentoEmolumentoDTO{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", numero='" + getNumero() + "'" +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            ", nomeAluno='" + getNomeAluno() + "'" +
            ", turmaAluno='" + getTurmaAluno() + "'" +
            ", estado='" + getEstado() + "'" +
            ", formaLiquidacaoId=" + getFormaLiquidacaoId() +
            ", formaLiquidacaoNome='" + getFormaLiquidacaoNome() + "'" +
            "}";
    }
}
