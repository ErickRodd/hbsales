package br.com.hbsis.fornecedor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorRest.class);
    private final FornecedorService fornecedorService;

    @Autowired
    public FornecedorRest(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public FornecedorDTO save(@RequestBody FornecedorDTO fornecedorDTO){
        LOGGER.info("Solicitação do usuário...");
        LOGGER.info("Validando novo fornecedor...");
        LOGGER.info("Cadastrando novo fornecedor '{}'...", fornecedorDTO.getRazaoSocial());
        LOGGER.debug("Payload: {}", fornecedorDTO);

        return this.fornecedorService.save(fornecedorDTO);
    }

    @GetMapping("/{id}")
    public FornecedorDTO find(@PathVariable("id") Long id){
        LOGGER.info("Procurando por fornecedor de Id '{}'...", id);

        return this.fornecedorService.findById(id);
    }

    @PutMapping("/{id}")
    public FornecedorDTO update(@PathVariable("id") Long id, @RequestBody FornecedorDTO fornecedorDTO){
        LOGGER.info("Atualizando fornecedor '{}'...", fornecedorDTO.getRazaoSocial());
        LOGGER.debug("Payload: {}", fornecedorDTO);

        return this.fornecedorService.update(fornecedorDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Excluindo fornecedor de Id '{}'...", id);

        this.fornecedorService.delete(id);
    }
}
