package br.com.hbsis.produto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/produto")
public class ProdutoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRest.class);
    private final ProdutoService produtoService;

    @Autowired
    public ProdutoRest(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public Produto save(@RequestBody ProdutoDTO produtoDTO){
        LOGGER.info("Solicitação do usuário...");
        LOGGER.info("Validando novo produto...");
        LOGGER.info("Cadastrando novo produto: {}", produtoDTO);
        LOGGER.debug("Payload: {}", produtoDTO);

        return this.produtoService.save(produtoDTO);
    }

    @GetMapping("/{id}")
    public ProdutoDTO find(@PathVariable("id") Long id){
        LOGGER.info("Procurando por produto de Id '{}'", id);

        return this.produtoService.findById(id);
    }

    @PutMapping("/{id}")
    public ProdutoDTO update(@PathVariable("id") Long id, @RequestBody ProdutoDTO produtoDTO){
        LOGGER.info("Atualizando produto de Id '{}'", id);
        LOGGER.debug("Payload: {}", produtoDTO);

        return this.produtoService.update(produtoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Excluindo produto de Id '{}'", id);

        this.produtoService.delete(id);
    }

    @GetMapping(value = "/exportar")
    public void exportarProduto(HttpServletResponse response) throws IOException, ParseException{
        LOGGER.info("Exportando produtos para CSV...");

        this.produtoService.exportarProduto(response);
    }

    @PostMapping(value = "/importar", consumes = "multipart/form-data")
    public void importarProdutoFile(@RequestParam("file")MultipartFile file) throws IOException{
        LOGGER.info("Importando produtos de CSV...");

        this.produtoService.importarProdutoFile(file);
    }

    @PostMapping(value = "/importar-fornecedor/{id}", consumes = "multipart/form-data")
    public void importarProdutoFornecedor(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) throws IOException{
        LOGGER.info("Importando produtos de CSV por fornecedor...");

        this.produtoService.importarProdutoPorFornecedor(file, id);
    }
}
