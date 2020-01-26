package com.ravunana.educacao.pagamento.repository;

import com.ravunana.educacao.pagamento.domain.Emolumento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Emolumento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmolumentoRepository extends JpaRepository<Emolumento, Long> {

}
