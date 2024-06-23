package com.organicshop.service;

import com.organicshop.domain.dto.binding.AddProductBindingDto;
import com.organicshop.domain.dto.binding.EditProductBindingDto;
import com.organicshop.domain.dto.view.ProductViewDto;
import com.organicshop.domain.entities.ProductEntity;
import com.organicshop.domain.enums.ProductCategoryEnum;
import com.organicshop.exception.NotFoundObjectException;
import com.organicshop.exception.WrongCategoryException;
import com.organicshop.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.organicshop.constants.Messages.PRODUCT;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository,
                          ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProductViewDto> getAllProductsByCategory(ProductCategoryEnum category) {
        return this.productRepository
                .findAllByCategory(category)
                .stream()
                .map(this::mapToViewDto)
                .collect(Collectors.toList());
    }

    private ProductViewDto mapToViewDto(ProductEntity productEntity) {
        return this.modelMapper.map(productEntity, ProductViewDto.class);
    }

    public String getCategoryName(Long id) {
        return this.productRepository
                .findProductEntityById(id)
                .getCategory()
                .name();
    }

    public void saveProduct(AddProductBindingDto productDto) {

        ProductEntity productToSave = createProduct(productDto);

        this.productRepository.saveAndFlush(productToSave);
    }

    private static ProductEntity createProduct(AddProductBindingDto productDto) {

        ProductEntity productToSave = new ProductEntity();

        productToSave
                .setName(productDto.getName())
                .setCategory(productDto.getCategory())
                .setDescription(productDto.getDescription())
                .setPrice(productDto.getPrice());

        return productToSave;
    }

    public ProductViewDto getProductById(Long productId) {

        ProductEntity productEntity = this.productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundObjectException(productId, PRODUCT));

        return this.modelMapper.map(productEntity, ProductViewDto.class);
    }

    public void editProduct(Long productId,
                            EditProductBindingDto editedProductDto) {

        ProductEntity productEntityById = this.productRepository
                .findProductEntityById(productId);

        productEntityById
                .setDescription(editedProductDto.getDescription())
                .setPrice(editedProductDto.getPrice());

        this.productRepository.saveAndFlush(productEntityById);

    }

    public void deleteProduct(Long productId) {
        this.productRepository.deleteById(productId);
    }

    public ProductCategoryEnum findCategory(String category) {

        for (ProductCategoryEnum categoryEnum : ProductCategoryEnum.values()) {
            if (categoryEnum.name().equals(category)) {
                return categoryEnum;
            }
        }

        throw new WrongCategoryException(category);
    }

}
