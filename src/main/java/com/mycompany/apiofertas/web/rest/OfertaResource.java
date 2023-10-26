package com.mycompany.apiofertas.web.rest;

import com.mycompany.apiofertas.domain.Oferta;
import com.mycompany.apiofertas.service.OfertaService;
import com.mycompany.apiofertas.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.apiofertas.domain.Oferta}.
 */
@RestController
@RequestMapping("/api")
public class OfertaResource {

    private final Logger log = LoggerFactory.getLogger(OfertaResource.class);

    private static final String ENTITY_NAME = "oferta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfertaService ofertaService;

    public OfertaResource(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    /**
     * {@code POST  /ofertas} : Create a new oferta.
     *
     * @param oferta the oferta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oferta, or with status {@code 400 (Bad Request)} if the oferta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ofertas")
    public ResponseEntity<Oferta> createOferta(@Valid @RequestBody Oferta oferta) throws URISyntaxException {
        log.debug("REST request to save Oferta : {}", oferta);
        if (oferta.getId() != null) {
            throw new BadRequestAlertException("A new oferta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Oferta result = ofertaService.save(oferta);
        return ResponseEntity.created(new URI("/api/ofertas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ofertas} : Updates an existing oferta.
     *
     * @param oferta the oferta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oferta,
     * or with status {@code 400 (Bad Request)} if the oferta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oferta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ofertas")
    public ResponseEntity<Oferta> updateOferta(@Valid @RequestBody Oferta oferta) throws URISyntaxException {
        log.debug("REST request to update Oferta : {}", oferta);
        if (oferta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Oferta result = ofertaService.save(oferta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oferta.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ofertas} : get all the ofertas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ofertas in body.
     */
    @GetMapping("/ofertas")
    public ResponseEntity<List<Oferta>> getAllOfertas(Pageable pageable) {
        log.debug("REST request to get a page of Ofertas");
        Page<Oferta> page = ofertaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ofertas/:id} : get the "id" oferta.
     *
     * @param id the id of the oferta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oferta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ofertas/{id}")
    public ResponseEntity<Oferta> getOferta(@PathVariable Long id) {
        log.debug("REST request to get Oferta : {}", id);
        Optional<Oferta> oferta = ofertaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(oferta);
    }

    /**
     * {@code DELETE  /ofertas/:id} : delete the "id" oferta.
     *
     * @param id the id of the oferta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ofertas/{id}")
    public ResponseEntity<Oferta> deleteOferta(@PathVariable Long id) {
        log.debug("REST request to delete Oferta : {}", id);
        Optional<Oferta> oferta = ofertaService.findOne(id);
        if(oferta.isPresent()) {
        	ofertaService.delete(id);
        }
       return ResponseUtil.wrapOrNotFound(oferta);
        /*ofertaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();*/
    }

    /**
     * {@code SEARCH  /_search/ofertas?query=:query} : search for the oferta corresponding
     * to the query.
     *
     * @param query the query of the oferta search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ofertas")
    public ResponseEntity<List<Oferta>> searchOfertas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Ofertas for query {}", query);
        Page<Oferta> page = ofertaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
