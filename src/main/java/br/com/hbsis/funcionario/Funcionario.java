package br.com.hbsis.funcionario;

import javax.persistence.*;

@Entity
@Table(name = "funcionario")
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_funcionario", length = 50, nullable = false)
    private String nome;

    @Column(name = "email_funcionario", length = 50, nullable = false)
    private String email;

    @Column(name = "uuid", length = 36, nullable = false)
    private String uuid;

    public Funcionario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", nomeFuncionario='" + nome + '\'' +
                ", emailFuncionario='" + email + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
