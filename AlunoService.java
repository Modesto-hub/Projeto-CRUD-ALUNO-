import java.util.ArrayList;
import java.util.List;

/**
 * Camada de serviço: concentra as regras de negócio e o CRUD em memória.
 * O ID é gerado automaticamente (auto-incremento iniciado em 1).
 */
public class AlunoService {

    private static final int IDADE_MINIMA_EXCLUSIVA = 0;  // idade deve ser > 0
    private static final int IDADE_MAXIMA = 120;          // idade deve ser <= 120

    private final ArrayList<Aluno> alunos;
    private int proximoId;

    /**
     * Inicializa a lista de alunos e o contador de IDs.
     */
    public AlunoService() {
        this.alunos = new ArrayList<>();
        this.proximoId = 1;
    }

    // ------------------------------------------------------------------
    // Validações (RN04 a RN07). Lançam IllegalArgumentException com a
    // mensagem de erro a ser exibida ao usuário.
    // ------------------------------------------------------------------

    /** RN04: o nome não pode ser vazio ou conter apenas espaços. */
    public void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser vazio");
        }
    }

    /** RN05: o curso não pode ser vazio ou conter apenas espaços. */
    public void validarCurso(String curso) {
        if (curso == null || curso.trim().isEmpty()) {
            throw new IllegalArgumentException("O curso não pode ser vazio");
        }
    }

    /** RN06/RN07: a idade deve ser maior que 0 e menor ou igual a 120. */
    public void validarIdade(int idade) {
        if (idade <= IDADE_MINIMA_EXCLUSIVA || idade > IDADE_MAXIMA) {
            throw new IllegalArgumentException(
                "A idade deve ser maior que 0 e menor ou igual a " + IDADE_MAXIMA);
        }
    }

    // ------------------------------------------------------------------
    // Operações de CRUD
    // ------------------------------------------------------------------

    /**
     * Cadastra um novo aluno com ID gerado automaticamente.
     *
     * @throws IllegalArgumentException se algum dado for inválido
     */
    public Aluno cadastrarAluno(String nome, int idade, String curso) {
        validarNome(nome);
        validarIdade(idade);
        validarCurso(curso);

        Aluno aluno = new Aluno(proximoId, nome.trim(), idade, curso.trim());
        proximoId++;
        alunos.add(aluno);
        return aluno;
    }

    /**
     * Retorna todos os alunos cadastrados (cópia da lista).
     */
    public List<Aluno> listarAlunos() {
        return new ArrayList<>(alunos);
    }

    /**
     * Busca um aluno pelo ID.
     *
     * @return o aluno encontrado ou null se não existir
     */
    public Aluno buscarAlunoPorId(int id) {
        for (Aluno aluno : alunos) {
            if (aluno.getId() == id) {
                return aluno;
            }
        }
        return null;
    }

    /**
     * Atualiza nome, idade e curso de um aluno existente (RN08).
     *
     * @return true se o aluno foi atualizado; false se o ID não existe
     * @throws IllegalArgumentException se algum dado novo for inválido
     */
    public boolean atualizarAluno(int id, String novoNome, int novaIdade, String novoCurso) {
        Aluno aluno = buscarAlunoPorId(id);
        if (aluno == null) {
            return false;
        }

        validarNome(novoNome);
        validarIdade(novaIdade);
        validarCurso(novoCurso);

        aluno.setNome(novoNome.trim());
        aluno.setIdade(novaIdade);
        aluno.setCurso(novoCurso.trim());
        return true;
    }

    /**
     * Remove um aluno da lista (RN09).
     *
     * @return true se o aluno foi removido; false se o ID não existe
     */
    public boolean excluirAluno(int id) {
        Aluno aluno = buscarAlunoPorId(id);
        if (aluno == null) {
            return false;
        }
        return alunos.remove(aluno);
    }
}
