package com.ravunana.educacao.pagamento.service.mapper;

import com.ravunana.educacao.pagamento.domain.*;
import com.ravunana.educacao.pagamento.service.dto.EfeitoPagamentoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EfeitoPagamento} and its DTO {@link EfeitoPagamentoDTO}.
 */
@Mapper(componentModel = "spring", uses = {DepositoMapper.class, EmolumentoMapper.class, PagamentoEmolumentoMapper.class})
public interface EfeitoPagamentoMapper extends EntityMapper<EfeitoPagamentoDTO, EfeitoPagamento> {

    @Mapping(source = "emolumento.id", target = "emolumentoId")
    @Mapping(source = "emolumento.nome", target = "emolumentoNome")
    @Mapping(source = "pagamento.id", target = "pagamentoId")
    @Mapping(source = "pagamento.numero", target = "pagamentoNumero")
    EfeitoPagamentoDTO toDto(EfeitoPagamento efeitoPagamento);

    @Mapping(target = "removeDeposito", ignore = true)
    @Mapping(source = "emolumentoId", target = "emolumento")
    @Mapping(source = "pagamentoId", target = "pagamento")
    EfeitoPagamento toEntity(EfeitoPagamentoDTO efeitoPagamentoDTO);

    default EfeitoPagamento fromId(Long id) {
        if (id == null) {
            return null;
        }
        EfeitoPagamento efeitoPagamento = new EfeitoPagamento();
        efeitoPagamento.setId(id);
        return efeitoPagamento;
    }
}
