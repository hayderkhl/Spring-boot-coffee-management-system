package com.haidar.coffeemanagementsystem.serviceImpl;

import com.haidar.coffeemanagementsystem.cofeUtils.CafeUtil;
import com.haidar.coffeemanagementsystem.constatnts.CafeConstants;
import com.haidar.coffeemanagementsystem.dao.ProductDao;
import com.haidar.coffeemanagementsystem.jwt.JwtFilter;
import com.haidar.coffeemanagementsystem.models.Category;
import com.haidar.coffeemanagementsystem.models.Product;
import com.haidar.coffeemanagementsystem.service.ProductService;
import com.haidar.coffeemanagementsystem.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;
    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProduct(requestMap, false)) {
                     productDao.save(getproductFromMap(requestMap, false));
                     return CafeUtil.getResponsseEntity("product is added successfly", HttpStatus.OK);
                }
                return CafeUtil.getResponsseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else  {
                return CafeUtil.getResponsseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if(validateProduct(requestMap, true)) {
                   Optional<Product> productOptional = productDao.findById(Integer.parseInt(requestMap.get("id")));

                   if (!productOptional.isEmpty()) {
                       Product product = getproductFromMap(requestMap, true);
                       product.setStatus(productOptional.get().getStatus());
                       productDao.save(product);
                       return CafeUtil.getResponsseEntity("product is updated successfly", HttpStatus.OK);
                   } else  {
                       return CafeUtil.getResponsseEntity("product id does not exist", HttpStatus.OK);
                   }
                } else {}
                return CafeUtil.getResponsseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtil.getResponsseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional optional= productDao.findById(id);
                if (!optional.isEmpty()) {
                    productDao.deleteById(id);
                    return CafeUtil.getResponsseEntity("Product deleted successfly", HttpStatus.OK);
                }
                return CafeUtil.getResponsseEntity("Product id does not exist", HttpStatus.OK);
            } else {
                return CafeUtil.getResponsseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatusProduct(Map<String, String> requestMap) {
       try {
           if (jwtFilter.isAdmin())
           {
               Optional<Product> optionalProduct = productDao.findById(Integer.parseInt(requestMap.get("id")));
               if (!optionalProduct.isEmpty()) {
                    productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return CafeUtil.getResponsseEntity("Status of Product updated succcessfly", HttpStatus.OK);
               }
               return CafeUtil.getResponsseEntity("Product id does not exist", HttpStatus.OK);
           }   else {
               return CafeUtil.getResponsseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
        return CafeUtil.getResponsseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>() , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper() , HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //instead this function we can use the Dto is better and easier to handle
    //the transferring of dat between the application components and layers
    private Product getproductFromMap(Map<String, String> requestMap, boolean idAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();
        if(idAdd) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }

    private boolean validateProduct(Map<String, String> requestMap, boolean validateID) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateID) {
                return true;
            } else if (!validateID) {
                return true;
            }
        }
        return false;
    }
}
