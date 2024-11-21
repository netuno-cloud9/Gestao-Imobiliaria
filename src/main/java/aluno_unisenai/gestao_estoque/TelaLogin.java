package aluno_unisenai.gestao_estoque;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TelaLogin extends JFrame {

    public TelaLogin() {
        setTitle("Login - Gerenciamento de Estoque");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Layout principal como GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Painel Principal
        JPanel painelPrincipal = criarPainelPrincipal();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(painelPrincipal, gbc);
    }

    /**
     * Cria o painel principal da interface.
     *
     * @return JPanel configurado.
     */
    private JPanel criarPainelPrincipal() {
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Bem-vindo!");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painelPrincipal.add(lblTitulo, gbc);

        // Imagem de Logotipo
        JLabel lblImagem = new JLabel(new ImageIcon("resources/logo.png")); // Altere para o caminho correto da imagem
        gbc.gridy = 1;
        painelPrincipal.add(lblImagem, gbc);

        // Campo de Usuário
        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        painelPrincipal.add(lblUsuario, gbc);

        JTextField txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        painelPrincipal.add(txtUsuario, gbc);

        // Campo de Senha
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 3;
        gbc.gridx = 0;
        painelPrincipal.add(lblSenha, gbc);

        JPasswordField txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        painelPrincipal.add(txtSenha, gbc);

        // Botão de Login
        JButton btnLogin = new JButton("Entrar");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painelPrincipal.add(btnLogin, gbc);

        // Ação do botão de login
        btnLogin.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String senha = new String(txtSenha.getPassword());

            if (validarCredenciais(usuario, senha)) {
                JOptionPane.showMessageDialog(this, "Bem-vindo!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.invokeLater(() -> {
                    new MainUI().setVisible(true);
                    dispose(); // Fecha a tela de login
                });
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais inválidas! Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                txtUsuario.setText("");
                txtSenha.setText("");
            }
        });

        return painelPrincipal;
    }

    /**
     * Valida as credenciais fornecidas com base no banco de dados.
     *
     * @param usuario Nome de usuário.
     * @param senha   Senha.
     * @return true se forem válidas, false caso contrário.
     */
    private boolean validarCredenciais(String usuario, String senha) {
        String url = Configuracao.get("db.url");
        String user = Configuracao.get("db.user");
        String password = Configuracao.get("db.password");

        System.out.println("Tentando conectar ao banco de dados:");
        System.out.println("URL: " + url);
        System.out.println("Usuário: " + user);

        String sql = "SELECT senha FROM usuarios WHERE nome_usuario = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String senhaArmazenada = rs.getString("senha");
                System.out.println("Senha armazenada para o usuário: " + senhaArmazenada);
                return senha.equals(senhaArmazenada); // Comparação direta (considere hashing para produção)
            } else {
                System.out.println("Usuário não encontrado: " + usuario);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco remoto: " + ex.getMessage(),
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
