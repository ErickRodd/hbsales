package br.com.hbsis.fornecedor;

import br.com.hbsis.categoria.UpdateCodCategoria;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FornecedorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);
    private final IFornecedorRepository iFornecedorRepository;
    private final UpdateCodCategoria updateCodCategoria;

    public FornecedorService(IFornecedorRepository iFornecedorRepository, UpdateCodCategoria updateCodCategoria) {
        this.iFornecedorRepository = iFornecedorRepository;
        this.updateCodCategoria = updateCodCategoria;
    }

    public void validate(FornecedorDTO fornecedorDTO){

        if(fornecedorDTO == null){
            throw new IllegalArgumentException("FornecedorDTO não pode estar vazio.");
        }

/* Validação Razão social */
        if(StringUtils.isBlank(fornecedorDTO.getRazaoSocial())){
            throw new IllegalArgumentException("Razão Social não pode estar vazia.");
        }

        if(fornecedorDTO.getRazaoSocial().length() > 100){
            throw new IllegalArgumentException("Razão social excedeu o limite de caracteres.");
        }

/*  . . . CNPJ */
        if(StringUtils.isBlank(fornecedorDTO.getCnpj())){
            throw new IllegalArgumentException("CNPJ não pode estar vazio.");
        }

        if(fornecedorDTO.getCnpj().length() != 14){
            throw new IllegalArgumentException("Quantidade inválida de caracteres do CNPJ.");
        }

        if(!StringUtils.containsOnly(fornecedorDTO.getCnpj(), "0123456789")){
            throw new IllegalArgumentException("CNPJ deve conter somente números.");
        }

/*  . . . Nome fantasia */
        if(StringUtils.isBlank(fornecedorDTO.getNomeFantasia())){
            throw new IllegalArgumentException("Nome Fantasia não pode estar vazio.");
        }

        if(fornecedorDTO.getNomeFantasia().length() > 100){
            throw new IllegalArgumentException("Nome Fantasia excedeu o limite de caracteres.");
        }

/*  . . . Endereço */
        if(StringUtils.isBlank(fornecedorDTO.getEndereco())){
            throw new IllegalArgumentException("Endereço não pode estar vazio.");
        }

        if(fornecedorDTO.getEndereco().length() > 100){
            throw new IllegalArgumentException("Endereço excedeu o limite de caracteres.");
        }

/*  . . . Telefone de contato */
        if(StringUtils.isBlank(fornecedorDTO.getTelContato())){
            throw new IllegalArgumentException("Telefone de contato não pode estar vazio.");
        }

        if(fornecedorDTO.getTelContato().length() > 14){
            throw new IllegalArgumentException("Telefone de contato excedeu o limite de caracteres.");
        }

        if(!StringUtils.containsOnly(fornecedorDTO.getTelContato(), "0123456789")){
            throw new IllegalArgumentException("Telefone de contato deve conter somente números.");
        }

/*  . . . Email de contato */
        if(StringUtils.isBlank(fornecedorDTO.getEmailContato())){
            throw new IllegalArgumentException("Email de contato não pode estar vazio.");
        }

        if(fornecedorDTO.getEmailContato().length() > 50){
            throw new IllegalArgumentException("Email de contato excedeu o limite de caracteres.");
        }

        if(!StringUtils.containsAny(fornecedorDTO.getEmailContato(), "@")){
            throw new IllegalArgumentException("Email de contato inválido.");
        }
    }

    public Fornecedor set(Fornecedor fornecedor, FornecedorDTO fornecedorDTO){
        fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
        fornecedor.setCnpj(fornecedorDTO.getCnpj());
        fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
        fornecedor.setEndereco(fornecedorDTO.getEndereco());
        fornecedor.setTelContato(fornecedorDTO.getTelContato());
        fornecedor.setEmailContato(fornecedorDTO.getEmailContato());
        fornecedor = this.iFornecedorRepository.save(fornecedor);

        return fornecedor;
    }

    public FornecedorDTO save(FornecedorDTO fornecedorDTO){
        this.validate(fornecedorDTO);

        if(!this.iFornecedorRepository.findByCnpj(fornecedorDTO.getCnpj()).isPresent()){
            Fornecedor fornecedor = new Fornecedor();

            return FornecedorDTO.of(this.set(fornecedor, fornecedorDTO));
        }

        throw new IllegalArgumentException(String.format("Fornecedor com CNPJ '%s' já existe.", fornecedorDTO.getCnpj()));
    }

    public FornecedorDTO findById(Long id){
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorOptional.isPresent()){
            return FornecedorDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("Fornecedor de ID '%s' não existe.", id));
    }

    public Optional<Fornecedor> findByFornecedorIdOptional(Long id){
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorOptional.isPresent()){
            return fornecedorOptional;
        }

        throw new IllegalArgumentException(String.format("Fornecedor de Id '%s' não existe.", id));
    }

    public Optional<Fornecedor> findByCnpj(String cnpj){

        return this.iFornecedorRepository.findByCnpj(cnpj);
    }

    public FornecedorDTO update(FornecedorDTO fornecedorDTO, Long id){
        this.validate(fornecedorDTO);

        Optional<Fornecedor> cnpjExistente = this.findByCnpj(fornecedorDTO.getCnpj());
        Optional<Fornecedor> fornecedorExistenteOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorExistenteOptional.isPresent()){
            if(!cnpjExistente.isPresent() || fornecedorDTO.getCnpj().equals(fornecedorExistenteOptional.get().getCnpj())){
                Fornecedor fornecedorExistente = this.set(fornecedorExistenteOptional.get(), fornecedorDTO);

                this.updateCodCategoria.updateByFornecedor(fornecedorExistente);

                return FornecedorDTO.of(fornecedorExistente);
            }
            throw new IllegalArgumentException(String.format("Fornecedor com CNPJ '%s' já existe.", fornecedorDTO.getCnpj()));
        }
        throw new IllegalArgumentException(String.format("Fornecedor com Id '%s' não existe.", id));
    }

    public void delete(Long id){
        if(this.iFornecedorRepository.findById(id).isPresent()){

            this.iFornecedorRepository.deleteById(id);
        }
        else{

            throw new IllegalArgumentException(String.format("Fornecedor de Id '%s' não existe.", id));
        }
    }
}
