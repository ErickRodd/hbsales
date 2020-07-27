package br.com.hbsis.linha.categoria;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.importacao.exportacao.ImportExportCSV;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service
public class LinhaCategoriaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaService categoriaService;
    private final ImportExportCSV importExportCSV;

    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaService categoriaService, ImportExportCSV importExportCSV) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaService = categoriaService;
        this.importExportCSV = importExportCSV;
    }

    public String formatarCodigoLinha(String codigoLinha){

        return StringUtils.leftPad(StringUtils.upperCase(codigoLinha), 10, "0");
    }

    public void validate(LinhaCategoriaDTO linhaCategoriaDTO){
        LOGGER.info("Validando {}", linhaCategoriaDTO);

        if(linhaCategoriaDTO == null){
            throw new IllegalArgumentException("LinhaCategoriaDTO não pode estar vazia.");
        }

/*   Validação Código da Categoria */
        if(StringUtils.isBlank(linhaCategoriaDTO.getCodigo())){
            throw new IllegalArgumentException("Código da linha não pode estar vazio.");
        }

        if(linhaCategoriaDTO.getCodigo().length() > 10){
            throw new IllegalArgumentException("Tamanho do código da linha inválido.");
        }

/*  . . . Categoria */
        if(linhaCategoriaDTO.getCategoria() == null){
            throw new IllegalArgumentException("Categoria não pode estar vazia.");
        }

/*  . . . Nome */
        if(StringUtils.isBlank(linhaCategoriaDTO.getNome())){
            throw new IllegalArgumentException("Nome da linha não pode estar vazia.");
        }

        if(linhaCategoriaDTO.getNome().length() > 50){
            throw new IllegalArgumentException("Tamanho do nome da linha de categoria inválido.");
        }
    }

    private LinhaCategoria set(LinhaCategoria linhaCategoria, LinhaCategoriaDTO linhaCategoriaDTO, Categoria categoria){
        linhaCategoria.setCodigo(this.formatarCodigoLinha(linhaCategoriaDTO.getCodigo()));
        linhaCategoria.setCategoria(categoria);
        linhaCategoria.setNome(linhaCategoriaDTO.getNome());
        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return linhaCategoria;
    }

    public LinhaCategoria save(LinhaCategoriaDTO linhaCategoriaDTO){
        this.validate(linhaCategoriaDTO);

        Optional<LinhaCategoria> linhaCategoriaOptional = this.findByCodigoLinha(this.formatarCodigoLinha(linhaCategoriaDTO.getCodigo()));
        Optional<Categoria> categoria = this.categoriaService.findByCategoriaIdOptional(linhaCategoriaDTO.getCategoria());

        if(!linhaCategoriaOptional.isPresent()){
            if(categoria.isPresent()){
                LinhaCategoria linhaCategoria = new LinhaCategoria();

                return this.set(linhaCategoria, linhaCategoriaDTO, categoria.get());
            }
            throw new IllegalArgumentException("Categoria não existente.");
        }
        throw new IllegalArgumentException("Linha de categoria já existente.");
    }

    public LinhaCategoriaDTO findById(Long id){
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if(linhaCategoriaOptional.isPresent()){
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("Linha de categoria de Id '%s' não existe.", id));
    }

    public Optional<LinhaCategoria> findByLinhaCategoriaIdOptional(Long id){
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if(linhaCategoriaOptional.isPresent()){
            return linhaCategoriaOptional;
        }

        throw new IllegalArgumentException(String.format("Linha de categoria de Id '%s' não existe.", id));
    }

    public Optional<LinhaCategoria> findByCodigoLinha(String codigoLinha){

        return this.iLinhaCategoriaRepository.findByCodigo(codigoLinha);
    }

    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id){
        this.validate(linhaCategoriaDTO);

        Optional<LinhaCategoria> linhaCategoriaExistente = this.iLinhaCategoriaRepository.findById(id);
        Optional<Categoria> categoriaExistente = this.categoriaService.findByCategoriaIdOptional(linhaCategoriaDTO.getCategoria());

        if(linhaCategoriaExistente.isPresent()){
            if(categoriaExistente.isPresent()){

                return LinhaCategoriaDTO.of(this.set(linhaCategoriaExistente.get(), linhaCategoriaDTO, categoriaExistente.get()));
            }
            throw new IllegalArgumentException(String.format("Categoria de código '%s' não existe.", categoriaExistente.get().getCodigo()));
        }
        throw new IllegalArgumentException(String.format("Linha de categoria de Id '%s' não existe.", id));
    }

    public void delete(Long id){
        if(this.iLinhaCategoriaRepository.findById(id).isPresent()){

            this.iLinhaCategoriaRepository.deleteById(id);
        } else{

            throw new IllegalArgumentException(String.format("Linha de categoria de Id '%s' não existe.", id));
        }
    }

    public void setImportarLinhaCategoria(LinhaCategoria linhaCategoria, String codigo, String nome, Categoria categoria){
        linhaCategoria.setCodigo(codigo);
        linhaCategoria.setNome(nome);
        linhaCategoria.setCategoria(categoria);
    }

    public void importarLinhaCategoriaFile(MultipartFile file) throws IOException{
        String linha = "";

        BufferedReader reader = importExportCSV.reader(file);

        while((linha = reader.readLine()) != null){
            String[] arrayLinha = importExportCSV.array(linha, ";");

            Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findByCodigo(StringUtils.leftPad(StringUtils.upperCase(arrayLinha[0]), 10, "0"));
            Optional<Categoria> categoriaOptional = this.categoriaService.findByCodigoCategoria(arrayLinha[2]);

            if(!linhaCategoriaOptional.isPresent() && categoriaOptional.isPresent()){
                LinhaCategoria linhaCategoria = new LinhaCategoria();

                this.setImportarLinhaCategoria(linhaCategoria, arrayLinha[0], arrayLinha[1], categoriaOptional.get());

                this.save(LinhaCategoriaDTO.of(linhaCategoria));
            }
        }
    }

    public void exportarLinhaCategoria(HttpServletResponse response) throws IOException {
        List<LinhaCategoria> linhaCategoriaList = this.iLinhaCategoriaRepository.findAll();

        PrintWriter writer = importExportCSV.exportar(response, "linhas_categoria", "Código;Nome;Código Categoria;Nome Categoria;");

        for(LinhaCategoria linhaCategoria : linhaCategoriaList){
            writer.println(
                linhaCategoria.getCodigo()
                + ";" +
                linhaCategoria.getNome()
                + ";" +
                linhaCategoria.getCategoria().getCodigo()
                + ";" +
                linhaCategoria.getCategoria().getNome()
            );
        }

        writer.close();
    }
}