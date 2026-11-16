/**
 * Classe de modelo (entidade) que representa um aluno.
 * O atributo 'id' é imutável: só é definido no construtor e não possui setter.
 */
public class Aluno {

    private final int id;     // Identificador único e sequencial (imutável)
    private String nome;
    private int idade;
    private String curso;

    /**
     * Cria um novo aluno.
     *
     * @param id    identificador único (gerado pelo AlunoService)
     * @param nome  nome do aluno
     * @param idade idade do aluno
     * @param curso curso do aluno
     */
    public Aluno(int id, String nome, int idade, String curso) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.curso = curso;
    }

    // ----------------------- Getters -----------------------

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getCurso() {
        return curso;
    }

    // ----------------------- Setters -----------------------
    // ATENÇÃO: não existe setId(), pois o ID é imutável após a criação.

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     * Formata a exibição organizada do aluno no console.
     */
    @Override
    public String toString() {
        return "------------------------------------\n"
             + String.format("ID    : %d%n", id)
             + String.format("Nome  : %s%n", nome)
             + String.format("Idade : %d anos%n", idade)
             + String.format("Curso : %s%n", curso)
             + "------------------------------------";
    }
}
