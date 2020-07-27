package br.com.hbsis.categoria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/categoria")
public class CategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaRest.class);
    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaRest(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public Categoria save(@RequestBody CategoriaDTO categoriaDTO){
        LOGGER.info("Solicitação do usuário...");
        LOGGER.info("Validando nova categoria...");
        LOGGER.info("Cadastrando nova categoria '{}'...", this.categoriaService.formatarCodigoCategoriaObj(categoriaDTO));
        LOGGER.debug("Payload: {}", categoriaDTO);

        return this.categoriaService.save(categoriaDTO);
    }

    @GetMapping("/{id}")
    public CategoriaDTO find(@PathVariable("id") Long id){
        LOGGER.info("Procurando por categoria de Id '{}'...", id);

        return this.categoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoriaDTO update(@PathVariable("id") Long id, @RequestBody CategoriaDTO categoriaDTO){
        LOGGER.info("Atualizando categoria '{}'...", id);
        LOGGER.debug("Payload: {}", categoriaDTO);

        return this.categoriaService.update(categoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Excluindo categoria '{}'...", id);

        this.categoriaService.delete(id);
    }

    @GetMapping(value = "/exportar")
    public void exportarCategoria(HttpServletResponse resp) throws IOException, ParseException{
        LOGGER.info("Exportando categorias para CSV...");

        this.categoriaService.exportarCategoria(resp);
    }

    @PostMapping(value = "/importar", consumes = "multipart/form-data")
    public void importarCategoriaFile(@RequestParam("file") MultipartFile file) throws IOException{
        LOGGER.info("Importando categorias de CSV...");

        this.categoriaService.importarCategoriaFile(file);
    }
}
