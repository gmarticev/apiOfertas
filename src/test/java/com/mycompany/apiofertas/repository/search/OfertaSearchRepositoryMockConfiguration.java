package com.mycompany.apiofertas.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link OfertaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OfertaSearchRepositoryMockConfiguration {

    @MockBean
    private OfertaSearchRepository mockOfertaSearchRepository;

}
