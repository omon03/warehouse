package com.example.warehouse.controllers;

import com.example.warehouse.models.OperationType;
import com.example.warehouse.models.Product;
import com.example.warehouse.models.ProductMovement;
import com.example.warehouse.repository.MovementRepository;
import com.example.warehouse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Map;

@Controller
public class MovementController {

    @Autowired
    private MovementRepository movementRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("movement")
    public String movement(Map<String, Object> model) {
        Iterable<ProductMovement> movements = movementRepository.findAll();
        model.put("movements", movements);

        return "movement";
    }

    @PostMapping("receipt")
    public String receiptAdd(
            @RequestParam(name = "idProduct") String idProduct,
            @RequestParam(name = "date") String date,
            @RequestParam(name = "changeInQuantity") String changeInQuantity,
            Map<String, Object> model) {

        Iterable<ProductMovement> movements;

        try {
            if (!idProduct.isEmpty() && productRepository.findById(Long.parseLong(idProduct)).isPresent()) {

                Product product = productRepository.findById(Long.parseLong(idProduct)).get();
                ProductMovement productMovement = new ProductMovement(
                        product,
                        OperationType.RECEIPT_OF_GOODS,
                        Date.valueOf(date),
                        Integer.parseInt(changeInQuantity));
                movementRepository.save(productMovement);

                movements = movementRepository.findAllByProduct(product);
            } else
                movements = movementRepository.findAll();
        } catch (NumberFormatException ignore) {
            movements = movementRepository.findAll();
        }

        model.put("movements", movements);
        return "movement";
    }

    @PostMapping("departure")
    public String departureAdd(
            @RequestParam(name = "idProduct") String idProduct,
            @RequestParam(name = "date") String date,
            @RequestParam(name = "changeInQuantity") String changeInQuantity,
            Map<String, Object> model) {

        Iterable<ProductMovement> movements;

        try {
            if (productRepository.findById(Long.parseLong(idProduct)).isPresent()) {

                Product product = productRepository.findById(Long.parseLong(idProduct)).get();
                ProductMovement productMovement = new ProductMovement(
                        product,
                        OperationType.DEPARTURE_OF_GOODS,
                        Date.valueOf(date),
                        Integer.parseInt(changeInQuantity));
                movementRepository.save(productMovement);

                movements = movementRepository.findAllByProduct(product);
            } else
                movements = movementRepository.findAll();
        } catch (NumberFormatException ignore) {
            movements = movementRepository.findAll();
        }

        model.put("movements", movements);

        return "movement";
    }

    @PostMapping("changemove")
    public String changeMove(
            @RequestParam(name = "idmove") String idMove,
            @RequestParam(name = "idProduct") String idProduct,
            @RequestParam(name = "date") String date,
            @RequestParam(name = "changeInQuantity") String changeInQuantity,
            Map<String, Object> model) {

        Iterable<ProductMovement> movements;

        try {
            if (movementRepository.findById(Long.parseLong(idMove)).isPresent() &&
            productRepository.findById(Long.parseLong(idProduct)).isPresent()) {

                ProductMovement productMovementOld = movementRepository.findById(Long.parseLong(idMove)).get();
                OperationType operationTypeOld = productMovementOld.getOperationType();
                Product product = productMovementOld.getProduct();
                int changeInQuantityOld = productMovementOld.getChangeInQuantity();
                long idProductOld = movementRepository.findById(Long.parseLong(idMove)).get().getProduct().getId();

                // если меняется продукт, убрать изменения по остаткам старого продукта
                if (idProductOld != Long.parseLong(idProduct)) {

                    ProductMovement productMovement;

                    if (operationTypeOld.equals(OperationType.RECEIPT_OF_GOODS))
                        productMovement = new ProductMovement(
                                product,
                                OperationType.DEPARTURE_OF_GOODS,
                                Date.valueOf(date),
                                changeInQuantityOld);
                    else
                        productMovement = new ProductMovement(
                                product,
                                operationTypeOld,
                                Date.valueOf(date),
                                changeInQuantityOld);

                    movementRepository.save(productMovement);
                }
                else {  // TODO

                    productMovementOld.setDate(Date.valueOf(date));
                }

                movements = movementRepository.findAllByProduct(product);
            } else
                movements = movementRepository.findAll();
        } catch (NumberFormatException ignore) {
            movements = movementRepository.findAll();
        }

        model.put("movements", movements);

        return "movement";
    }
}
