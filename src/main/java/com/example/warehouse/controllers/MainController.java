package com.example.warehouse.controllers;

import com.example.warehouse.models.OperationType;
import com.example.warehouse.models.Product;
import com.example.warehouse.models.ProductMovement;
import com.example.warehouse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//import java.util.Date;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String main(Map<String, Object> model) {

        Iterable<Product> products = productRepository.findAll();
        model.put("products", products);

        return "main";
    }

    @PostMapping("add")
    public String productAdd(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "shortname", required = false, defaultValue = "") String shortName,
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

//    @PostMapping
//    public String movementOfGoods(
//            @RequestParam String name,
//            @RequestParam Date date,
//            @RequestParam int count) {
//
//        if (0 == count)
//            return "redirect:/";
//
//        ProductMovement productMovement = new ProductMovement(
//                productRepository.findByName(name),
//                count > 0 ? OperationType.DELIVERY_OF_GOODS : OperationType.WRITE_OF_GOODS,
//                date,
//                Math.abs(count));
//        return "redirect:/";
//    }

//    @GetMapping("/{id}")
//    public String productInfo(
//            @PathVariable(value = "id") long id,
//            Map<String, Object> model) {
//        Optional<Product> product = productRepository.findById(id);
//
//        ArrayList<Product> products = new ArrayList<>();
//        product.ifPresent(products::add);
//
//        model.put("product", products);
//        return "product-info";
//    }

//    @GetMapping("/{id}/update")
//    public String productUpdate(
//            @PathVariable(value = "id") long id,
//            Map<String, Object> model) {
//        Optional<Product> product = productRepository.findById(id);
//
//        ArrayList<Product> products = new ArrayList<>();
//        product.ifPresent(products::add);
//
//        model.put("product", products);
//        return "product-update";
//    }

//    @PostMapping("/{id}/update")
//    public String productUpdateForm(
//            @PathVariable(value = "id") long id,
//            @RequestParam String name,
//            @RequestParam String shortName,
//            @RequestParam String characteristics) throws ClassNotFoundException {
//
//        Product product = productRepository.findById(id).orElseThrow(
//                ClassNotFoundException::new);
//
//        product.setName(name);
//        product.setShortName(shortName);
//        product.setCharacteristics(characteristics);
//        productRepository.save(product);
//
//        return "redirect:/product-info" + id;
//    }

//    @PostMapping("/{id}/delete")
//    public String productUpdateForm(@PathVariable(value = "id") long id) throws ClassNotFoundException {
//
//        Product product = productRepository.findById(id).orElseThrow(
//                ClassNotFoundException::new);
//
//        productRepository.delete(product);
//
//        return "redirect:/products" + id;
//    }
}
