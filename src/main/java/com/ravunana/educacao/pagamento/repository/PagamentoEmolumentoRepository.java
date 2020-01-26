package com.ravunana.educacao.pagamento.repository;

import com.ravunana.educacao.pagamento.domain.PagamentoEmolumento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PagamentoEmolumento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagamentoEmolumentoRepository extends JpaRepository<PagamentoEmolumento, Long> {

}
