package com.example.warehouse.controllers;

import com.example.warehouse.models.Product;
import com.example.warehouse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("main")
    public String main(Map<String, Object> model) {

        Iterable<Product> products = productRepository.findAll();
        model.put("products", products);

        return "main";
    }

    @PostMapping("add")
    public String productAdd(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "shortName", required = false, defaultValue = "") String shortName,
            @RequestParam(name = "characteristics", required = false, defaultValue = "") String characteristics,
            Map<String, Object> model) {

        if ( !name.isEmpty() && null == productRepository.findByName(name) ) {
            Product product = new Product(name, shortName, characteristics);
            productRepository.save(product);
        }

        Iterable<Product> products = productRepository.findAll();
        model.put("products", products);

        return "main";
    }

    @PostMapping("change")
    public String productChange(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "shortName", required = false, defaultValue = "") String shortName,
            @RequestParam(name = "characteristics", required = false, defaultValue = "") String characteristics,
            Map<String, Object> model) {

        try {
            if ( productRepository.findById(Long.parseLong(id)).isPresent() ) {
                Product product = productRepository.findById(Long.parseLong(id)).get();

                if (null != name && null == productRepository.findByName(name))
                    product.setName(name);

                if (null != shortName)
                    product.setShortName(shortName);

                if (null != characteristics)
                    product.setCharacteristics(characteristics);

                productRepository.save(product);
            }
        } catch (Exception ignore) {}

        Iterable<Product> products = productRepository.findAll();
        model.put("products", products);

        return "main";
    }

    @PostMapping("del")
    public String productDel(
            @RequestParam(name = "id") String id,
            Map<String, Object> model) {

        try {
            if ( !id.isEmpty() && productRepository.findById(Long.parseLong(id)).isPresent()) {
                Product product = productRepository.findById(Long.parseLong(id)).get();
                productRepository.delete(product);
            }
        } catch (NumberFormatException ignored) {}

        Iterable<Product> products = productRepository.findAll();
        model.put("products", products);

        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {

        Iterable<Product> products;

        if (filter != null && !filter.isEmpty()) {
            products = productRepository.findByShortName(filter);
        } else {
            products = productRepository.findAll();
        }
        model.put("products", products);

        return "main";
    }
}
