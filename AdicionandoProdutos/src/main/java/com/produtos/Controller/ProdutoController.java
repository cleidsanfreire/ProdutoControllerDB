package com.produtos.Controller;

import com.produtos.Main.Produto;
import com.produtos.Models.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    private List<Produto> produtos = new ArrayList<>();

    //Méthod GET - Buscar produto por produtos no postman
    @GetMapping("/lista")
    public List<Produto> listarProdutos() {
        List<Produto> produtosBanco = produtoRepository.findAll();
        List<Produto> todosProdutos = new ArrayList<>(produtos);
        todosProdutos.addAll(produtosBanco);
        return todosProdutos;
    }

    // Méthod GET - Buscar produto por ID (Banco de dados)
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Méthod POST - Adicionar produto à lista local postman
    @PostMapping("/adicionar")
    public Produto adicionarProdutoLista(@RequestBody Produto produto) {
        produto.setId(produtos.size() + 1);
        produtos.add(produto);
        return produto;
    }

    // Méthod POST - Criar produto no banco de dados
    @PostMapping("criar-produto/{id}")
    public Produto criarProdutoBanco(@RequestBody Produto produto) {
        produto.setId(produtos.size() + 1);
        return produtoRepository.save(produto);

    }

    // Méthod PUT - Atualizar o produto na lista local postman
    @PutMapping("/atualizar-lista/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                produto.setNome(produtoAtualizado.getNome());
                produto.setPreco(produtoAtualizado.getPreco());
                return ResponseEntity.ok(produto);
            }
        }
                return ResponseEntity.noContent().build();
    }

    // Méthod PUT - Atualizar o produto na lista local postman
    @PutMapping("/atualizar-lista-bd/{id}")
    public ResponseEntity<Produto> atualizarProdutoBandoDB(@PathVariable long id, @RequestBody Produto produtoAtualizado) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isPresent()){
            Produto produto = produtoExistente.get();
            produto.setNome(produtoAtualizado.getNome());
            produto.setPreco(produtoAtualizado.getPreco());

            Produto produtoSalvo = produtoRepository.save(produto);
        return ResponseEntity.ok(produtoSalvo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar-lista/{id}")
    public ResponseEntity<Void> deletarProdutoLista(@PathVariable long id) {
        boolean removido = produtos.removeIf(produto -> produto.getId() == id);
        if (removido) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar-lista-bd/{id}")
    public ResponseEntity<Void> deletarProdutoBanco(@PathVariable long id) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isPresent()) {
            produtoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }

    }
}