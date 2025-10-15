package com.diegofarias.usuario.business;

import com.diegofarias.usuario.business.converter.UsuarioConverter;
import com.diegofarias.usuario.business.dto.UsuarioDTO;
import com.diegofarias.usuario.infrasctructure.entity.Usuario;
import com.diegofarias.usuario.infrasctructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);

        return usuarioConverter.paraUsuarioDTO(usuario);
    }

}