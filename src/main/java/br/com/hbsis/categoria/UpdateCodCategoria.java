package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateCodCategoria {
    private final ICategoriaRepository iCategoriaRepository;

    public UpdateCodCategoria(ICategoriaRepository iCategoriaRepository) {
        this.iCategoriaRepository = iCategoriaRepository;
    }

    public void updateByFornecedor(Fornecedor fornecedor){
        List<Categoria> categoriaList = this.iCategoriaRepository.findByFornecedor_Id(fornecedor.getId());

        if(categoriaList != null){
            for(Categoria categoria : categoriaList){
                categoria.setCodigo("CAT" + fornecedor.getCnpj().substring(10) + categoria.getCodigo().substring(7));
                categoria.setFornecedor(fornecedor);
                this.iCategoriaRepository.save(categoria);
            }
        }
    }
}
