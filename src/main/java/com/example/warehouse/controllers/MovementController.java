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

import java.util.Map;
import java.util.Optional;

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
                int totalAmount = product.getTotalAmount();
                product.setTotalAmount(totalAmount + Integer.parseInt(changeInQuantity));
                productRepository.save(product);

                ProductMovement productMovement = new ProductMovement(
                        product,
                        OperationType.RECEIPT_OF_GOODS.toString(),
                        date,
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
                int totalAmount = product.getTotalAmount();
                product.setTotalAmount(totalAmount - Integer.parseInt(changeInQuantity));
                productRepository.save(product);

                ProductMovement productMovement = new ProductMovement(
                        product,
                        OperationType.DEPARTURE_OF_GOODS.toString(),
                        date,
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

    @PostMapping("delMovement")
    public String movementDel(
            @RequestParam(name = "idMovement") String idMovement,
            Map<String, Object> model) {

        Iterable<ProductMovement> movements;

        try {
            if (movementRepository.findById(Long.parseLong(idMovement)).isPresent()) {

                ProductMovement productMovement = movementRepository.findById(Long.parseLong(idMovement)).get();
                Product product = productMovement.getProduct();
                int totalAmount = product.getTotalAmount();
                int changeInQuantity = productMovement.getChangeInQuantity();
                String operationType = productMovement.getOperationType();

                if (operationType.equals(OperationType.RECEIPT_OF_GOODS.toString()))
                    product.setTotalAmount(totalAmount - changeInQuantity);
                else
                    product.setTotalAmount(totalAmount + changeInQuantity);

                movementRepository.delete(productMovement);
                productRepository.save(product);
            }
        } catch (NumberFormatException ignore) { }

        movements = movementRepository.findAll();
        model.put("movements", movements);

        return "movement";
    }

    @PostMapping("changeMovement")
    public String changeMove(
            @RequestParam(name = "idMovement") String idMovement,
            @RequestParam(name = "idProduct") String idProduct,
            @RequestParam(name = "operationType") String operationType,
            @RequestParam(name = "date") String date,
            @RequestParam(name = "changeInQuantity") String changeInQuantity,
            Map<String, Object> model) {

        Iterable<ProductMovement> movements;

        try {
            if (movementRepository.findById(Long.parseLong(idMovement)).isPresent() &&
            productRepository.findById(Long.parseLong(idProduct)).isPresent()) {

                ProductMovement productMovement = movementRepository.findById(Long.parseLong(idMovement)).get();
                String operationTypeOld = productMovement.getOperationType();
                Product productOld = productMovement.getProduct();
                Product productNew = productRepository.findById(Long.parseLong(idProduct)).get();
                int changeInQuantityOld = productMovement.getChangeInQuantity();
                int totalAmountOld = productOld.getTotalAmount();

                // если меняется продукт, убрать изменения по остаткам старого продукта
                if (productOld != productNew) {

                    int totalAmountNew = productNew.getTotalAmount();

                    if (operationTypeOld.equals(OperationType.RECEIPT_OF_GOODS.toString())) {
                        productNew.setTotalAmount(totalAmountNew + Integer.parseInt(changeInQuantity));
                        productOld.setTotalAmount(totalAmountOld - changeInQuantityOld);
                    } else {
                        productNew.setTotalAmount(totalAmountNew - Integer.parseInt(changeInQuantity));
                        productOld.setTotalAmount(totalAmountOld + changeInQuantityOld);
                    }

                    productMovement.setProduct(productNew);
                    productRepository.save(productOld);
                    productRepository.save(productNew);
                } else {

                    if (operationType.equals(OperationType.RECEIPT_OF_GOODS.toString()))
                        productOld.setTotalAmount(totalAmountOld + Integer.parseInt(changeInQuantity));
                    else
                        productOld.setTotalAmount(totalAmountOld - Integer.parseInt(changeInQuantity));

                    productRepository.save(productOld);
                }

                productMovement.setOperationType(OperationType.valueOf(operationType).toString());
                productMovement.setDate(date);
                productMovement.setChangeInQuantity(Integer.parseInt(changeInQuantity));
                movementRepository.save(productMovement);
                movements = movementRepository.findAllByProduct(productOld);
            } else
                movements = movementRepository.findAll();
        } catch (NumberFormatException ignore) {
            movements = movementRepository.findAll();
        }

        model.put("movements", movements);

        return "movement";
    }

    @PostMapping("filterMovement")
    public String filter(@RequestParam(name = "filterMovement", required = false) String filter, Map<String, Object> model) {

        Iterable<ProductMovement> movements;

        if (filter != null && !filter.isEmpty() && productRepository.findById(Long.parseLong(filter)).isPresent())
            movements = movementRepository.findAllByProduct(productRepository.findById(Long.parseLong(filter)).get());
        else
            movements = movementRepository.findAll();

        model.put("movements", movements);

        return "movement";
    }
}
