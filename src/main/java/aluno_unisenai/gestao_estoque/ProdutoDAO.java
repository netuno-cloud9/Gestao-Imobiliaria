package aluno_unisenai.gestao_estoque;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ProdutoDAO {
    private static final Logger logger = Logger.getLogger(ProdutoDAO.class.getName());

    // Definição das constantes de conexão
    private static final String URL = "jdbc:mysql://localhost:3307/GerenciamentoEstoque";
    private static final String USER = "root"; // Substitua pelo seu usuário do banco de dados
    private static final String PASSWORD = "BASH$634pcpv!!"; // Substitua pela sua senha do banco de dados

    private Connection connect() throws SQLException {
        logger.info("Estabelecendo conexão com o banco de dados...");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produtos (nome, quantidade, preco, categoria, data_validade, descricao) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getCategoria());
            if (produto.getDataValidade() != null) {
                stmt.setDate(5, Date.valueOf(produto.getDataValidade()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setString(6, produto.getDescricao());
            stmt.executeUpdate();
            logger.info("Produto inserido com sucesso.");
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao inserir produto", ex);
            throw ex;
        }
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE Produtos SET nome = ?, quantidade = ?, preco = ?, categoria = ?, data_validade = ?, descricao = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getCategoria());
            if (produto.getDataValidade() != null) {
                stmt.setDate(5, Date.valueOf(produto.getDataValidade()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setString(6, produto.getDescricao());
            stmt.setInt(7, produto.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Produto atualizado com sucesso.");
            } else {
                logger.warning("Nenhum produto foi atualizado.");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao atualizar produto", ex);
            throw ex;
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM Produtos WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Produto excluído com sucesso.");
            } else {
                logger.warning("Nenhum produto foi excluído.");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao excluir produto", ex);
            throw ex;
        }
    }

    public Produto obterPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Produtos WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco"),
                        rs.getString("categoria"),
                        rs.getDate("data_validade") != null ? rs.getDate("data_validade").toLocalDate() : null,
                        rs.getString("descricao")
                    );
                    return produto;
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao obter produto por ID", ex);
            throw ex;
        }
        return null;
    }

    public List<Produto> listarTodos() throws SQLException {
        logger.info("Listando todos os produtos...");
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produtos";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("quantidade"),
                    rs.getDouble("preco"),
                    rs.getString("categoria"),
                    rs.getDate("data_validade") != null ? rs.getDate("data_validade").toLocalDate() : null,
                    rs.getString("descricao")
                ));
            }
            logger.info("Produtos listados com sucesso.");
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao listar produtos", ex);
            throw ex;
        }
        return produtos;
    }

    public List<Produto> listarProdutosComPoucoEstoque() throws SQLException {
        logger.info("Listando produtos com menos de 10 itens em estoque...");
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, nome, quantidade FROM Produtos WHERE quantidade < 10";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("quantidade"),
                    0.0, // Preço não é relevante aqui
                    null, // Categoria não é relevante aqui
                    null, // Data de validade não é relevante aqui
                    null  // Descrição não é relevante aqui
                ));
            }
            logger.info("Produtos com pouco estoque listados com sucesso.");
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao listar produtos com pouco estoque", ex);
            throw ex;
        }
        return produtos;
    }

    public void importarBackupDeCSV(String caminhoArquivo) throws SQLException, IOException {
        String sql = "INSERT INTO Produtos (id, nome, quantidade, preco, categoria, data_validade, descricao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {

            String linha;
            br.readLine(); // Pular cabeçalho do CSV

            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(",");
                stmt.setInt(1, Integer.parseInt(valores[0]));
                stmt.setString(2, valores[1]);
                stmt.setInt(3, Integer.parseInt(valores[2]));
                stmt.setDouble(4, Double.parseDouble(valores[3]));
                stmt.setString(5, valores[4]);
                stmt.setDate(6, valores[5].isEmpty() ? null : Date.valueOf(valores[5]));
                stmt.setString(7, valores.length > 6 ? valores[6] : null); // Descrição

                stmt.executeUpdate();
            }
            logger.log(Level.INFO, "Backup importado com sucesso do arquivo: {0}", caminhoArquivo);
        } catch (SQLException | IOException ex) {
            logger.log(Level.SEVERE, "Erro ao importar backup de CSV", ex);
            throw ex;
        }
    }

    public void realizarBackupParaCSV(String caminhoArquivo) throws SQLException, IOException {
        String sql = "SELECT * FROM Produtos";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();
             FileWriter csvWriter = new FileWriter(caminhoArquivo)) {

            // Cabeçalho do CSV
            csvWriter.append("id,nome,quantidade,preco,categoria,data_validade,descricao\n");

            // Escrevendo dados
            while (rs.next()) {
                csvWriter.append(rs.getInt("id") + ",");
                csvWriter.append(rs.getString("nome") + ",");
                csvWriter.append(rs.getInt("quantidade") + ",");
                csvWriter.append(rs.getDouble("preco") + ",");
                csvWriter.append(rs.getString("categoria") + ",");
                csvWriter.append(rs.getDate("data_validade") != null ? rs.getDate("data_validade").toString() : "").append(",");
                csvWriter.append(rs.getString("descricao") != null ? rs.getString("descricao") : "").append("\n");
            }
            csvWriter.flush();
            logger.info("Backup realizado com sucesso para o arquivo CSV.");
        } catch (SQLException | IOException ex) {
            logger.log(Level.SEVERE, "Erro ao realizar backup para CSV", ex);
            throw ex;
        }
    }

    public void realizarBackup() throws SQLException {
        logger.info("Iniciando backup dos produtos...");
        String backupTableSql = "CREATE TABLE IF NOT EXISTS Produtos_Backup LIKE Produtos";
        String insertBackupSql = "INSERT INTO Produtos_Backup SELECT * FROM Produtos";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(backupTableSql);
            stmt.executeUpdate("TRUNCATE TABLE Produtos_Backup");
            int rowsCopied = stmt.executeUpdate(insertBackupSql);
            logger.log(Level.INFO, "Backup realizado com sucesso. {0} registros copiados.", rowsCopied);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao realizar backup", ex);
            throw ex;
        }
    }
}
