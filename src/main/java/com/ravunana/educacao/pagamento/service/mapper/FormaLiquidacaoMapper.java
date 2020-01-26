package com.ravunana.educacao.pagamento.service.mapper;

import com.ravunana.educacao.pagamento.domain.*;
import com.ravunana.educacao.pagamento.service.dto.FormaLiquidacaoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormaLiquidacao} and its DTO {@link FormaLiquidacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FormaLiquidacaoMapper extends EntityMapper<FormaLiquidacaoDTO, FormaLiquidacao> {


    @Mapping(target = "pagamentoEmolumentos", ignore = true)
    @Mapping(target = "removePagamentoEmolumento", ignore = true)
    FormaLiquidacao toEntity(FormaLiquidacaoDTO formaLiquidacaoDTO);

    default FormaLiquidacao fromId(Long id) {
        if (id == null) {
            return null;
        }
        FormaLiquidacao formaLiquidacao = new FormaLiquidacao();
        formaLiquidacao.setId(id);
        return formaLiquidacao;
    }
}
