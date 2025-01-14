package aluno_unisenai.gestao_estoque;

import java.time.LocalDate;

public class Produto {
    private int id; // Identificador único do produto
    private String nome; // Nome do produto
    private int quantidade; // Quantidade disponível em estoque
    private double preco; // Preço unitário do produto
    private String categoria; // Categoria do produto (ex: alimentos, bebidas, etc.)
    private LocalDate dataValidade; // Data de validade do produto
    private String descricao; // Descrição detalhada do produto

    // Construtores

    /**
     * Construtor completo com todos os campos.
     */
    public Produto(int id, String nome, int quantidade, double preco, String categoria, LocalDate dataValidade, String descricao) {
        this(nome, quantidade, preco, categoria, dataValidade, descricao);
        this.id = id;
    }

    /**
     * Construtor sem ID (para produtos ainda não persistidos no banco de dados).
     */
    public Produto(String nome, int quantidade, double preco, String categoria, LocalDate dataValidade, String descricao) {
        if (quantidade < 0) throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        if (preco < 0) throw new IllegalArgumentException("Preço não pode ser negativo.");
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome não pode ser vazio.");
        if (categoria == null || categoria.isBlank()) throw new IllegalArgumentException("Categoria não pode ser vazia.");

        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.categoria = categoria;
        this.dataValidade = dataValidade;
        this.descricao = descricao != null ? descricao : "";
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome não pode ser vazio.");
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) throw new IllegalArgumentException("Preço não pode ser negativo.");
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) throw new IllegalArgumentException("Categoria não pode ser vazia.");
        this.categoria = categoria;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        if (dataValidade != null && dataValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de validade não pode ser no passado.");
        }
        this.dataValidade = dataValidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null) {
            this.descricao = "";
        } else {
            this.descricao = descricao;
        }
    }

    // Métodos Auxiliares

    /**
     * Verifica se o produto está válido (baseado na data de validade).
     * @return 
     */
    public boolean isValido() {
        return dataValidade == null || !dataValidade.isBefore(LocalDate.now());
    }

    /**
     * Calcula o valor total do estoque baseado na quantidade e preço.
     * @return 
     */
    public double calcularValorTotal() {
        return quantidade * preco;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                ", categoria='" + categoria + '\'' +
                ", dataValidade=" + dataValidade +
                ", descricao='" + descricao + '\'' +
                ", valido=" + isValido() +
                '}';
    }
}
