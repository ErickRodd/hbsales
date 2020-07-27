package br.com.hbsis.periodo.vendas;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PeriodoVendasService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PeriodoVendasService.class);
    private final IPeriodoVendasRepository iPeriodoVendasRepository;
    private final FornecedorService fornecedorService;

    public PeriodoVendasService(IPeriodoVendasRepository iPeriodoVendasRepository, FornecedorService fornecedorService) {
        this.iPeriodoVendasRepository = iPeriodoVendasRepository;
        this.fornecedorService = fornecedorService;
    }

    public void validate(PeriodoVendasDTO periodoVendasDTO){
        LOGGER.info("Validando período de vendas.");

        if(periodoVendasDTO == null){
            throw new IllegalArgumentException("PeriodoVendasDTO não pode ser nulo.");
        }

/*   Validação Data de início */
        if(periodoVendasDTO.getDataInicio() == null){
            throw new IllegalArgumentException("Data de início não pode estar vazia.");
        }

        if(periodoVendasDTO.getDataInicio().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Data de início não pode ser anterior ao dia atual.");
        }

        if(this.findByData(periodoVendasDTO.getDataInicio(), periodoVendasDTO)){
            throw new IllegalArgumentException("Data de início não pode estar entre as datas de outro período.");
        }

/*  . . . Data de fim */
        if(periodoVendasDTO.getDataFim() == null){
            throw new IllegalArgumentException("Data de fim não pode estar vazia.");
        }

        if(periodoVendasDTO.getDataFim().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Data de fim não pode ser anterior ao dia atual.");
        }

        if(periodoVendasDTO.getDataFim().isBefore(periodoVendasDTO.getDataInicio())){
            throw new IllegalArgumentException("Data de fim não pode ser anterior a data de início.");
        }

        if(this.findByData(periodoVendasDTO.getDataFim(), periodoVendasDTO)){
            throw new IllegalArgumentException("Data de fim não pode estar entre as datas de outro período.");
        }

/*  . . . Fornecedor */
        if(periodoVendasDTO.getFornecedor() == null){
            throw new IllegalArgumentException("Fornecedor não pode estar vazio.");
        }

/*  . . . Data retirada */
        if(periodoVendasDTO.getDataRetirada() == null){
            throw new IllegalArgumentException("Data de retirada não pode estar vazia.");
        }

        if(periodoVendasDTO.getDataRetirada().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Data de retirada não pode ser anterior ao dia atual.");
        }

        if(periodoVendasDTO.getDataRetirada().isBefore(periodoVendasDTO.getDataFim())){
            throw new IllegalArgumentException("Data de retirada não pode ser anterior a data de fim do período.");
        }

        if(this.findByData(periodoVendasDTO.getDataRetirada(), periodoVendasDTO)){
            throw new IllegalArgumentException("Data de retirada não pode estar entre as datas de outro período.");
        }

/*  . . . Descrição */
        if(StringUtils.isBlank(periodoVendasDTO.getDescricao())){
            throw new IllegalArgumentException("Descrição não pode estar vazia.");
        }

        if(periodoVendasDTO.getDescricao().length() > 50){
            throw new IllegalArgumentException("Descrição atingiu o limite de caracteres.");
        }
    }

    private PeriodoVendas set(PeriodoVendas periodoVendas, PeriodoVendasDTO periodoVendasDTO, Fornecedor fornecedor){
        periodoVendas.setDataInicio(periodoVendasDTO.getDataInicio());
        periodoVendas.setDataFim(periodoVendasDTO.getDataFim());
        periodoVendas.setFornecedor(fornecedor);
        periodoVendas.setDataRetirada(periodoVendasDTO.getDataRetirada());
        periodoVendas.setDescricao(periodoVendasDTO.getDescricao());
        periodoVendas = this.iPeriodoVendasRepository.save(periodoVendas);

        return periodoVendas;
    }

    public PeriodoVendasDTO save(PeriodoVendasDTO periodoVendasDTO){
        this.validate(periodoVendasDTO);

        Optional<Fornecedor> fornecedor = this.fornecedorService.findByFornecedorIdOptional(periodoVendasDTO.getFornecedor());

        if(fornecedor.isPresent()){
            PeriodoVendas periodo = new PeriodoVendas();

            return PeriodoVendasDTO.of(this.set(periodo, periodoVendasDTO, fornecedor.get()));
        }

        throw new IllegalArgumentException(String.format("Fornecedor de Id '%s' não existe.", fornecedor.get().getId()));
    }

    public PeriodoVendasDTO findById(Long id){
        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findById(id);

        if(periodoVendasOptional.isPresent()){
            return PeriodoVendasDTO.of(periodoVendasOptional.get());
        }

        throw new IllegalArgumentException(String.format("Período de vendas de Id '%s' não existe.", id));
    }

    public boolean findByData(LocalDate data, PeriodoVendasDTO periodoVendasDTO){
        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findByDataFim(data);
        Optional<Fornecedor> fornecedorOptional = this.fornecedorService.findByFornecedorIdOptional(periodoVendasDTO.getFornecedor());

        if(periodoVendasOptional.isPresent() && fornecedorOptional.isPresent()){
            return true;
        }
        else{
            return false;
        }
    }

    public PeriodoVendasDTO update(PeriodoVendasDTO periodoVendasDTO, Long id){
        this.validate(periodoVendasDTO);

        Optional<PeriodoVendas> periodoVendasExistente = this.iPeriodoVendasRepository.findById(id);
        Optional<Fornecedor> fornecedorExistente = this.fornecedorService.findByFornecedorIdOptional(periodoVendasDTO.getFornecedor());

        if(periodoVendasExistente.isPresent()){
            if(fornecedorExistente.isPresent()){
                if(!periodoVendasDTO.getDataFim().isAfter(LocalDate.now())){
                    PeriodoVendas periodoVendas = this.set(periodoVendasExistente.get(), periodoVendasDTO, fornecedorExistente.get());

                    return PeriodoVendasDTO.of(periodoVendas);
                }
                throw new IllegalArgumentException("Não é possível alterar um período de vendas após o término de vigência.");
            }
            throw new IllegalArgumentException("Fornecedor informado não existente.");
        }
        throw new IllegalArgumentException("Período de vendas não existente.");
    }

    public void delete(Long id){
        if(this.iPeriodoVendasRepository.findById(id).isPresent()){

            this.iPeriodoVendasRepository.deleteById(id);
        }
        else{

            throw new IllegalArgumentException(String.format("Período de vendas de Id '%s' não existe.", id));
        }
    }
}
