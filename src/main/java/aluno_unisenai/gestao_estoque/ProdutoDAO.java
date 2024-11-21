package aluno_unisenai.gestao_estoque;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // Configurações do banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/GerenciamentoEstoque";
    private static final String USER = "root"; // Substitua pelo seu usuário
    private static final String PASSWORD = "password"; // Substitua pela sua senha

    // Obter conexão com o banco de dados
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Adicionar produto no banco
    public void adicionarProduto(Produto produto) throws SQLException {
        if (produto == null || produto.getNome() == null || produto.getNome().isBlank() || produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Dados do produto inválidos.");
        }
        String sql = "INSERT INTO Produtos (nome, quantidade) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.executeUpdate();
        }
    }

    // Listar todos os produtos
    public List<Produto> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Produtos";
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(new Produto(rs.getString("nome"), rs.getInt("id")));
            }
        }
        return produtos;
    }

    // Listar produtos com estoque baixo
    public List<Produto> listarComEstoqueBaixo() throws SQLException {
        String sql = "SELECT * FROM Produtos WHERE quantidade < 10";
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(new Produto(rs.getString("nome"), rs.getInt("id")));
            }
        }
        return produtos;
    }

    // Atualizar quantidade de um produto
    public void atualizarProduto(int id, int quantidade) throws SQLException {
        if (id <= 0 || quantidade < 0) {
            throw new IllegalArgumentException("ID ou quantidade inválidos.");
        }
        String sql = "UPDATE Produtos SET quantidade = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    // Excluir produto
    public void excluirProduto(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        String sql = "DELETE FROM Produtos WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Salvar produtos em arquivo
    public void salvarProdutosEmArquivo(String caminho) throws SQLException, IOException {
        if (caminho == null || caminho.isBlank()) {
            throw new IllegalArgumentException("Caminho para o arquivo é obrigatório.");
        }
        List<Produto> produtos = listarTodos();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (Produto produto : produtos) {
                writer.write(produto.getId() + "," + produto.getNome() + "," + produto.getQuantidade());
                writer.newLine();
            }
        }
    }

    // Método main para testes (apenas em ambiente de desenvolvimento)
    public static void main(String[] args) {
        ProdutoDAO dao = new ProdutoDAO();
        try {
            // Teste: adicionar produto
            Produto novoProduto = new Produto("Feijão", 20);
            dao.adicionarProduto(novoProduto);
            System.out.println("Produto adicionado com sucesso!");

            // Teste: listar todos os produtos
            List<Produto> produtos = dao.listarTodos();
            System.out.println("Produtos cadastrados no banco:");
            for (Produto p : produtos) {
                System.out.println(p.getId() + " - " + p.getNome() + " - " + p.getQuantidade());
            }
        } catch (SQLException | IllegalArgumentException  e) {
            // Em produção, substitua por um Logger apropriado
            
        }
    }
}
