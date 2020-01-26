package com.ravunana.educacao.pagamento.repository;

import com.ravunana.educacao.pagamento.domain.Caixa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Caixa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaixaRepository extends JpaRepository<Caixa, Long> {

}
