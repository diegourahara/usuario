package com.diegofarias.usuario.business;

import com.diegofarias.usuario.business.converter.UsuarioConverter;
import com.diegofarias.usuario.business.dto.EnderecoDTO;
import com.diegofarias.usuario.business.dto.TelefoneDTO;
import com.diegofarias.usuario.business.dto.UsuarioDTO;
import com.diegofarias.usuario.infrasctructure.entity.Endereco;
import com.diegofarias.usuario.infrasctructure.entity.Telefone;
import com.diegofarias.usuario.infrasctructure.entity.Usuario;
import com.diegofarias.usuario.infrasctructure.exceptions.ConflictException;
import com.diegofarias.usuario.infrasctructure.exceptions.ResourceNotFoundException;
import com.diegofarias.usuario.infrasctructure.repository.EnderecoRepository;
import com.diegofarias.usuario.infrasctructure.repository.TelefoneRepository;
import com.diegofarias.usuario.infrasctructure.repository.UsuarioRepository;
import com.diegofarias.usuario.infrasctructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

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

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository
                            .findByEmail(email)
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("E-mail não encontrado " + email)
                            ));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("E-mail não encontrado " + email);
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO) {
        // Buscou o e-mail do usuário através do token (tirar obrigatoriedade de passar e-mail)
        String email = jwtUtil.extractUsername(token.substring(7));

        // Criptografia de senha
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        // Buscou os dados do usuário no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("E-mail não localizado"));

        // Mesclou os dados que recebemos na requisição DTO com os dados od banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

        // Salvou os dados do usuário convertido e depois pegou o retorno convertido para UsuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado " + idEndereco)
        );

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado " + idTelefone)
        );

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

}