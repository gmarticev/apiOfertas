package com.mycompany.apiofertas.repository.search;

import com.mycompany.apiofertas.domain.Oferta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Oferta} entity.
 */
public interface OfertaSearchRepository extends ElasticsearchRepository<Oferta, Long> {
}
