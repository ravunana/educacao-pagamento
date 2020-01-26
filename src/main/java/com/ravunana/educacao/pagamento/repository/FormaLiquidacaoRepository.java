package com.ravunana.educacao.pagamento.repository;

import com.ravunana.educacao.pagamento.domain.FormaLiquidacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FormaLiquidacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormaLiquidacaoRepository extends JpaRepository<FormaLiquidacao, Long> {

}
