package br.com.hbsis.periodo.vendas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/periodo-vendas")
public class PeriodoVendasRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendasRest.class);
    private final PeriodoVendasService periodoVendasService;

    @Autowired
    public PeriodoVendasRest(PeriodoVendasService periodoVendasService) {
        this.periodoVendasService = periodoVendasService;
    }

    @PostMapping
    public PeriodoVendasDTO save(@RequestBody PeriodoVendasDTO periodoVendasDTO){
        LOGGER.info("Solicitação...");
        LOGGER.debug("Payload: {}", periodoVendasDTO);

        return this.periodoVendasService.save(periodoVendasDTO);
    }

    @GetMapping("/{id}")
    public PeriodoVendasDTO find(@PathVariable("id") Long id){
        LOGGER.info("Procurando por perído de venda de Id '{}'", id);

        return this.periodoVendasService.findById(id);
    }

    @PutMapping("/{id}")
    public PeriodoVendasDTO update(@RequestBody PeriodoVendasDTO periodoVendasDTO, @PathVariable("id") Long id){
        LOGGER.info("Atualizando período de vendas de Id '{}'", id);
        LOGGER.debug("Payload: {}", periodoVendasDTO);

        return this.periodoVendasService.update(periodoVendasDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Excluindo categoria de Id '{}'", id);

        this.periodoVendasService.delete(id);
    }
}
