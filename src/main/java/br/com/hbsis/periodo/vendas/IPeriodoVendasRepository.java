package br.com.hbsis.periodo.vendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
interface IPeriodoVendasRepository extends JpaRepository<PeriodoVendas, Long> {
    Optional<PeriodoVendas> findByDataFim(LocalDate data);
}
