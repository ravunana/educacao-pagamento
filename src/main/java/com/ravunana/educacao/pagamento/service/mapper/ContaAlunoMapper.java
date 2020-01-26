package com.ravunana.educacao.pagamento.service.mapper;

import com.ravunana.educacao.pagamento.domain.*;
import com.ravunana.educacao.pagamento.service.dto.ContaAlunoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContaAluno} and its DTO {@link ContaAlunoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContaAlunoMapper extends EntityMapper<ContaAlunoDTO, ContaAluno> {



    default ContaAluno fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContaAluno contaAluno = new ContaAluno();
        contaAluno.setId(id);
        return contaAluno;
    }
}
