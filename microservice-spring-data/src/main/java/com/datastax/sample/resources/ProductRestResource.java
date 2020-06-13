package com.datastax.sample.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.sample.entity.Product;
import com.datastax.sample.repository.ProductRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Product API",  
     description = "The Products API Rest Resource")
@RequestMapping("/api/v1/products")
public class ProductRestResource {

    /** Logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(ProductRestResource.class);
    
    /** Service implementation Injection. */
    private ProductRepository  productRepo;
    
    /**
     * Inject with Constructor
     */
    public ProductRestResource(ProductRepository repo) {
        this.productRepo = repo;
    }
    
    // ----- READ (find) --------
    
    /**
     * List all order in DB. Please not there is no implementation of paging. As such result can be
     * really large. If you query tables with large number of rows, please use Paging.
     *  
     * @return
     *      list all {@link Product} available in the table 
     */
    @RequestMapping(
            value = "/",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    @Operation(
            summary = "List all products available in the table", 
            description = "No paging use with caution", 
            tags = { "Products" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", 
                         content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))) })  
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productRepo.findAll());
    }
    
    /**
     * Retrieve single reservation by confirmation number.
     *
     * @param confirmationNumber
     *      unique confirmation number
     * @return
     *      reservation if exists
     */
    @RequestMapping(
            value = "/{productId}",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Access Product information if exists", 
            description = "Retrieve a product based on its identifier", 
            tags = { "Products" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "UUID is blank or contains invalid characters (expecting valid UUID)"),
            @ApiResponse(responseCode = "404", description = "Product not found") })
    public ResponseEntity<Product> findByProductId(
            @Parameter(name="productId", 
                     description="confirmation number for a reservation",
                     example = "smith", required=true )
            @PathVariable(value = "productId") String productId) {
        Assert.hasLength(productId, "Product id is required and should not be null");
        Optional<Product> myProduct = productRepo.findById(UUID.fromString(productId));
        // Routing Result
        if (!myProduct.isPresent()) {
            logger.warn("Reservation with confirmation number {} has not been found", productId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(myProduct.get());
    }
    
    /*
    @Operation(summary = "Update an existing product price", description = "", tags = { "Product" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "Product not found") })
    @RequestMapping(value = "/{productId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> upsertProduct(
            @Parameter(description="Id of the product to be update. Cannot be empty.", required=true)
            @PathVariable long contactId,
            @Parameter(description="Contact's address to update.",
                    required=true, [email protected](implementation = Address.class))
            @RequestBody Address address) {
        return 
    }
    
    @Operation(summary = "Update an existing product price", description = "", tags = { "Product" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "Product not found") })
    @RequestMapping(value = "/{productId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateProductPrice(
            @Parameter(description="Id of the product to be update. Cannot be empty.", required=true)
            @PathVariable long contactId,
            @Parameter(description="Contact's address to update.",
                    required=true, [email protected](implementation = Address.class))
            @RequestBody Address address) {
    return 
    }
    
    @Operation(summary = "Deletes a Product", description = "", tags = { "Product" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "404", description = "Product not found") })
    @DeleteMapping(path="/{productId}")
    public ResponseEntity<Void> deleteContactById(
            @ParameteOr(description="Id of the contact to be delete. Cannot be empty.",
                    required=true)
            @PathVariable long contactId) {
        ...
    return ...
    }*/
    
}
