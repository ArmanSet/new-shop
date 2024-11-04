package com.example.demo.controllers.admin;




import com.example.demo.entity.Products;
import com.example.demo.service.ProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
        import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminEditProductControler {

    @Autowired
    private ProductsService productService;

    @GetMapping("/select")
    public String selectProduct(@RequestParam(required = false) Long productId, Model model) {
        List<Products> products = productService.findAll();
        model.addAttribute("products", products);

        if (productId != null) {
            Products selectedProduct = productService.findProductById(productId);
            model.addAttribute("selectedProduct", selectedProduct);
        }
        return "admin/edit-products";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam double price,
                              @RequestParam int maxQuantity,
                              @RequestParam String brand,
                              @RequestParam String description,
                              @RequestParam int active,
                              @RequestParam(required = false) String imageUrl) {
        Products product = productService.findProductById(id);
        product.setName(name);
        product.setPrice(price);
        product.setMaxQuantity(maxQuantity);
        product.setBrand(brand);
        product.setDescription(description);
        product.setActive(active);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            product.setImage(imageUrl);
        }

        productService.save(product);
        return "redirect:/admin/products/select?productId=" + id;
    }
}