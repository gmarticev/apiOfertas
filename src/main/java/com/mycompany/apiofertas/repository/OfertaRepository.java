package com.mycompany.apiofertas.repository;

import com.mycompany.apiofertas.domain.Oferta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Oferta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {
}
