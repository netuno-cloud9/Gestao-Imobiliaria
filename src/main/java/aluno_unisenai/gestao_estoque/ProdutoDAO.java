package aluno_unisenai.gestao_estoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final String url = "jdbc:mysql://localhost:3306/GerenciamentoEstoque";
    private final String user = "root"; // Substitua pelo seu usuário
    private final String password = "password"; // Substitua pela sua senha

    // Conexão com o banco de dados
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Adicionar produto no banco
    public void adicionarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produtos (nome, quantidade) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.executeUpdate();
        }
    }

    // Listar todos os produtos
    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produtos";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produtos.add(new Produto(rs.getInt("id"), rs.getString("nome"), rs.getInt("quantidade")));
            }
        }
        return produtos;
    }

    // Atualizar quantidade de um produto
    public void atualizarProduto(int id, int quantidade) throws SQLException {
        String sql = "UPDATE Produtos SET quantidade = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    // Excluir produto
    public void excluirProduto(int id) throws SQLException {
        String sql = "DELETE FROM Produtos WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
     // Método main para testar a conexão
    public static void main(String[] args) {
        ProdutoDAO dao = new ProdutoDAO(); // Cria uma instância do ProdutoDAO

        try {
            // Adiciona um novo produto no banco
            dao.adicionarProduto(new Produto("Arroz", 50));
            System.out.println("Produto adicionado com sucesso!");

            // Lista todos os produtos cadastrados no banco
            List<Produto> produtos = dao.listarTodos();
            System.out.println("Produtos cadastrados no banco:");
            for (Produto p : produtos) {
                System.out.println(p.getId() + " - " + p.getNome() + " - " + p.getQuantidade());
            }
        } catch (SQLException e) {
            // Exibe erros no console, caso existam
            
        }
    }
}
