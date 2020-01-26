package com.ravunana.educacao.pagamento.service.mapper;

import com.ravunana.educacao.pagamento.domain.*;
import com.ravunana.educacao.pagamento.service.dto.PagamentoEmolumentoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PagamentoEmolumento} and its DTO {@link PagamentoEmolumentoDTO}.
 */
@Mapper(componentModel = "spring", uses = {FormaLiquidacaoMapper.class})
public interface PagamentoEmolumentoMapper extends EntityMapper<PagamentoEmolumentoDTO, PagamentoEmolumento> {

    @Mapping(source = "formaLiquidacao.id", target = "formaLiquidacaoId")
    @Mapping(source = "formaLiquidacao.nome", target = "formaLiquidacaoNome")
    PagamentoEmolumentoDTO toDto(PagamentoEmolumento pagamentoEmolumento);

    @Mapping(target = "efeitoPagamentos", ignore = true)
    @Mapping(target = "removeEfeitoPagamento", ignore = true)
    @Mapping(source = "formaLiquidacaoId", target = "formaLiquidacao")
    PagamentoEmolumento toEntity(PagamentoEmolumentoDTO pagamentoEmolumentoDTO);

    default PagamentoEmolumento fromId(Long id) {
        if (id == null) {
            return null;
        }
        PagamentoEmolumento pagamentoEmolumento = new PagamentoEmolumento();
        pagamentoEmolumento.setId(id);
        return pagamentoEmolumento;
    }
}
