package br.com.hbsis.linha.categoria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/linha-categoria")
public class LinhaCategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaRest.class);
    private final LinhaCategoriaService linhaCategoriaService;

    @Autowired
    public LinhaCategoriaRest(LinhaCategoriaService linhaCategoriaService) {
        this.linhaCategoriaService = linhaCategoriaService;
    }

    @PostMapping
    public LinhaCategoria save(@RequestBody LinhaCategoriaDTO linhaCategoriaDTO){
        LOGGER.info("Solicitação do usuário...");
        LOGGER.info("Validando nova linha de categoria...");
        LOGGER.info("Cadastrando nova linha de categoria '{}'...", linhaCategoriaDTO.getNome());
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.save(linhaCategoriaDTO);
    }

    @GetMapping("/{id}")
    public LinhaCategoriaDTO find(@PathVariable("id") Long id){
        LOGGER.info("Procurando por linha de categoria de Id '{}'...", id);

        return this.linhaCategoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public LinhaCategoriaDTO update(@PathVariable("id") Long id, @RequestBody LinhaCategoriaDTO linhaCategoriaDTO){
        LOGGER.info("Atualizando linha de categoria '{}'...", linhaCategoriaDTO.getNome());
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.update(linhaCategoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Excluindo linha de categoria de Id '{}'...", id);

        this.linhaCategoriaService.delete(id);
    }

    @GetMapping(value = "/exportar")
    public void exportarLinhaCategoria(HttpServletResponse response) throws IOException {
        LOGGER.info("Exportando linhas de categoria para CSV...");

        this.linhaCategoriaService.exportarLinhaCategoria(response);
    }

    @PostMapping(value = "/importar", consumes = "multipart/form-data")
    public void importarLinhaCategoriaFile(@RequestParam("file") MultipartFile file) throws IOException{
        LOGGER.info("Importando linhas de categoria de CSV...");

        this.linhaCategoriaService.importarLinhaCategoriaFile(file);
    }
}
