package com.mycompany.apiofertas.service;

import com.mycompany.apiofertas.domain.Oferta;
import com.mycompany.apiofertas.repository.OfertaRepository;
import com.mycompany.apiofertas.repository.search.OfertaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Oferta}.
 */
@Service
@Transactional
public class OfertaService {

    private final Logger log = LoggerFactory.getLogger(OfertaService.class);

    private final OfertaRepository ofertaRepository;

    private final OfertaSearchRepository ofertaSearchRepository;

    public OfertaService(OfertaRepository ofertaRepository, OfertaSearchRepository ofertaSearchRepository) {
        this.ofertaRepository = ofertaRepository;
        this.ofertaSearchRepository = ofertaSearchRepository;
    }

    /**
     * Save a oferta.
     *
     * @param oferta the entity to save.
     * @return the persisted entity.
     */
    public Oferta save(Oferta oferta) {
        log.debug("Request to save Oferta : {}", oferta);
        Oferta result = ofertaRepository.save(oferta);
        ofertaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ofertas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Oferta> findAll(Pageable pageable) {
        log.debug("Request to get all Ofertas");
        return ofertaRepository.findAll(pageable);
    }


    /**
     * Get one oferta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Oferta> findOne(Long id) {
        log.debug("Request to get Oferta : {}", id);
        return ofertaRepository.findById(id);
    }

    /**
     * Delete the oferta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Oferta : {}", id);
        ofertaRepository.deleteById(id);
        ofertaSearchRepository.deleteById(id);
    }

    /**
     * Search for the oferta corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Oferta> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ofertas for query {}", query);
        return ofertaSearchRepository.search(queryStringQuery(query), pageable);    }
}
