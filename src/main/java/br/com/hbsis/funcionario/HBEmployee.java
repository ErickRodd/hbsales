package br.com.hbsis.funcionario;

public class HBEmployee {
    private String nome;
    private String employeeUuid;

    public HBEmployee() {
    }

    public HBEmployee(String nome) {
        this.nome = nome;
    }

    public HBEmployee(String nome, String employeeUuid) {
        this.nome = nome;
        this.employeeUuid = employeeUuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }
}
