package com.bymdev.controller;

import com.bymdev.entity.Category;
import com.bymdev.entity.Order;
import com.bymdev.entity.OrderItem;
import com.bymdev.entity.Product;
import com.bymdev.pojo.ProductSearch;
import com.bymdev.service.GenericService;
import com.bymdev.service.ProductSearchService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by oleksii on 14.12.16.
 */
@Api
@RestController
public class MyController {
    @Autowired
    private GenericService<Order> orderService;

    @Autowired
    private GenericService<OrderItem> orderItemService;

    @Autowired
    private GenericService<Product> productService;

    @Autowired
    private GenericService<Category> categoryService;

    @Autowired
    private ProductSearchService productSearchService;

    //-------------------------------------------CATEGORY-------------------------------------------
    //get all categories
    @RequestMapping(value = "/category/", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.readAll();
        if(categories.isEmpty()){
            return new ResponseEntity<List<Category>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }

    //get category
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Category.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable("id") long id){
        Category category = categoryService.read(id);
        if (category == null){
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Category>(category,HttpStatus.OK);
    }

    //create category
    @RequestMapping(value = "/category/", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "products", required = false, dataType = "long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public ResponseEntity<Void> createCategory(@RequestBody Category category, UriComponentsBuilder ucBuilder){
        if (categoryService.isExist(category)){
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }


        categoryService.create(category);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/category/{id}").buildAndExpand(category.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //update category
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "name", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "products", required = false, dataType = "long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Category.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Failure")})
    @RequestMapping(value = "/category/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Category> updateCategory(@PathVariable("id") long id, @RequestBody Category category){
        Category currentCategory = categoryService.read(id);

        if(currentCategory == null)
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);

        //Changing category for a list of products
        if(category.getProducts() != null){

            for (Product p : category.getProducts()) {
                if (!productService.isExist(p))
                {
                    return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
                }
                currentCategory.addProduct(productService.read(p.getId()));
            }
        }

        //Changing the name
        if(category.getName() != null)
            currentCategory.setName(category.getName());

        categoryService.update(currentCategory);
        return new ResponseEntity<Category>(currentCategory, HttpStatus.OK);
    }

    //delete category
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Category> deleteCategory(@PathVariable("id") long id){
        Category category = categoryService.read(id);

        if (category == null){
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        }
        if (!category.getProducts().isEmpty())
        {
            return new ResponseEntity<Category>(HttpStatus.CONFLICT);
        }

        categoryService.delete(category);
        return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
    }

    //-------------------------------------------ORDER-------------------------------------------
    //get all orders
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/order/", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.readAll();
        if(orders.isEmpty()){
            return new ResponseEntity<List<Order>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }

    //get order
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Order.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrder(@PathVariable("id") long id){
        Order order = orderService.read(id);
        if (order == null){
            return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Order>(order,HttpStatus.OK);
    }

    //create order
    @ApiImplicitParams({
            @ApiImplicitParam(name = "total", required = false, dataType = "double", paramType = "query"),
            @ApiImplicitParam(name = "orderDate", required = false, dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "orderItems", required = true, dataType = "long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/order/", method = RequestMethod.POST)
    public ResponseEntity<Void> createOrder(@RequestBody Order order, UriComponentsBuilder ucBuilder){
        if (orderService.isExist(order)){
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        if (order.getOrderItems() == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        //Searching order items
        for (int i=0; i<order.getOrderItems().size(); i++)
        {
            OrderItem orderItem = orderItemService.read(order.getOrderItems().get(i).getId());
            if (orderItem == null){
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }
            order.getOrderItems().set(i, orderItem);
        }

        orderService.create(order);

        //Updating orderitems (setting order id)
        for (int i=0; i<order.getOrderItems().size(); i++)
        {
            order.getOrderItems().get(i).setOrder(order);
            orderItemService.update(order.getOrderItems().get(i));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/order/{id}").buildAndExpand(order.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //update order
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "total", required = false, dataType = "double", paramType = "query"),
            @ApiImplicitParam(name = "orderDate", required = false, dataType = "date", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Order.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/order/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Order> updateOrder(@PathVariable("id") long id, @RequestBody Order order){
        Order currentOrder = orderService.read(id);

        if(currentOrder == null){
            return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
        }

        if (order.getTotal() != null)
            currentOrder.setTotal(order.getTotal());
        if (order.getOrderDate() != null)
            currentOrder.setOrderDate(order.getOrderDate());

        orderService.update(currentOrder);
        return new ResponseEntity<Order>(currentOrder, HttpStatus.OK);
    }

    //delete order
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/order/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Order> deleteOrder(@PathVariable("id") long id){
        Order order = orderService.read(id);

        if (order == null){
            return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
        }

        orderService.delete(order);
        return new ResponseEntity<Order>(HttpStatus.NO_CONTENT);
    }

    //-------------------------------------------ORDER ITEM-------------------------------------------
    //get all order items
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/orderItem/", method = RequestMethod.GET)
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemService.readAll();
        if(orderItems.isEmpty()){
            return new ResponseEntity<List<OrderItem>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<OrderItem>>(orderItems, HttpStatus.OK);
    }

    //get orderItem
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = OrderItem.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/orderItem/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable("id") long id){
        OrderItem orderItem = orderItemService.read(id);
        if (orderItem == null){
            return new ResponseEntity<OrderItem>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<OrderItem>(orderItem,HttpStatus.OK);
    }

    //create orderItem
    @ApiImplicitParams({
            @ApiImplicitParam(name = "quantity", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orderItem", required = true, dataType = "long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/orderItem/", method = RequestMethod.POST)
    public ResponseEntity<Void> createOrderItem(@RequestBody OrderItem orderItem, UriComponentsBuilder ucBuilder){
        if (orderItemService.isExist(orderItem)){
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        //Searching product with ID
        if (orderItem.getProduct() == null || orderItem.getQuantity() == 0)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        Product product = productService.read(orderItem.getProduct().getId());

        if (product == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        orderItem.setProduct(product);
        orderItemService.create(orderItem);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/orderItem/{id}").buildAndExpand(orderItem.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //update orderItem
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "quantity", required = false, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = OrderItem.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/orderItem/{id}", method = RequestMethod.PUT)
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable("id") long id, @RequestBody OrderItem orderItem){
        OrderItem currentOrderItem = orderItemService.read(id);

        if(currentOrderItem == null){
            return new ResponseEntity<OrderItem>(HttpStatus.NOT_FOUND);
        }

        if (orderItem.getQuantity() != 0)
            currentOrderItem.setQuantity(orderItem.getQuantity());

        orderItemService.update(currentOrderItem);
        return new ResponseEntity<OrderItem>(currentOrderItem, HttpStatus.OK);
    }

    //delete orderItem
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/orderItem/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<OrderItem> deleteOrderItem(@PathVariable("id") long id){
        OrderItem orderItem = orderItemService.read(id);

        if (orderItem == null){
            return new ResponseEntity<OrderItem>(HttpStatus.NOT_FOUND);
        }

        if (orderItem.getOrder() != null){
            return new ResponseEntity<OrderItem>(HttpStatus.CONFLICT);
        }

        orderItemService.delete(orderItem);
        return new ResponseEntity<OrderItem>(HttpStatus.NO_CONTENT);
    }

    //-------------------------------------------PRODUCT-------------------------------------------
    //get all products
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/product/", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.readAll();
        if(products.isEmpty()){
            return new ResponseEntity<List<Product>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    //get product
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Product.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProduct(@PathVariable("id") long id){
        Product product = productService.read(id);
        if (product == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(product,HttpStatus.OK);
    }

    //create product
    @ApiImplicitParams({
            @ApiImplicitParam(name = "price", required = false, dataType = "double", paramType = "query"),
            @ApiImplicitParam(name = "sku", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "name", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "category", required = false, dataType = "long", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/product/", method = RequestMethod.POST)
    public ResponseEntity<Void> createProduct(@RequestBody Product product, UriComponentsBuilder ucBuilder){
        if (productService.isExist(product)){
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        //If inserting product with current category ID
        if (product.getCategory() != null)
        {
            if (product.getCategory().getId() != 0) {
                Category category = categoryService.read(product.getCategory().getId());
                if (category == null) {
                    return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
                }
                product.setCategory(category);
            }
        }

        productService.create(product);

        //ElasticSearch
        ProductSearch productSearch = new ProductSearch();
        productSearch.setId(new Long(product.getId()).toString());
        productSearch.setName(product.getName());
        productSearch.setSku(product.getSku());

        productSearchService.save(productSearch);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/product/{id}").buildAndExpand(product.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //update product
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "price", required = false, dataType = "double", paramType = "query"),
            @ApiImplicitParam(name = "sku", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "name", required = false, dataType = "string", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Product.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/product/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody Product product){
        Product currentProduct = productService.read(id);

        if(currentProduct == null)
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);

        //If new value is not null, then update
        if (product.getPrice() != null)
            currentProduct.setPrice(product.getPrice());
        if (product.getSku() != null)
            currentProduct.setSku(product.getSku());
        if (product.getName() != null)
            currentProduct.setName(product.getName());

        if(product.getCategory() != null){
            return new ResponseEntity<Product>(HttpStatus.CONFLICT);
        }

        productService.update(currentProduct);
        return new ResponseEntity<Product>(currentProduct, HttpStatus.OK);
    }

    //delete product
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Failure")
    })
    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id){
        Product product = productService.read(id);

        if (product == null){
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }

        //If violates FK constraint
        try {
            productService.delete(product);
        } catch(Exception e)
        {
            return new ResponseEntity<Product>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }
}
