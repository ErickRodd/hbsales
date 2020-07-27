package br.com.hbsis.funcionario;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class FuncionarioService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioService.class);
    private final IFuncionarioRepository iFuncionarioRepository;

    public FuncionarioService(IFuncionarioRepository iFuncionarioRepository) {
        this.iFuncionarioRepository = iFuncionarioRepository;
    }

    public void validate(FuncionarioDTO funcionarioDTO){

        if(funcionarioDTO == null){
            throw new IllegalArgumentException("FuncionarioDTO não pode ser nulo.");
        }

/** Validação Nome funcionário */
        if(StringUtils.isBlank(funcionarioDTO.getNome()) || StringUtils.isEmpty(funcionarioDTO.getNome())){
            throw new IllegalArgumentException("Nome do funcionário não pode estar vazio.");
        }

        if(funcionarioDTO.getNome().length() > 50){
            throw  new IllegalArgumentException("Nome do funcionário excedeu o limite de caracteres.");
        }

/** . . . Email funcionário */
        if(StringUtils.isBlank(funcionarioDTO.getEmail()) || StringUtils.isEmpty(funcionarioDTO.getEmail())){
            throw new IllegalArgumentException("E-mail do funcionário não pode estar vazio.");
        }

        if(funcionarioDTO.getEmail().length() > 50){
            throw  new IllegalArgumentException("E-mail do funcionário excedeu o limite de caracteres.");
        }
    }

    public FuncionarioDTO save(FuncionarioDTO funcionarioDTO){
        this.validate(funcionarioDTO);

        String uuid = this.validarFuncionarioHBEmployee(funcionarioDTO.getNome());

        Funcionario funcionario = new Funcionario();

        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setEmail(funcionarioDTO.getEmail());
        funcionario.setUuid(uuid);

        funcionario = this.iFuncionarioRepository.save(funcionario);
        return FuncionarioDTO.of(funcionario);
    }

    public String validarFuncionarioHBEmployee(String nome){
        String url = "http://10.2.54.25:9999/api/employees";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "f59ff07e-1b67-11ea-978f-2e728ce88125");

        HttpEntity<HBEmployee> request = new HttpEntity<>(new HBEmployee(nome), headers);

        HttpEntity<HBEmployee> response = restTemplate.exchange(url, HttpMethod.POST, request, HBEmployee.class);

        HBEmployee result = response.getBody();

        assert result != null;
        return result.getEmployeeUuid();
    }

    public FuncionarioDTO findById(Long id){
        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if(funcionarioOptional.isPresent()){
            return FuncionarioDTO.of(funcionarioOptional.get());
        }
        else{
            throw new IllegalArgumentException(String.format("Funcionário de Id '%s' não existe.", id));
        }
    }

    public FuncionarioDTO update(FuncionarioDTO funcionarioDTO, Long id){
        this.validate(funcionarioDTO);

        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if(funcionarioOptional.isPresent()){
            Funcionario funcionario = funcionarioOptional.get();

            funcionario.setNome(funcionarioDTO.getNome());
            funcionario.setEmail(funcionarioDTO.getEmail());

            funcionario = this.iFuncionarioRepository.save(funcionario);
            return FuncionarioDTO.of(funcionario);
        }

        throw new IllegalArgumentException(String.format("Funcionário de Id '%s' não existe.", id));
    }

    public void delete(Long id){
        if(this.iFuncionarioRepository.findById(id).isPresent()){

            this.iFuncionarioRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException(String.format("Funcionário de Id '%s' não existe.", id));
        }
    }
}