package com.example.warehouse.controllers;

import com.example.warehouse.models.Product;
import com.example.warehouse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public String productAdd(@RequestParam String name) {

        Product product = new Product(name);
        productRepository.save(product);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String productInfo(
            @PathVariable(value = "id") long id,
            Map<String, Object> model) {
        Optional<Product> product = productRepository.findById(id);

        ArrayList<Product> products = new ArrayList<>();
        product.ifPresent(products::add);

        model.put("product", products);
        return "product-info";
    }

    @GetMapping("/{id}/update")
    public String productUpdate(
            @PathVariable(value = "id") long id,
            Map<String, Object> model) {
        Optional<Product> product = productRepository.findById(id);

        ArrayList<Product> products = new ArrayList<>();
        product.ifPresent(products::add);

        model.put("product", products);
        return "product-update";
    }

    @PostMapping("/{id}/update")
    public String productUpdateForm(
            @PathVariable(value = "id") long id,
            @RequestParam String name,
            @RequestParam String shortName,
            @RequestParam String characteristics) throws ClassNotFoundException {

        Product product = productRepository.findById(id).orElseThrow(
                ClassNotFoundException::new);

        product.setName(name);
        product.setShortName(shortName);
        product.setCharacteristics(characteristics);
        productRepository.save(product);

        return "redirect:/product-info" + id;
    }

    @PostMapping("/{id}/delete")
    public String productUpdateForm(@PathVariable(value = "id") long id) throws ClassNotFoundException {

        Product product = productRepository.findById(id).orElseThrow(
                ClassNotFoundException::new);

        productRepository.delete(product);

        return "redirect:/products" + id;
    }
}
