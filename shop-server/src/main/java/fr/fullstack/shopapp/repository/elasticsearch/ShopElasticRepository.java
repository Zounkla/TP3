package fr.fullstack.shopapp.repository.elasticsearch;

import fr.fullstack.shopapp.model.Shop;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ShopElasticRepository extends ElasticsearchRepository<Shop, String> {

    List<Shop> findByNameContaining(String keyword);
}
