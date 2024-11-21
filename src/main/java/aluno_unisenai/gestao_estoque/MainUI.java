package aluno_unisenai.gestao_estoque;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.nio.file.Files; // Importação adicionada

public class MainUI extends JFrame {

    private final ProdutoDAO produtoDAO;

    public MainUI() {
        produtoDAO = new ProdutoDAO();

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
        JMenuItem itemExportar = criarMenuItem("Exportar para CSV", e -> exportarParaCSV());
        JMenuItem itemSair = criarMenuItem("Sair", e -> {
            int escolha = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menuArquivo.add(itemExportar);
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

    private JPanel criarPainelAtualizar() {
    // Painel principal com layout moderno
    JPanel painel = new JPanel(new GridBagLayout());
    painel.setBackground(new Color(240, 240, 240)); // Fundo claro

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Formatter para o formato esperado
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ID
    JLabel lblId = new JLabel("ID do Produto:");
    lblId.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtId = new JTextField(10);
    txtId.setFont(new Font("Arial", Font.PLAIN, 14));

    // Nome
    JLabel lblNome = new JLabel("Nome:");
    lblNome.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtNome = new JTextField(20);
    txtNome.setFont(new Font("Arial", Font.PLAIN, 14));

    // Quantidade
    JLabel lblQuantidade = new JLabel("Quantidade:");
    lblQuantidade.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtQuantidade = new JTextField(10);
    txtQuantidade.setFont(new Font("Arial", Font.PLAIN, 14));

    // Preço
    JLabel lblPreco = new JLabel("Preço:");
    lblPreco.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtPreco = new JTextField(10);
    txtPreco.setFont(new Font("Arial", Font.PLAIN, 14));

    // Categoria
    JLabel lblCategoria = new JLabel("Categoria:");
    lblCategoria.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtCategoria = new JTextField(15);
    txtCategoria.setFont(new Font("Arial", Font.PLAIN, 14));

    // Data de Validade
    JLabel lblDataValidade = new JLabel("Data de Validade (dd/MM/yyyy):");
    lblDataValidade.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtDataValidade = new JTextField(10);
    txtDataValidade.setFont(new Font("Arial", Font.PLAIN, 14));

    // Descrição
    JLabel lblDescricao = new JLabel("Descrição:");
    lblDescricao.setFont(new Font("Arial", Font.BOLD, 14));
    JTextArea txtDescricao = new JTextArea(3, 20);
    txtDescricao.setLineWrap(true);
    txtDescricao.setWrapStyleWord(true);
    txtDescricao.setFont(new Font("Arial", Font.PLAIN, 14));
    JScrollPane descricaoScroll = new JScrollPane(txtDescricao);

    // Botão Carregar Dados
    JButton btnCarregar = criarBotao("Carregar Dados", e -> {
        try {
            int id = Integer.parseInt(txtId.getText());
            Produto produto = produtoDAO.obterPorId(id);
            if (produto != null) {
                txtNome.setText(produto.getNome());
                txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
                txtPreco.setText(String.valueOf(produto.getPreco()));
                txtCategoria.setText(produto.getCategoria());
                txtDataValidade.setText(produto.getDataValidade() != null ? produto.getDataValidade().format(formatter) : "");
                txtDescricao.setText(produto.getDescricao());
                JOptionPane.showMessageDialog(this, "Dados carregados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    });

    // Botão Atualizar
    JButton btnAtualizar = criarBotao("Atualizar", e -> {
    try {
        // Verificação e conversão do ID
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo ID é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(idStr);

        Produto produtoExistente = produtoDAO.obterPorId(id);

        if (produtoExistente != null) {
            // Captura dos valores dos campos
            String nome = txtNome.getText().trim();
            String quantidadeStr = txtQuantidade.getText().trim();
            String precoStr = txtPreco.getText().trim();
            String categoria = txtCategoria.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String dataValidadeStr = txtDataValidade.getText().trim();

            // Validação do campo Nome
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação do campo Quantidade
            if (quantidadeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Quantidade é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantidade;
            try {
                quantidade = Integer.parseInt(quantidadeStr);
                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantidade não pode ser menor que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser um número inteiro válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação do campo Preço
            if (precoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Preço é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double preco;
            try {
                preco = Double.parseDouble(precoStr);
                if (preco < 0) {
                    JOptionPane.showMessageDialog(this, "Preço não pode ser negativo!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação da Data de Validade
            if (!dataValidadeStr.isEmpty()) {
                try {
                    LocalDate dataValidade = LocalDate.parse(dataValidadeStr, formatter);
                    if (dataValidade.isBefore(LocalDate.now())) {
                        JOptionPane.showMessageDialog(this, "A data de validade não pode ser no passado!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    produtoExistente.setDataValidade(dataValidade);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "A data deve estar no formato dd/MM/yyyy!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Atualização dos campos no objeto produto
            produtoExistente.setNome(nome);
            produtoExistente.setQuantidade(quantidade);
            produtoExistente.setPreco(preco);
            if (!categoria.isEmpty()) produtoExistente.setCategoria(categoria);
            if (!descricao.isEmpty()) produtoExistente.setDescricao(descricao);

            // Atualização no banco de dados
            produtoDAO.atualizar(produtoExistente);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Limpar campos após a atualização
            txtId.setText("");
            txtNome.setText("");
            txtQuantidade.setText("");
            txtPreco.setText("");
            txtCategoria.setText("");
            txtDataValidade.setText("");
            txtDescricao.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "ID deve ser um número inteiro válido!", "Erro", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
});


    // Adicionando componentes ao painel
    gbc.gridx = 0; gbc.gridy = 0; painel.add(lblId, gbc);
    gbc.gridx = 1; painel.add(txtId, gbc);
    gbc.gridx = 2; painel.add(btnCarregar, gbc);

    gbc.gridx = 0; gbc.gridy = 1; painel.add(lblNome, gbc);
    gbc.gridx = 1; painel.add(txtNome, gbc);

    gbc.gridx = 0; gbc.gridy = 2; painel.add(lblQuantidade, gbc);
    gbc.gridx = 1; painel.add(txtQuantidade, gbc);

    gbc.gridx = 0; gbc.gridy = 3; painel.add(lblPreco, gbc);
    gbc.gridx = 1; painel.add(txtPreco, gbc);

    gbc.gridx = 0; gbc.gridy = 4; painel.add(lblCategoria, gbc);
    gbc.gridx = 1; painel.add(txtCategoria, gbc);

    gbc.gridx = 0; gbc.gridy = 5; painel.add(lblDataValidade, gbc);
    gbc.gridx = 1; painel.add(txtDataValidade, gbc);

    gbc.gridx = 0; gbc.gridy = 6; painel.add(lblDescricao, gbc);
    gbc.gridx = 1; painel.add(descricaoScroll, gbc);

    gbc.gridx = 1; gbc.gridy = 7; painel.add(btnAtualizar, gbc);

    return painel;
};


    private JPanel criarPainelListar() {
    JPanel painel = new JPanel(new BorderLayout());

    JTextArea areaTexto = new JTextArea();
    areaTexto.setEditable(false);

    JCheckBox chkBaixoEstoque = new JCheckBox("Mostrar apenas produtos com menos de 10 itens em estoque");

    JButton btnListar = criarBotao("Atualizar Lista", e -> {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            StringBuilder sb = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Produto p : produtos) {
                // Aplica o filtro se o checkbox estiver marcado
                if (!chkBaixoEstoque.isSelected() || p.getQuantidade() < 10) {
                    sb.append("ID: ").append(p.getId())
                      .append(" | Nome: ").append(p.getNome())
                      .append(" | Quantidade: ").append(p.getQuantidade())
                      .append("\n");
                }
            }
            areaTexto.setText(sb.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar produtos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    });

    JPanel painelSuperior = new JPanel(new BorderLayout());
    painelSuperior.add(chkBaixoEstoque, BorderLayout.NORTH);
    painelSuperior.add(new JScrollPane(areaTexto), BorderLayout.CENTER);

    painel.add(painelSuperior, BorderLayout.CENTER);
    painel.add(btnListar, BorderLayout.SOUTH);

    return painel;
}

    private JPanel criarPainelAdicionar() {
    // Painel principal com layout moderno
    JPanel painel = new JPanel(new GridBagLayout());
    painel.setBackground(new Color(240, 240, 240)); // Fundo claro

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Formatter para o formato esperado
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Nome
    JLabel lblNome = new JLabel("Nome:");
    lblNome.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtNome = new JTextField(20);
    txtNome.setFont(new Font("Arial", Font.PLAIN, 14));

    // Quantidade
    JLabel lblQuantidade = new JLabel("Quantidade:");
    lblQuantidade.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtQuantidade = new JTextField(10);
    txtQuantidade.setFont(new Font("Arial", Font.PLAIN, 14));

    // Preço
    JLabel lblPreco = new JLabel("Preço:");
    lblPreco.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtPreco = new JTextField(10);
    txtPreco.setFont(new Font("Arial", Font.PLAIN, 14));

    // Categoria
    JLabel lblCategoria = new JLabel("Categoria:");
    lblCategoria.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtCategoria = new JTextField(15);
    txtCategoria.setFont(new Font("Arial", Font.PLAIN, 14));

    // Data de Validade
    JLabel lblDataValidade = new JLabel("Data de Validade (dd/MM/yyyy):");
    lblDataValidade.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField txtDataValidade = new JTextField(10);
    txtDataValidade.setFont(new Font("Arial", Font.PLAIN, 14));

    // Descrição
    JLabel lblDescricao = new JLabel("Descrição:");
    lblDescricao.setFont(new Font("Arial", Font.BOLD, 14));
    JTextArea txtDescricao = new JTextArea(3, 20);
    txtDescricao.setLineWrap(true);
    txtDescricao.setWrapStyleWord(true);
    txtDescricao.setFont(new Font("Arial", Font.PLAIN, 14));
    JScrollPane descricaoScroll = new JScrollPane(txtDescricao);

    // Botão Adicionar com ícone
    JButton btnAdicionar = criarBotao("Adicionar", e -> {
        String nome = txtNome.getText();
        String quantidadeStr = txtQuantidade.getText();
        String precoStr = txtPreco.getText();
        String categoria = txtCategoria.getText();
        String dataValidadeStr = txtDataValidade.getText();
        String descricao = txtDescricao.getText();

        if (nome.isBlank() || quantidadeStr.isBlank() || precoStr.isBlank() || categoria.isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            double preco = Double.parseDouble(precoStr);
            LocalDate dataValidade = dataValidadeStr.isBlank() ? null : LocalDate.parse(dataValidadeStr, formatter);

            if (quantidade <= 0 || preco <= 0) {
                throw new NumberFormatException();
            }

            Produto produto = new Produto(0, nome, quantidade, preco, categoria, dataValidade, descricao);
            produtoDAO.inserir(produto);

            JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Limpa os campos
            txtNome.setText("");
            txtQuantidade.setText("");
            txtPreco.setText("");
            txtCategoria.setText("");
            txtDataValidade.setText("");
            txtDescricao.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser números válidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "A data deve estar no formato dd/MM/yyyy!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    });

    btnAdicionar.setIcon(new ImageIcon("resources/add_icon.png")); // Adicione um ícone aqui
    btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));

    // Adicionando componentes ao painel
    gbc.gridx = 0; gbc.gridy = 0; painel.add(lblNome, gbc);
    gbc.gridx = 1; painel.add(txtNome, gbc);

    gbc.gridx = 0; gbc.gridy = 1; painel.add(lblQuantidade, gbc);
    gbc.gridx = 1; painel.add(txtQuantidade, gbc);

    gbc.gridx = 0; gbc.gridy = 2; painel.add(lblPreco, gbc);
    gbc.gridx = 1; painel.add(txtPreco, gbc);

    gbc.gridx = 0; gbc.gridy = 3; painel.add(lblCategoria, gbc);
    gbc.gridx = 1; painel.add(txtCategoria, gbc);

    gbc.gridx = 0; gbc.gridy = 4; painel.add(lblDataValidade, gbc);
    gbc.gridx = 1; painel.add(txtDataValidade, gbc);

    gbc.gridx = 0; gbc.gridy = 5; painel.add(lblDescricao, gbc);
    gbc.gridx = 1; painel.add(descricaoScroll, gbc);

    gbc.gridx = 1; gbc.gridy = 6; painel.add(btnAdicionar, gbc);

    return painel;
}


   private JPanel criarPainelExcluir() {
    // Painel principal
    JPanel painel = new JPanel(new GridBagLayout());
    painel.setBackground(new Color(245, 245, 245)); // Fundo claro

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Título
    JLabel titulo = new JLabel("Excluir Produto");
    titulo.setFont(new Font("Arial", Font.BOLD, 18));
    titulo.setForeground(new Color(50, 50, 50));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    painel.add(titulo, gbc);

    // Subtítulo
    JLabel subtitulo = new JLabel("Insira o ID do produto que deseja excluir.");
    subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
    subtitulo.setForeground(new Color(100, 100, 100));
    gbc.gridy = 1;
    painel.add(subtitulo, gbc);

    gbc.gridwidth = 1; // Resetando gridwidth

    // Campo para ID
    JLabel lblId = new JLabel("ID do Produto:");
    lblId.setFont(new Font("Arial", Font.BOLD, 14));
    lblId.setForeground(new Color(50, 50, 50));
    gbc.gridx = 0;
    gbc.gridy = 2;
    painel.add(lblId, gbc);

    JTextField txtId = new JTextField(10);
    txtId.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridx = 1;
    painel.add(txtId, gbc);

    // Botão Excluir com confirmação
    JButton btnExcluir = criarBotao("Excluir Produto", e -> {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um ID válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza de que deseja excluir o produto com ID " + idStr + "?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                produtoDAO.excluir(id);
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                txtId.setText(""); // Limpa o campo de texto
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    btnExcluir.setIcon(new ImageIcon("resources/delete_icon.png")); // Adicione um ícone relevante
    btnExcluir.setBackground(new Color(220, 20, 60)); // Cor vermelha para ação crítica
    btnExcluir.setForeground(Color.WHITE);
    btnExcluir.setFont(new Font("Arial", Font.BOLD, 14));

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    painel.add(btnExcluir, gbc);

    // Mensagem adicional (opcional)
    JLabel mensagemSeguranca = new JLabel("<html><i>Exclusões são permanentes e não podem ser desfeitas.</i></html>");
    mensagemSeguranca.setFont(new Font("Arial", Font.PLAIN, 12));
    mensagemSeguranca.setForeground(new Color(150, 50, 50));
    gbc.gridy = 4;
    painel.add(mensagemSeguranca, gbc);

    return painel;
}


    private JPanel criarPainelBackup() {
    // Painel principal
    JPanel painel = new JPanel(new GridBagLayout());
    painel.setBackground(new Color(245, 245, 245)); // Fundo claro

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Título
    JLabel titulo = new JLabel("Gerenciamento de Backups");
    titulo.setFont(new Font("Arial", Font.BOLD, 18));
    titulo.setForeground(new Color(50, 50, 50));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    painel.add(titulo, gbc);

    // Subtítulo
    JLabel subtitulo = new JLabel("<html>Salve um backup do estoque ou importe um arquivo existente.<br>Tenha cuidado, a importação substituirá os dados atuais.</html>");
    subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
    subtitulo.setForeground(new Color(100, 100, 100));
    gbc.gridy = 1;
    painel.add(subtitulo, gbc);

    gbc.gridwidth = 1; // Resetando gridwidth

    // Botão Realizar Backup
    JButton btnBackup = criarBotaoComEstilo("Realizar Backup", "resources/backup_icon.png", e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Backup");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try {
                produtoDAO.realizarBackupParaCSV(fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Backup realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao realizar backup: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    // Botão Importar Backup
    JButton btnImportarBackup = criarBotaoComEstilo("Importar Backup", "resources/import_icon.png", e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar Backup para Importar");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToImport = fileChooser.getSelectedFile();
            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza de que deseja importar este backup? Isso substituirá os dados atuais.",
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    produtoDAO.importarBackupDeCSV(fileToImport.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Backup importado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao importar backup: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    });

    // Adicionando os botões ao painel
    gbc.gridx = 0;
    gbc.gridy = 2;
    painel.add(btnBackup, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    painel.add(btnImportarBackup, gbc);

    // Mensagem adicional de segurança
    JLabel mensagemSeguranca = new JLabel("<html><i>Recomenda-se criar um backup antes de importar dados.</i></html>");
    mensagemSeguranca.setFont(new Font("Arial", Font.PLAIN, 12));
    mensagemSeguranca.setForeground(new Color(150, 50, 50));
    gbc.gridy = 4;
    painel.add(mensagemSeguranca, gbc);

    return painel;
}
private JButton criarBotaoComEstilo(String texto, String iconeCaminho, ActionListener acao) {
    JButton botao = new JButton(texto);
    botao.setFont(new Font("Arial", Font.BOLD, 14));
    botao.setBackground(new Color(70, 130, 180)); // Azul moderno
    botao.setForeground(Color.WHITE);
    botao.setIcon(new ImageIcon(iconeCaminho)); // Ícone correspondente
    botao.setFocusPainted(false);
    botao.setBorderPainted(false);
    botao.setPreferredSize(new Dimension(200, 40));
    botao.addActionListener(acao);
    return botao;
}


    private JButton criarBotao(String texto, java.awt.event.ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(new Color(70, 130, 180));
        botao.setForeground(Color.WHITE);
        botao.addActionListener(acao);
        return botao;
    }

    private void exportarParaCSV() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            StringBuilder sb = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            sb.append("ID;Nome;Quantidade;Preço;Categoria;DataValidade\n");
            for (Produto p : produtos) {
                sb.append(p.getId()).append(";")
                  .append(p.getNome()).append(";")
                  .append(p.getQuantidade()).append(";")
                  .append(p.getPreco()).append(";")
                  .append(p.getCategoria()).append(";")
                  .append(p.getDataValidade() != null ? p.getDataValidade().format(formatter) : "")
                  .append("\n");
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar arquivo CSV");
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                Files.write(fileToSave.toPath(), sb.toString().getBytes());
                JOptionPane.showMessageDialog(this, "Exportação realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (HeadlessException | IOException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
