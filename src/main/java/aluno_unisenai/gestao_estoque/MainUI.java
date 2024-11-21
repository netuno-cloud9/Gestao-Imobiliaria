package aluno_unisenai.gestao_estoque;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {

    public MainUI() {
        setTitle("Gerenciamento de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        adicionarMenuSuperior();
        adicionarPaineis();
    }

    private void adicionarMenuSuperior() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = criarMenuItem("Sair", e -> {
            int escolha = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menuArquivo.add(itemSair);
        menuBar.add(menuArquivo);

        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = criarMenuItem("Sobre", e -> exibirSobre());
        menuAjuda.add(itemSobre);
        menuBar.add(menuAjuda);

        setJMenuBar(menuBar);
    }

    private JMenuItem criarMenuItem(String texto, java.awt.event.ActionListener acao) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(acao);
        return item;
    }

    private void exibirSobre() {
        JOptionPane.showMessageDialog(this,
                "Sistema de Gerenciamento de Estoque\nVersão 1.0\nDesenvolvido por Unisenai",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void adicionarPaineis() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Adicionar Produto", criarPainelAdicionar());
        tabbedPane.addTab("Listar Produtos", criarPainelListar());
        tabbedPane.addTab("Atualizar Produto", criarPainelAtualizar());
        tabbedPane.addTab("Excluir Produto", criarPainelExcluir());
        tabbedPane.addTab("Backup", criarPainelBackup());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel criarPainelAdicionar() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField(20);

        JLabel lblQuantidade = new JLabel("Quantidade:");
        JTextField txtQuantidade = new JTextField(10);

        JLabel lblPreco = new JLabel("Preço:");
        JTextField txtPreco = new JTextField(10);

        JButton btnAdicionar = criarBotao("Adicionar", e -> {
            String nome = txtNome.getText();
            String quantidadeStr = txtQuantidade.getText();
            String precoStr = txtPreco.getText();

            if (nome.isBlank() || quantidadeStr.isBlank() || precoStr.isBlank()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int quantidade = Integer.parseInt(quantidadeStr);
                double preco = Double.parseDouble(precoStr);

                if (quantidade < 0 || preco < 0) {
                    throw new NumberFormatException();
                }

                JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                txtNome.setText("");
                txtQuantidade.setText("");
                txtPreco.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser números válidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; painel.add(lblNome, gbc);
        gbc.gridx = 1; painel.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1; painel.add(lblQuantidade, gbc);
        gbc.gridx = 1; painel.add(txtQuantidade, gbc);
        gbc.gridx = 0; gbc.gridy = 2; painel.add(lblPreco, gbc);
        gbc.gridx = 1; painel.add(txtPreco, gbc);
        gbc.gridx = 1; gbc.gridy = 3; painel.add(btnAdicionar, gbc);

        return painel;
    }

    private JPanel criarPainelListar() {
        JPanel painel = new JPanel(new BorderLayout());

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);

        JButton btnListar = criarBotao("Atualizar Lista", e -> {
            // Exemplo estático
            areaTexto.setText("Produto 1 - Quantidade: 10\nProduto 2 - Quantidade: 5\n");
        });

        painel.add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        painel.add(btnListar, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelAtualizar() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField(10);

        JLabel lblNovaQuantidade = new JLabel("Nova Quantidade:");
        JTextField txtNovaQuantidade = new JTextField(10);

        JButton btnAtualizar = criarBotao("Atualizar", e -> {
            // Exemplo de mensagem estática
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });

        gbc.gridx = 0; gbc.gridy = 0; painel.add(lblId, gbc);
        gbc.gridx = 1; painel.add(txtId, gbc);
        gbc.gridx = 0; gbc.gridy = 1; painel.add(lblNovaQuantidade, gbc);
        gbc.gridx = 1; painel.add(txtNovaQuantidade, gbc);
        gbc.gridx = 1; gbc.gridy = 2; painel.add(btnAtualizar, gbc);

        return painel;
    }

    private JPanel criarPainelExcluir() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField(10);

        JButton btnExcluir = criarBotao("Excluir", e -> {
            JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });

        gbc.gridx = 0; gbc.gridy = 0; painel.add(lblId, gbc);
        gbc.gridx = 1; painel.add(txtId, gbc);
        gbc.gridx = 1; gbc.gridy = 1; painel.add(btnExcluir, gbc);

        return painel;
    }

    private JPanel criarPainelBackup() {
        JPanel painel = new JPanel();

        JButton btnBackup = criarBotao("Realizar Backup", e -> {
            JOptionPane.showMessageDialog(this, "Backup realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });

        painel.add(btnBackup);

        return painel;
    }

    private JButton criarBotao(String texto, java.awt.event.ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(new Color(70, 130, 180));
        botao.setForeground(Color.WHITE);
        botao.addActionListener(acao);
        return botao;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
