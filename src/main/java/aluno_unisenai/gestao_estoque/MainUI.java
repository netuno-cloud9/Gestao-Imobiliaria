package aluno_unisenai.gestao_estoque;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainUI extends JFrame {

    public MainUI() {
        setTitle("Gerenciamento de Estoque");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        JButton btnAdd = new JButton("Adicionar Produto");
        JButton btnListAll = new JButton("Listar Todos os Produtos");

        add(btnAdd);
        add(btnListAll);

        btnAdd.addActionListener(e -> JOptionPane.showMessageDialog(this, "Adicionar produto não implementado."));
        btnListAll.addActionListener(e -> listarProdutos());
    }

    private void listarProdutos() {
        ProdutoDAO dao = new ProdutoDAO();
        try {
            List<Produto> produtos = dao.listarTodos();
            StringBuilder sb = new StringBuilder();
            for (Produto produto : produtos) {
                sb.append(produto.getId()).append(" - ").append(produto.getNome()).append(" - ").append(produto.getQuantidade()).append(" unidades\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Lista de Produtos", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
    }
}
