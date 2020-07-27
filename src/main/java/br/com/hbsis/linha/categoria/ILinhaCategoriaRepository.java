package br.com.hbsis.linha.categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ILinhaCategoriaRepository extends JpaRepository<LinhaCategoria, Long> {
    Optional<LinhaCategoria> findByCodigo(String codigo);
}
