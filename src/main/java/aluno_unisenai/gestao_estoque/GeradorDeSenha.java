package aluno_unisenai.gestao_estoque;

import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;

public class GeradorDeSenha {
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";

    public static String gerarSenha(int tamanho) {
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            senha.append(CARACTERES.charAt(random.nextInt(CARACTERES.length())));
        }
        return senha.toString();
    }

    public static String hashSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static void main(String[] args) {
        String senha = gerarSenha(10); // Tamanho da senha
        String hash = hashSenha(senha);

        System.out.println("Senha gerada: " + senha);
        System.out.println("Hash armazenado: " + hash);
    }
}
