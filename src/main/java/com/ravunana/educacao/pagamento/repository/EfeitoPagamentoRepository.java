package com.ravunana.educacao.pagamento.repository;

import com.ravunana.educacao.pagamento.domain.EfeitoPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the EfeitoPagamento entity.
 */
@Repository
public interface EfeitoPagamentoRepository extends JpaRepository<EfeitoPagamento, Long> {

    @Query(value = "select distinct efeitoPagamento from EfeitoPagamento efeitoPagamento left join fetch efeitoPagamento.depositos",
        countQuery = "select count(distinct efeitoPagamento) from EfeitoPagamento efeitoPagamento")
    Page<EfeitoPagamento> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct efeitoPagamento from EfeitoPagamento efeitoPagamento left join fetch efeitoPagamento.depositos")
    List<EfeitoPagamento> findAllWithEagerRelationships();

    @Query("select efeitoPagamento from EfeitoPagamento efeitoPagamento left join fetch efeitoPagamento.depositos where efeitoPagamento.id =:id")
    Optional<EfeitoPagamento> findOneWithEagerRelationships(@Param("id") Long id);

}
