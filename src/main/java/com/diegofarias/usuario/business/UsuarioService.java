package com.diegofarias.usuario.business;

import com.diegofarias.usuario.business.converter.UsuarioConverter;
import com.diegofarias.usuario.business.dto.UsuarioDTO;
import com.diegofarias.usuario.infrasctructure.entity.Usuario;
import com.diegofarias.usuario.infrasctructure.exceptions.ConflictException;
import com.diegofarias.usuario.infrasctructure.exceptions.ResourceNotFoundException;
import com.diegofarias.usuario.infrasctructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);

        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);

            if (existe) {
                throw new ConflictException("E-mail já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("E-mail já cadastrado " + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("E-mail não encontrado " + email));
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

}