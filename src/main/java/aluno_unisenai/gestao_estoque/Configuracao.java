package aluno_unisenai.gestao_estoque;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JOptionPane;

public class Configuracao {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = Configuracao.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Arquivo config.properties não encontrado no classpath!");
            }
            props.load(input);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar configurações: " + e.getMessage(),
                    "Erro de Configuração", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
