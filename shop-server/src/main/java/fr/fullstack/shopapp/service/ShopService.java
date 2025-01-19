package fr.fullstack.shopapp.service;

import fr.fullstack.shopapp.dto.ShopDTO;
import fr.fullstack.shopapp.model.Product;
import fr.fullstack.shopapp.model.Shop;
import fr.fullstack.shopapp.repository.elasticsearch.ShopElasticRepository;
import fr.fullstack.shopapp.repository.jpa.ShopRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopElasticRepository shopElasticRepository;

    @Transactional
    public Shop createShop(Shop shop) throws Exception {
        try {
            Shop newShop = shopRepository.save(shop);
            // Refresh the entity after the save. Otherwise, @Formula does not work.
            em.flush();
            em.refresh(newShop);
            return newShop;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void deleteShopById(long id) throws Exception {
        try {
            Shop shop = getShop(id);
            // delete nested relations with products
            deleteNestedRelations(shop);
            shopRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Shop getShopById(long id) throws Exception {
        try {
            return getShop(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Page<Shop> getShopList(
            Optional<String> sortBy,
            Optional<Boolean> inVacations,
            Optional<String> createdBefore,
            Optional<String> createdAfter,
            Pageable pageable
    ) {
        Page<Shop> shopList = getShopListWithFilter(inVacations, createdBefore, createdAfter, pageable);
        if (sortBy.isPresent() && shopList != null) {
            return switch (sortBy.get()) {
                case "name" -> new PageImpl<>(
                        shopList.getContent().stream()
                                .sorted(Comparator.comparing(Shop::getName))
                                .collect(Collectors.toList()), pageable, shopList.getTotalElements());
                case "createdAt" -> new PageImpl<>(
                        shopList.getContent().stream()
                                .sorted(Comparator.comparing(Shop::getCreatedAt))
                                .collect(Collectors.toList()), pageable, shopList.getTotalElements());
                default -> new PageImpl<>(
                        shopList.getContent().stream()
                                .sorted(Comparator.comparing(Shop::getNbProducts))
                                .collect(Collectors.toList()), pageable, shopList.getTotalElements());
            };
        }
        // SORT
        if (sortBy.isPresent()) {
            return switch (sortBy.get()) {
                case "name" -> shopRepository.findByOrderByNameAsc(pageable);
                case "createdAt" -> shopRepository.findByOrderByCreatedAtAsc(pageable);
                default -> shopRepository.findByOrderByNbProductsAsc(pageable);
            };
        }

        // FILTERS
        if (shopList != null) {
            return shopList;
        }

        // NONE
        return shopRepository.findByOrderByIdAsc(pageable);
    }

    public List<ShopDTO> fullTextShopSearch(String input, Optional<Boolean> inVacations,
                                            Optional<String> createdAfter, Optional<String> createdBefore) {


        List<Shop> shops = shopElasticRepository.findByNameContaining(input);
        if (inVacations.isPresent()) {
            shops = filterByVacationStatus(shops, inVacations.get());
        }

        if (createdAfter.isPresent()) {
            LocalDate afterDate = LocalDate.parse(createdAfter.get());
            shops = filterByCreationDateAfter(shops, afterDate);
        }

        if (createdBefore.isPresent()) {
            LocalDate beforeDate = LocalDate.parse(createdBefore.get());
            shops = filterByCreationDateBefore(shops, beforeDate);
        }

        return shops.stream().map(this::shopToDTO).toList();
    }

    @Transactional
    public Shop updateShop(Shop shop) throws Exception {
        try {
            getShop(shop.getId());
            return this.createShop(shop);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void deleteNestedRelations(Shop shop) {
        List<Product> products = shop.getProducts();
        for (Product product : products) {
            product.setShop(null);
            em.merge(product);
            em.flush();
        }
    }

    private Shop getShop(Long id) throws Exception {
        Optional<Shop> shop = shopRepository.findById(id);
        if (shop.isEmpty()) {
            throw new Exception("Shop with id " + id + " not found");
        }
        return shop.get();
    }

    private Page<Shop> getShopListWithFilter(
            Optional<Boolean> inVacations,
            Optional<String> createdAfter,
            Optional<String> createdBefore,
            Pageable pageable
    ) {
        if (inVacations.isPresent() && createdBefore.isPresent() && createdAfter.isPresent()) {
            return shopRepository.findByInVacationsAndCreatedAtGreaterThanAndCreatedAtLessThan(
                    inVacations.get(),
                    LocalDate.parse(createdAfter.get()),
                    LocalDate.parse(createdBefore.get()),
                    pageable
            );
        }

        if (inVacations.isPresent() && createdBefore.isPresent()) {
            return shopRepository.findByInVacationsAndCreatedAtLessThan(
                    inVacations.get(), LocalDate.parse(createdBefore.get()), pageable
            );
        }

        if (inVacations.isPresent() && createdAfter.isPresent()) {
            return shopRepository.findByInVacationsAndCreatedAtGreaterThan(
                    inVacations.get(), LocalDate.parse(createdAfter.get()), pageable
            );
        }

        if (inVacations.isPresent()) {
            return shopRepository.findByInVacations(inVacations.get(), pageable);
        }

        if (createdBefore.isPresent() && createdAfter.isPresent()) {
            return shopRepository.findByCreatedAtBetween(
                    LocalDate.parse(createdAfter.get()), LocalDate.parse(createdBefore.get()), pageable
            );
        }

        if (createdBefore.isPresent()) {
            return shopRepository.findByCreatedAtLessThan(
                    LocalDate.parse(createdBefore.get()), pageable
            );
        }

        return createdAfter.map(s -> shopRepository.findByCreatedAtGreaterThan(
                LocalDate.parse(s), pageable
        )).orElse(null);

    }

    private List<Shop> filterByVacationStatus(List<Shop> shops, Boolean inVacation) {
        return shops.stream()
                .filter(shop -> shop.getInVacations() == inVacation)
                .toList();
    }

    private List<Shop> filterByCreationDateAfter(List<Shop> shops, LocalDate afterDate) {
        return shops.stream()
                .filter(shop -> shop.getCreatedAt().isAfter(afterDate))
                .toList();
    }

    private List<Shop> filterByCreationDateBefore(List<Shop> shops, LocalDate beforeDate) {
        return shops.stream()
                .filter(shop -> shop.getCreatedAt().isBefore(beforeDate))
                .toList();
    }

    private ShopDTO shopToDTO(Shop shop) {
        ShopDTO dto = new ShopDTO();
        dto.setId(shop.getId());
        dto.setCreatedAt(shop.getCreatedAt());
        dto.setInVacations(shop.getInVacations());
        dto.setName(shop.getName());
        dto.setNbProducts(shop.getNbProducts());
        dto.setOpeningHours(shop.getOpeningHours());
        dto.setProducts(shop.getProducts());
        dto.setNumberOfCategories(shop.getNbCategory());
        return dto;
    }
}
