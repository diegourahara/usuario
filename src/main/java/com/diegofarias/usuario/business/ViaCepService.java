package com.diegofarias.usuario.business;

import com.diegofarias.usuario.infrasctructure.clients.ViaCepClient;
import com.diegofarias.usuario.infrasctructure.clients.ViaCepDTO;
import com.diegofarias.usuario.infrasctructure.exceptions.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient viaCepClient;

    public ViaCepDTO buscarDadosEndereco(String cep) {
        return viaCepClient.buscaDadosEndereco(processarCep(cep));
    }

    private String processarCep(String cep) {
        String cepFormatado = cep.replace(" ", "").replace("-", "");

        if (!cepFormatado.matches("\\d+") || !Objects.equals(cepFormatado.length(), 8)) {
            throw new IllegalArgumentException("O cep contém caractéres inválidos. Favor verificar.");
        }

        return cepFormatado;
    }

}