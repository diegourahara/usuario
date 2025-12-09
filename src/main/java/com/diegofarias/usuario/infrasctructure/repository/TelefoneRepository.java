package com.diegofarias.usuario.infrasctructure.repository;

import com.diegofarias.usuario.infrasctructure.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

}