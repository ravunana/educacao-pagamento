package com.ravunana.educacao.pagamento.service.mapper;

import com.ravunana.educacao.pagamento.domain.*;
import com.ravunana.educacao.pagamento.service.dto.EmolumentoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Emolumento} and its DTO {@link EmolumentoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmolumentoMapper extends EntityMapper<EmolumentoDTO, Emolumento> {


    @Mapping(target = "efeitoPagamentos", ignore = true)
    @Mapping(target = "removeEfeitoPagamento", ignore = true)
    Emolumento toEntity(EmolumentoDTO emolumentoDTO);

    default Emolumento fromId(Long id) {
        if (id == null) {
            return null;
        }
        Emolumento emolumento = new Emolumento();
        emolumento.setId(id);
        return emolumento;
    }
}
