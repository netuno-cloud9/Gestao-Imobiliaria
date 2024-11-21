package aluno_unisenai.gestao_estoque;

public class Gestao_Estoque {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                new TelaLogin().setVisible(true);
            } catch (Exception e) {
                System.err.println("Erro ao iniciar o sistema: " + e.getMessage());
            }
        });
    }
}
