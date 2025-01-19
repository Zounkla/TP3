package fr.fullstack.shopapp.controller;

import fr.fullstack.shopapp.dto.ShopDTO;
import fr.fullstack.shopapp.model.Shop;
import fr.fullstack.shopapp.service.ShopService;
import fr.fullstack.shopapp.util.ErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/shops")
public class ShopController {

    @Autowired
    private ShopService service;

    @Operation(summary = "Create a shop")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Shop> createShop(@Valid @RequestBody Shop shop, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ErrorValidation.getErrorValidationMessage(errors));
        }

        try {
            return ResponseEntity.ok(service.createShop(shop));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Delete a shop by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Shop deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid shop id")
    })
    @DeleteMapping("/{id}")
    public HttpStatus deleteShop(@PathVariable long id) {
        try {
            service.deleteShopById(id);
            return HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Get shops (sorting and filtering are possible)")
    @Parameters({
            @Parameter(name = "page", schema = @Schema(type = "integer", defaultValue = "0"), description = "Results page you want to retrieve (0..N)"),
            @Parameter(name = "size", schema = @Schema(type = "integer", defaultValue = "5"), description = "Number of records per page"),
            @Parameter(name = "sortBy", schema = @Schema(type = "string"), description = "To sort the shops. Possible values are 'name', 'nbProducts' and 'createdAt'"),
            @Parameter(name = "inVacations", schema = @Schema(type = "boolean"), description = "Define that the shops must be in vacations or not"),
            @Parameter(name = "createdAfter", schema = @Schema(type = "string"), description = "Define that the shops must be created after this date"),
            @Parameter(name = "createdBefore", schema = @Schema(type = "string"), description = "Define that the shops must be created before this date")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shops retrieved successfully"),
    })
    @GetMapping
    public ResponseEntity<Page<Shop>> getAllShops(
            Pageable pageable,
            @RequestParam(required = false) Optional<String> sortBy,
            @RequestParam(required = false) Optional<Boolean> inVacations,
            @RequestParam(required = false) Optional<String> createdAfter,
            @RequestParam(required = false) Optional<String> createdBefore

    ) {
        return ResponseEntity.ok(
                service.getShopList(sortBy, inVacations, createdAfter, createdBefore, pageable)
        );
    }

    @Operation(summary = "Full text search for shops")
    @Parameters({
            @Parameter(name = "name", schema = @Schema(type = "string"), description = "Name of the shop to search for"),
            @Parameter(name = "inVacations", schema = @Schema(type = "boolean"), description = "Define that the shops must be in vacations or not"),
            @Parameter(name = "createdAfter", schema = @Schema(type = "string"), description = "Define that the shops must be created after this date"),
            @Parameter(name = "createdBefore", schema = @Schema(type = "string"), description = "Define that the shops must be created before this date")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shops retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ShopDTO>> fullTextSearchShops(
            @RequestParam String name,
            @RequestParam(required = false) Optional<Boolean> inVacations,
            @RequestParam(required = false) Optional<String> createdAfter,
            @RequestParam(required = false) Optional<String> createdBefore
    ) {
        return ResponseEntity.ok().body(service.fullTextShopSearch(name, inVacations, createdAfter, createdBefore));
    }

    @Operation(summary = "Get a shop by id")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid shop id")
    })
    public ResponseEntity<Shop> getShopById(@PathVariable long id) {
        try {
            return ResponseEntity.ok().body(service.getShopById(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Update a shop")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop modified"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping
    public ResponseEntity<Shop> updateShop(@Valid @RequestBody Shop shop, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ErrorValidation.getErrorValidationMessage(errors));
        }

        try {
            return ResponseEntity.ok().body(service.updateShop(shop));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
