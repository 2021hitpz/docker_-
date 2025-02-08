package org.tech.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tech.entity.Product;
import org.tech.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        log.info("获取商品信息: id = {}", id);
        return productRepository.selectById(id);
    }
    
    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAllProducts() {
        log.info("获取所有商品信息");
        try {
            return productRepository.selectList(null);
        } catch (Exception e) {
            log.error("获取所有商品信息失败", e);
            throw e;
        }
    }
    
    public Page<Product> getProductsByPage(Integer current, Integer size) {
        log.info("分页获取商品信息: current = {}, size = {}", current, size);
        return productRepository.selectPage(new Page<>(current, size), null);
    }
    
    @CacheEvict(value = {"product", "products"}, allEntries = true)
    public Product createProduct(Product product) {
        log.info("创建商品: {}", product);
        productRepository.insert(product);
        return product;
    }
    
    @CacheEvict(value = {"product", "products"}, allEntries = true)
    public Product updateProduct(Product product) {
        log.info("更新商品: {}", product);
        productRepository.updateById(product);
        return product;
    }
    
    @CacheEvict(value = {"product", "products"}, allEntries = true)
    public boolean deleteProduct(Long id) {
        log.info("删除商品: id = {}", id);
        return productRepository.deleteById(id) > 0;
    }
    
    public List<Product> searchProducts(String name, Integer minStock) {
        log.info("搜索商品: name = {}, minStock = {}", name, minStock);
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Product::getName, name);
        }
        
        if (minStock != null) {
            queryWrapper.ge(Product::getStock, minStock);
        }
        
        return productRepository.selectList(queryWrapper);
    }
} 