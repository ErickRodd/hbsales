package br.com.hbsis.produto;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaDTO;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.importacao.exportacao.ImportExportCSV;
import br.com.hbsis.linha.categoria.LinhaCategoria;
import br.com.hbsis.linha.categoria.LinhaCategoriaDTO;
import br.com.hbsis.linha.categoria.LinhaCategoriaService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;
    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;
    private final ImportExportCSV importExportCSV;

    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService, CategoriaService categoriaService, FornecedorService fornecedorService, ImportExportCSV importExportCSV) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;
        this.importExportCSV = importExportCSV;
    }

    public String formatarCodigoProduto(String codigoProduto){

        return StringUtils.leftPad(StringUtils.upperCase(codigoProduto), 10, "0");
    }

    public void validate(ProdutoDTO produtoDTO){
        if (produtoDTO == null) {
            throw new IllegalArgumentException("ProdutoDTO não pode ser nulo.");
        }

/*   Validação Código do produto */
        if(StringUtils.isBlank(produtoDTO.getCodigo())){
            throw new IllegalArgumentException("Código do produto não pode estar vazio.");
        }

        if(produtoDTO.getCodigo().length() > 10){
            throw new IllegalArgumentException("Código do produto excedeu o limite de caracteres.");
        }

/*  . . . Nome do produto */
        if(StringUtils.isBlank(produtoDTO.getNome())){
            throw new IllegalArgumentException("Nome do produto não pode estar vazio.");
        }

        if(produtoDTO.getNome().length() > 200){
            throw new IllegalArgumentException("Nome do produto excedeu o limite de caracteres.");
        }

/*  . . . Preço do produto */
        if(produtoDTO.getPreco() == null){
            throw new IllegalArgumentException("Preço do produto não pode estar vazio.");
        }

/*  . . . Linha de categoria */
        if (produtoDTO.getLinhaCategoria() == null) {
            throw new IllegalArgumentException("Linha de categoria não pode estar vazio.");
        }

/*  . . . Unidade por caixa */
        if(produtoDTO.getUnCaixa() == null){
            throw new IllegalArgumentException("Unidade por caixa não pode estar vazio.");
        }

/*  . . . Peso por unidade */
        if(produtoDTO.getUnPeso() == null){
            throw new IllegalArgumentException("Peso por unidade não pode estar vazio.");
        }

/*  . . . Medida do peso */
        if(StringUtils.isBlank(produtoDTO.getMedidaPeso())){
            throw new IllegalArgumentException("Medida do peso não pode estar vazio.");
        }

        if(!(StringUtils.lowerCase(produtoDTO.getMedidaPeso()).equals("mg") || StringUtils.lowerCase(produtoDTO.getMedidaPeso()).equals("g") || StringUtils.lowerCase(produtoDTO.getMedidaPeso()).equals("kg"))){
            throw new IllegalArgumentException("Medida do peso inválida.");
        }

/*  . . . Validade do produto */
        if(produtoDTO.getValidade() == null){
            throw new IllegalArgumentException("Validade do produto não pode estar vazio.");
        }
    }

    public Produto set(Produto produto, ProdutoDTO produtoDTO, LinhaCategoria linhaCategoria){
        produto.setCodigo(this.formatarCodigoProduto(produtoDTO.getCodigo()));
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setLinhaCategoria(linhaCategoria);
        produto.setUnCaixa(produtoDTO.getUnCaixa());
        produto.setUnPeso(produtoDTO.getUnPeso());
        produto.setMedidaPeso(StringUtils.lowerCase(produtoDTO.getMedidaPeso()));
        produto.setValidade(produtoDTO.getValidade());
        produto = this.iProdutoRepository.save(produto);

        return produto;
    }

    public Produto save(ProdutoDTO produtoDTO) {
        this.validate(produtoDTO);

        Optional<LinhaCategoria> linhaCategoria = this.linhaCategoriaService.findByLinhaCategoriaIdOptional(produtoDTO.getLinhaCategoria());

        if(!this.iProdutoRepository.findByCodigo(this.formatarCodigoProduto(produtoDTO.getCodigo())).isPresent()){
            if(linhaCategoria.isPresent()){
                Produto produto = new Produto();

                return this.set(produto, produtoDTO, linhaCategoria.get());
            }
            throw new IllegalArgumentException(String.format("Linha de categoria de Id '%s' não existe.", linhaCategoria.get().getId()));
        }
        throw new IllegalArgumentException(String.format("Produto com código '%s' já existe.", produtoDTO.getCodigo()));
    }

    public ProdutoDTO findById(Long id){
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if(produtoOptional.isPresent()){
            return ProdutoDTO.of(produtoOptional.get());
        }

        throw new IllegalArgumentException(String.format("Produto de Id '%s' não existe.", id));
    }

    public Optional<Produto> findByNomeProduto(String nomeProduto){
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByNome(nomeProduto);

        if(produtoOptional.isPresent()){
            return produtoOptional;
        }

        throw new IllegalArgumentException(String.format("Produto de nome '%s' não existe.", nomeProduto));
    }

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id){
        this.validate(produtoDTO);

        Optional<Produto> produtoExistente = this.iProdutoRepository.findById(id);
        Optional<LinhaCategoria> linhaCategoriaExistente = this.linhaCategoriaService.findByLinhaCategoriaIdOptional(produtoDTO.getLinhaCategoria());

        if(produtoExistente.isPresent() && linhaCategoriaExistente.isPresent()){

            return ProdutoDTO.of(this.set(produtoExistente.get(), produtoDTO, linhaCategoriaExistente.get()));
        }
        throw new IllegalArgumentException(String.format("Código de '%s' não existe.", id));
    }

    public void delete(Long id){
        if(this.iProdutoRepository.findById(id).isPresent()){

            this.iProdutoRepository.deleteById(id);
        } else{

            throw new IllegalArgumentException(String.format("Produto de Id '%s' não existe.", id));
        }
    }

    public void setImportarProduto(Produto produto, String codigo, String nome, String preco, LinhaCategoria linhaCategoria, String unCaixa, String unPeso, String medidaPeso, String validade){
        produto.setCodigo(this.formatarCodigoProduto(codigo));
        produto.setNome(nome);
        produto.setPreco(Double.parseDouble(StringUtils.replaceChars(preco, "R$.", "").replace(",", ".")));
        produto.setLinhaCategoria(linhaCategoria);
        produto.setUnCaixa(Integer.parseInt(unCaixa));
        produto.setUnPeso(Double.parseDouble(StringUtils.replaceChars(unPeso.toLowerCase(), "mgk", "")));
        produto.setMedidaPeso(StringUtils.replaceChars(medidaPeso.toLowerCase(), ".0123456789", ""));
        produto.setValidade(LocalDate.parse(validade.substring(6) + "-" + validade.substring(3, 5) + "-" + validade.substring(0, 2)));
    }

    public void importarProdutoFile(MultipartFile file) throws IOException{
        String linha = "";

        BufferedReader reader = importExportCSV.reader(file);

        while((linha = reader.readLine()) != null){
            String[] arrayP = importExportCSV.array(linha, ";");

            Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigo(StringUtils.leftPad(arrayP[0], 10, "0"));
            Optional<LinhaCategoria> linhaCategoriaOptional = this.linhaCategoriaService.findByCodigoLinha(StringUtils.leftPad(StringUtils.upperCase(arrayP[6]), 10, "0"));

            if(!produtoOptional.isPresent() && linhaCategoriaOptional.isPresent()){
                Produto produto = new Produto();

                this.setImportarProduto(produto, arrayP[0], arrayP[1], arrayP[2], linhaCategoriaOptional.get(), arrayP[3], arrayP[4], arrayP[4], arrayP[5]);

                this.save(ProdutoDTO.of(produto));
            }
        }
    }

    public void importarProdutoPorFornecedor(MultipartFile file, Long id) throws IOException {
        Optional<Fornecedor> fornecedorOptional = this.fornecedorService.findByFornecedorIdOptional(id);

        if(fornecedorOptional.isPresent()){
            String linha = "";

            BufferedReader reader = importExportCSV.reader(file);

            while((linha = reader.readLine()) != null){
                String[] arrayProdFornecedor = importExportCSV.array(linha,";");

                Optional<Categoria> categoriaOptional = this.categoriaService.findByCodigoCategoria(this.categoriaService.formatarCodigoCategoriaArray(fornecedorOptional.get().getCnpj(), arrayProdFornecedor[8]));
                Optional<LinhaCategoria> linhaCategoriaOptional = this.linhaCategoriaService.findByCodigoLinha(this.linhaCategoriaService.formatarCodigoLinha(arrayProdFornecedor[6]));
                Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigo(this.formatarCodigoProduto(arrayProdFornecedor[0]));

                /* Se a categoria não existir */
                if(!categoriaOptional.isPresent()){
                    Categoria categoria = new Categoria();
                    this.categoriaService.setImportarCategoria(categoria, arrayProdFornecedor[8], arrayProdFornecedor[9], fornecedorOptional.get());
                    categoria = this.categoriaService.save(CategoriaDTO.of(categoria));

                    LinhaCategoria linhaCategoria = new LinhaCategoria();
                    this.linhaCategoriaService.setImportarLinhaCategoria(linhaCategoria, arrayProdFornecedor[6], arrayProdFornecedor[7], categoria);
                    linhaCategoria = this.linhaCategoriaService.save(LinhaCategoriaDTO.of(linhaCategoria));

                    Produto produto = new Produto();
                    this.setImportarProduto(produto, arrayProdFornecedor[0], arrayProdFornecedor[1], arrayProdFornecedor[2], linhaCategoria, arrayProdFornecedor[3], arrayProdFornecedor[4], arrayProdFornecedor[4], arrayProdFornecedor[5]);
                    this.save(ProdutoDTO.of(produto));
                }

                /* Se a linha de categoria não existir */
                if(!linhaCategoriaOptional.isPresent() && categoriaOptional.isPresent()){
                    LinhaCategoria linhaCategoria = new LinhaCategoria();
                    this.linhaCategoriaService.setImportarLinhaCategoria(linhaCategoria, arrayProdFornecedor[6], arrayProdFornecedor[7], categoriaOptional.get());
                    linhaCategoria = this.linhaCategoriaService.save(LinhaCategoriaDTO.of(linhaCategoria));

                    Produto produto = new Produto();
                    this.setImportarProduto(produto, arrayProdFornecedor[0], arrayProdFornecedor[1], arrayProdFornecedor[2], linhaCategoria, arrayProdFornecedor[3], arrayProdFornecedor[4], arrayProdFornecedor[4], arrayProdFornecedor[5]);
                    this.save(ProdutoDTO.of(produto));
                }

                /* Se o produto não existir */
                if(!produtoOptional.isPresent() && linhaCategoriaOptional.isPresent()){
                    Produto produto = new Produto();
                    this.setImportarProduto(produto, arrayProdFornecedor[0], arrayProdFornecedor[1], arrayProdFornecedor[2], linhaCategoriaOptional.get(), arrayProdFornecedor[3], arrayProdFornecedor[4], arrayProdFornecedor[4], arrayProdFornecedor[5]);
                    this.save(ProdutoDTO.of(produto));
                }

                /* Se a linha de categoria existir */
                if(linhaCategoriaOptional.isPresent() && categoriaOptional.isPresent()){
                    LinhaCategoria linhaCategoriaExistente = linhaCategoriaOptional.get();
                    this.linhaCategoriaService.setImportarLinhaCategoria(linhaCategoriaExistente, arrayProdFornecedor[6], arrayProdFornecedor[7], categoriaOptional.get());
                    this.linhaCategoriaService.update(LinhaCategoriaDTO.of(linhaCategoriaExistente), linhaCategoriaExistente.getId());
                }

                /* Se o produto existir */
                if(produtoOptional.isPresent() && linhaCategoriaOptional.isPresent()){
                    Produto produtoExistente = produtoOptional.get();
                    this.setImportarProduto(produtoExistente, arrayProdFornecedor[0], arrayProdFornecedor[1], arrayProdFornecedor[2], linhaCategoriaOptional.get(), arrayProdFornecedor[3], arrayProdFornecedor[4], arrayProdFornecedor[4], arrayProdFornecedor[5]);
                    this.update(ProdutoDTO.of(produtoExistente), produtoExistente.getId());
                }
            }
        }
        else{
            throw new IllegalArgumentException(String.format("Fornecedor de Id '%s' não existe.", id));
        }
    }

    public void exportarProduto(HttpServletResponse response) throws IOException, ParseException {
        List<Produto> produtoList = this.iProdutoRepository.findAll();

        DateTimeFormatter dataMasc = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        MaskFormatter cnpjMasc = new MaskFormatter("##.###.###/####-##");
        cnpjMasc.setValueContainsLiteralCharacters(false);

        PrintWriter writer = importExportCSV.exportar(response, "produtos", "Código;Nome;Preço;Unidade por caixa;Peso por unidade;Validade;Código linha;Nome linha;Código categoria;Nome categoria;Fornecedor CNPJ; Razão Social;");

        for(Produto produto : produtoList){
            writer.println(
                produto.getCodigo()
                + ";" +
                produto.getNome()
                + ";" +
                "R$" + produto.getPreco().toString().replace(".", ",")
                + ";" +
                produto.getUnCaixa()
                + ";" +
                produto.getUnPeso() + produto.getMedidaPeso()
                + ";" +
                produto.getValidade().format(dataMasc)
                + ";" +
                produto.getLinhaCategoria().getCodigo()
                + ";" +
                produto.getLinhaCategoria().getNome()
                + ";" +
                produto.getLinhaCategoria().getCategoria().getCodigo()
                + ";" +
                produto.getLinhaCategoria().getCategoria().getNome()
                + ";" +
                cnpjMasc.valueToString(produto.getLinhaCategoria().getCategoria().getFornecedor().getCnpj())
                + ";" +
                produto.getLinhaCategoria().getCategoria().getFornecedor().getRazaoSocial()
            );
        }

        writer.close();
    }
}