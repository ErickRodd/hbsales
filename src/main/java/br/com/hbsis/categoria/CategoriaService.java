package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.importacao.exportacao.ImportExportCSV;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);
    private final ICategoriaRepository iCategoriaRepository;
    private final FornecedorService fornecedorService;
    private final ImportExportCSV importExportCSV;

    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService, ImportExportCSV importExportCSV) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;
        this.importExportCSV = importExportCSV;
    }

    public String formatarCodigoCategoriaObj(CategoriaDTO categoriaDTO) {
        Optional<Fornecedor> fornecedor = this.fornecedorService.findByFornecedorIdOptional(categoriaDTO.getFornecedor());

        return this.formatarCodigoCategoria(fornecedor.get().getCnpj().substring(10), StringUtils.leftPad(categoriaDTO.getCodigo(), 3, "0"));
    }

    public String formatarCodigoCategoria(String cnpj, String codigoUsuario){
        final String cat = "CAT";

        return cat + cnpj + codigoUsuario;
    }

    public String formatarCodigoCategoriaArray(String cnpj, String codigoIndex){
        if(codigoIndex.length() < 10){
            cnpj = (cnpj.replace(".", "").replace("/", "").replace("-", "")).substring(10);
            String codigoUsuario = StringUtils.leftPad(codigoIndex, 3, "0");

            return this.formatarCodigoCategoria(cnpj, codigoUsuario);
        }

        return codigoIndex;
    }

    private void validate(CategoriaDTO categoriaDTO) {

        if(categoriaDTO == null){
            throw new IllegalArgumentException("CategoriaDTO não pode estar vazia.");
        }

/* Validação Código categoria */
        if(StringUtils.isBlank(categoriaDTO.getCodigo())){
            throw new IllegalArgumentException("Código da categoria não pode estar vazio.");
        }

        if(categoriaDTO.getCodigo().length() > 3){
            throw new IllegalArgumentException("Código categoria fornecido pelo usuário deve conter no máximo 3 caracteres.");
        }

        if(!StringUtils.containsOnly(categoriaDTO.getCodigo(), "0123456789")){
            throw new IllegalArgumentException("Código categoria fornecido pelo usuário deve conter apenas caracteres númericos.");
        }

/*  . . . Fornecedor */
        if(categoriaDTO.getFornecedor() == null){
            throw new IllegalArgumentException("Fornecedor não pode estar vazio.");
        }

/*  . . . Nome categoria */
        if(StringUtils.isBlank(categoriaDTO.getNome())){
            throw new IllegalArgumentException("Nome da categoria não pode estar vazio.");
        }

        if(categoriaDTO.getNome().length() > 50){
            throw new IllegalArgumentException("Nome da categoria não pode conter mais de 50 caracteres.");
        }
    }

    public Categoria set(Categoria categoria, CategoriaDTO categoriaDTO, Fornecedor fornecedor){
        categoria.setCodigo(this.formatarCodigoCategoriaObj(categoriaDTO));
        categoria.setFornecedor(fornecedor);
        categoria.setNome(categoriaDTO.getNome());
        categoria = this.iCategoriaRepository.save(categoria);

        return categoria;
    }

    public Categoria save(CategoriaDTO categoriaDTO){
        this.validate(categoriaDTO);

        Optional<Fornecedor> fornecedorOptional = this.fornecedorService.findByFornecedorIdOptional(categoriaDTO.getFornecedor());
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findByCodigo(this.formatarCodigoCategoriaObj(categoriaDTO));

        if(!categoriaOptional.isPresent()){
            if(fornecedorOptional.isPresent()){
                Categoria categoria = new Categoria();

                return this.set(categoria, categoriaDTO, fornecedorOptional.get());
            }
            throw new IllegalArgumentException(String.format("Fornecedor de Id '%s' não existe.", fornecedorOptional.get().getId()));
        }
        throw new IllegalArgumentException(String.format("Código de categoria '%s' já existe.", categoriaOptional.get().getCodigo()));
    }

    public CategoriaDTO findById(Long id){
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if(categoriaOptional.isPresent()){
            return CategoriaDTO.of(categoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("Categoria de Id '%s' não existe.", id));
    }

    public Optional<Categoria> findByCategoriaIdOptional(Long id){
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if(categoriaOptional.isPresent()){
            return categoriaOptional;
        }

        throw new IllegalArgumentException(String.format("Categoria de Id '%s' não existe.", id));
    }

    public Optional<Categoria> findByCodigoCategoria(String codigoCategoria){

        return this.iCategoriaRepository.findByCodigo(codigoCategoria);
    }

    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        this.validate(categoriaDTO);

        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);
        Optional<Categoria> codCategoriaExistente = this.findByCodigoCategoria(this.formatarCodigoCategoriaObj(categoriaDTO));
        Optional<Fornecedor> fornecedorExistente = this.fornecedorService.findByFornecedorIdOptional(categoriaDTO.getFornecedor());

        if(categoriaOptional.isPresent() && fornecedorExistente.isPresent()){
            if(!codCategoriaExistente.isPresent() || this.formatarCodigoCategoriaObj(categoriaDTO).equals(categoriaOptional.get().getCodigo())){

                return CategoriaDTO.of(this.set(categoriaOptional.get(), categoriaDTO, fornecedorExistente.get()));
            }
            throw new IllegalArgumentException(String.format("Código de categoria '%s' já existe.", categoriaDTO.getCodigo()));
        }
        throw new IllegalArgumentException(String.format("Categoria de Id '%s' não existe.", id));
    }

    public void delete(Long id) {
        if(this.iCategoriaRepository.findById(id).isPresent()){

            this.iCategoriaRepository.deleteById(id);
        }
        else{

            throw new IllegalArgumentException(String.format("Categoria de Id '%s' não existe.", id));
        }
    }

    public void setImportarCategoria(Categoria categoria, String codigo, String nome, Fornecedor fornecedor){
        categoria.setCodigo(codigo);
        categoria.setNome(nome);
        categoria.setFornecedor(fornecedor);
    }

    public void importarCategoriaFile(MultipartFile file) throws IOException{
        String linha = "";

        BufferedReader reader = importExportCSV.reader(file);

        while((linha = reader.readLine()) != null) {
            String[] arrayCategoria = importExportCSV.array(linha, ";");

            Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findByCodigo(this.formatarCodigoCategoriaArray(arrayCategoria[3].replace(".", "").replace("/", "").replace("-", ""), arrayCategoria[0]));
            Optional<Fornecedor> fornecedorOptional = this.fornecedorService.findByCnpj(arrayCategoria[3].replace(".", "").replace("/", "").replace("-", ""));

            if(!categoriaOptional.isPresent() && fornecedorOptional.isPresent()){
                Categoria categoria = new Categoria();

                this.setImportarCategoria(categoria, arrayCategoria[0], arrayCategoria[1], fornecedorOptional.get());

                this.save(CategoriaDTO.of(categoria));
            }
        }
    }

    public void exportarCategoria(HttpServletResponse response) throws IOException, ParseException {
        List<Categoria> categoriaList = this.iCategoriaRepository.findAll();
        MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
        mask.setValueContainsLiteralCharacters(false);

        PrintWriter writer = importExportCSV.exportar(response, "categorias", "Código;Categoria;Razão Social;CNPJ;");

        for(Categoria categoria : categoriaList){
            writer.println(
                categoria.getCodigo()
                + ";" +
                categoria.getNome()
                + ";" +
                categoria.getFornecedor().getRazaoSocial()
                + ";" +
                mask.valueToString(categoria.getFornecedor().getCnpj()));
        }

        writer.close();
    }
}
