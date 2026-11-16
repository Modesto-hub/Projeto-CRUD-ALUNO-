import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Interface de console do Sistema de Cadastro de Alunos.
 * Responsável pelo menu, pela captura segura de dados e pelo feedback visual.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AlunoService service = new AlunoService();

    private static final String MSG_ENTRADA_INVALIDA =
        "❌ Entrada inválida! Digite apenas números inteiros.";
    private static final String MSG_NAO_ENCONTRADO = "Aluno não encontrado.";

    public static void main(String[] args) {
        int opcao = -1;

        try {
            do {
                exibirMenu();
                Integer lida = lerInteiro(null);

                // Entrada inválida (letras etc.): avisa e volta ao menu
                if (lida == null) {
                    continue;
                }
                opcao = lida;

                switch (opcao) {
                    case 1:
                        cadastrar();
                        break;
                    case 2:
                        listar();
                        break;
                    case 3:
                        consultarPorId();
                        break;
                    case 4:
                        atualizar();
                        break;
                    case 5:
                        excluir();
                        break;
                    case 0:
                        System.out.println("\n👋 Encerrando o sistema... Obrigado por utilizar!");
                        break;
                    default:
                        System.out.println("⚠️ Opção inexistente. Escolha uma das opções do menu.");
                }
            } while (opcao != 0);
        } catch (NoSuchElementException e) {
            // Fim da entrada (ex.: Ctrl+D): encerra com elegância
            System.out.println("\n👋 Encerrando o sistema... Obrigado por utilizar!");
        } finally {
            scanner.close();
        }
    }

    // ------------------------------------------------------------------
    // Menu
    // ------------------------------------------------------------------

    private static void exibirMenu() {
        System.out.println();
        System.out.println("====================================");
        System.out.println("       SISTEMA DE CADASTRO");
        System.out.println("             DE ALUNOS");
        System.out.println("====================================");
        System.out.println("1 🟢 Cadastrar aluno");
        System.out.println("2 📋 Listar alunos");
        System.out.println("3 🔍 Consultar aluno por ID");
        System.out.println("4 ✏️  Atualizar aluno");
        System.out.println("5 ❌ Excluir aluno");
        System.out.println("0 🚪 Sair");
        System.out.println("====================================");
        System.out.print("Digite a opção desejada: ");
    }

    // ------------------------------------------------------------------
    // Operações
    // ------------------------------------------------------------------

    private static void cadastrar() {
        System.out.println("\n--- 🟢 Cadastro de aluno ---");
        String nome = lerNome("Nome: ");
        Integer idade = lerIdade("Idade: ");
        String curso = lerCurso("Curso: ");

        Aluno aluno = service.cadastrarAluno(nome, idade, curso);
        System.out.println("✅ Aluno " + aluno.getNome()
            + " cadastrado com sucesso! ID gerado: " + aluno.getId());
    }

    private static void listar() {
        System.out.println("\n--- 📋 Lista de alunos ---");
        List<Aluno> alunos = service.listarAlunos();

        if (alunos.isEmpty()) {
            System.out.println("⚠️ Nenhum aluno cadastrado.");
            return;
        }
        for (Aluno aluno : alunos) {
            System.out.println(aluno);
        }
        System.out.println("Total de alunos: " + alunos.size());
    }

    private static void consultarPorId() {
        System.out.println("\n--- 🔍 Consulta por ID ---");
        Integer id = lerInteiro("Informe o ID do aluno: ");
        if (id == null) {
            return;
        }

        Aluno aluno = service.buscarAlunoPorId(id);
        if (aluno == null) {
            System.out.println(MSG_NAO_ENCONTRADO);
        } else {
            System.out.println(aluno);
        }
    }

    private static void atualizar() {
        System.out.println("\n--- ✏️  Atualização de aluno ---");
        Integer id = lerInteiro("Informe o ID do aluno: ");
        if (id == null) {
            return;
        }

        // RN08: só atualiza se o ID existir
        Aluno existente = service.buscarAlunoPorId(id);
        if (existente == null) {
            System.out.println(MSG_NAO_ENCONTRADO);
            return;
        }

        System.out.println("Dados atuais:");
        System.out.println(existente);
        System.out.println("Informe os novos dados:");

        String novoNome = lerNome("Novo nome: ");
        Integer novaIdade = lerIdade("Nova idade: ");
        String novoCurso = lerCurso("Novo curso: ");

        if (service.atualizarAluno(id, novoNome, novaIdade, novoCurso)) {
            System.out.println("✅ Aluno atualizado com sucesso! ID: " + id);
        } else {
            System.out.println(MSG_NAO_ENCONTRADO);
        }
    }

    private static void excluir() {
        System.out.println("\n--- ❌ Exclusão de aluno ---");
        Integer id = lerInteiro("Informe o ID do aluno: ");
        if (id == null) {
            return;
        }

        // RN09: só exclui se o ID existir
        Aluno aluno = service.buscarAlunoPorId(id);
        if (aluno == null) {
            System.out.println(MSG_NAO_ENCONTRADO);
            return;
        }

        System.out.println(aluno);

        // RN10: confirmação explícita (1 - Sim / 2 - Não)
        Integer confirmacao = null;
        while (confirmacao == null || (confirmacao != 1 && confirmacao != 2)) {
            confirmacao = lerInteiro("Confirmar exclusão? (1 - Sim / 2 - Não): ");
            if (confirmacao != null && confirmacao != 1 && confirmacao != 2) {
                System.out.println("⚠️ Digite 1 para Sim ou 2 para Não.");
            }
        }

        if (confirmacao == 1) {
            service.excluirAluno(id);
            System.out.println("✅ Aluno " + aluno.getNome() + " excluído com sucesso!");
        } else {
            System.out.println("⚠️ Exclusão cancelada.");
        }
    }

    // ------------------------------------------------------------------
    // Captura segura de dados
    // ------------------------------------------------------------------

    /**
     * Lê um número inteiro (RN13). Se o usuário digitar algo que não seja
     * um inteiro, exibe a mensagem de erro, descarta a entrada e retorna null.
     *
     * @param prompt texto exibido antes da leitura (pode ser null)
     */
    private static Integer lerInteiro(String prompt) {
        if (prompt != null) {
            System.out.print(prompt);
        }
        try {
            int valor = scanner.nextInt();
            scanner.nextLine(); // consome o restante da linha (quebra de linha)
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // descarta a entrada inválida
            System.out.println(MSG_ENTRADA_INVALIDA);
            return null;
        }
    }

    /** Lê o nome repetindo até ser válido (RN04). */
    private static String lerNome(String prompt) {
        while (true) {
            System.out.print(prompt);
            String nome = scanner.nextLine();
            try {
                service.validarNome(nome);
                return nome.trim();
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ " + e.getMessage() + ".");
            }
        }
    }

    /** Lê o curso repetindo até ser válido (RN05). */
    private static String lerCurso(String prompt) {
        while (true) {
            System.out.print(prompt);
            String curso = scanner.nextLine();
            try {
                service.validarCurso(curso);
                return curso.trim();
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ " + e.getMessage() + ".");
            }
        }
    }

    /** Lê a idade repetindo até ser um inteiro válido entre 1 e 120 (RN06/RN07). */
    private static int lerIdade(String prompt) {
        while (true) {
            Integer idade = lerInteiro(prompt);
            if (idade == null) {
                continue; // mensagem de entrada inválida já exibida
            }
            try {
                service.validarIdade(idade);
                return idade;
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ " + e.getMessage() + ".");
            }
        }
    }
}
