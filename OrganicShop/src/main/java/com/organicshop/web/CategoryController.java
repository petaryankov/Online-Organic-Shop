package com.organicshop.web;

import com.organicshop.domain.enums.ProductCategoryEnum;
import com.organicshop.exception.WrongCategoryException;
import com.organicshop.service.ProductService;
import com.organicshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

import static com.organicshop.constants.ControllerAttributesConstants.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final ProductService productService;
    private final UserService userService;

    public CategoryController(ProductService productService,
                              UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @ExceptionHandler(WrongCategoryException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView categoryDoesNotExist(WrongCategoryException productNotFoundException) {

        ModelAndView modelAndView = new ModelAndView("category-does-not-exist");

        modelAndView.addObject(CATEGORY, productNotFoundException.getCategory());

        return modelAndView;
    }
    @GetMapping
    public String getProductsCategory(Principal principal,
                                      Model model) {

        if (principal != null) {
            model.addAttribute(COUNT_PRODUCTS,
                    this.userService
                            .getUserByUsername(principal.getName())
                            .getCart().getCountProducts());
        }

        return "products-category";
    }

    @GetMapping("/{category}")
    public String getProductsCategoriesPage(@PathVariable("category")
                                  String category,
                                            Model model,
                                            Principal principal) {

        model.addAttribute(CATEGORY, this.productService.findCategory(category));
        model.addAttribute(PRODUCTS, this.productService.getAllProductsByCategory(ProductCategoryEnum.valueOf(category)));

        if (principal != null) {
            model.addAttribute(COUNT_PRODUCTS,
                    this.userService
                            .getUserByUsername(principal.getName())
                            .getCart().getCountProducts());
        }

        return "categories-page";
    }

}
