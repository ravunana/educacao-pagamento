package com.ravunana.educacao.pagamento.repository;

import com.ravunana.educacao.pagamento.domain.ContaAluno;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ContaAluno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContaAlunoRepository extends JpaRepository<ContaAluno, Long> {

}
