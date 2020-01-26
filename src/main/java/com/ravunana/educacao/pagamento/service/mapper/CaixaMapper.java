package com.ravunana.educacao.pagamento.service.mapper;

import com.ravunana.educacao.pagamento.domain.*;
import com.ravunana.educacao.pagamento.service.dto.CaixaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Caixa} and its DTO {@link CaixaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CaixaMapper extends EntityMapper<CaixaDTO, Caixa> {


    @Mapping(target = "depositos", ignore = true)
    @Mapping(target = "removeDeposito", ignore = true)
    Caixa toEntity(CaixaDTO caixaDTO);

    default Caixa fromId(Long id) {
        if (id == null) {
            return null;
        }
        Caixa caixa = new Caixa();
        caixa.setId(id);
        return caixa;
    }
}
