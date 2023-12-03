package com.organicshop.web;

import com.organicshop.service.CartService;
import com.organicshop.service.OrderService;
import com.organicshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import static com.organicshop.constants.ControllerAttributesConstants.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;

    public CartController(CartService cartService,
                          ProductService productService,
                          OrderService orderService) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping
    public String getCart(Model model,
                          Principal principal) {

        model.addAttribute(CART_PRODUCTS, this.orderService.getProductsInTheCart(principal.getName()));
        model.addAttribute(PRODUCTS_PRICE, this.orderService.getProductsPrice(principal.getName()));
        model.addAttribute(COUNT_PRODUCTS, this.orderService.getProductsInTheCart(principal.getName()).size());

        return "order-cart";
    }

    @PatchMapping("/add/{id}")
    public String addProductToTheCart(@PathVariable("id") Long id,
                                      Principal principal) {

        this.cartService.addProductToTheCart(id, principal.getName());

        return "redirect:/categories/" + this.productService.getCategoryName(id);
    }

    @PatchMapping("/remove/{id}")
    public String removeProductFromTheCart(@PathVariable("id") Long id,
                                           Principal principal) {

        this.cartService.removeProductFromTheCart(id, principal.getName());

        return "redirect:/cart";
    }

}
