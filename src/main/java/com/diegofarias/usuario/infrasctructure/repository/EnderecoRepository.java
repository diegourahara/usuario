package com.diegofarias.usuario.infrasctructure.repository;

import com.diegofarias.usuario.infrasctructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}