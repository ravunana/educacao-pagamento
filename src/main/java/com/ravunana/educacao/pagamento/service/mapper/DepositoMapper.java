package com.ravunana.educacao.pagamento.service.mapper;

import com.ravunana.educacao.pagamento.domain.*;
import com.ravunana.educacao.pagamento.service.dto.DepositoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Deposito} and its DTO {@link DepositoDTO}.
 */
@Mapper(componentModel = "spring", uses = {CaixaMapper.class})
public interface DepositoMapper extends EntityMapper<DepositoDTO, Deposito> {

    @Mapping(source = "bancoCaixa.id", target = "bancoCaixaId")
    @Mapping(source = "bancoCaixa.descricao", target = "bancoCaixaDescricao")
    DepositoDTO toDto(Deposito deposito);

    @Mapping(source = "bancoCaixaId", target = "bancoCaixa")
    @Mapping(target = "efeitos", ignore = true)
    @Mapping(target = "removeEfeito", ignore = true)
    Deposito toEntity(DepositoDTO depositoDTO);

    default Deposito fromId(Long id) {
        if (id == null) {
            return null;
        }
        Deposito deposito = new Deposito();
        deposito.setId(id);
        return deposito;
    }
}
