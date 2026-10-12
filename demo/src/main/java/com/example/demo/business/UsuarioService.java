package com.example.demo.business;

import com.example.demo.infraestructure.entity.Usuario;
import com.example.demo.infraestructure.exceptions.ConflictExeption;
import com.example.demo.infraestructure.exceptions.ResourceNotFoundException;
import com.example.demo.infraestructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // para inserir construtores e não precisar injetar dependencias com @Autowierd (necessário incluir como final o repository)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario salvarUsuario(Usuario usuario){
        try{
            emailExiste(usuario.getEmail());
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

            return usuarioRepository.save(usuario);

        }catch(ConflictExeption e){
            throw new ConflictExeption("Email já cadastrado" + usuario.getEmail());

        }
    }

    public void emailExiste(String email){
        try{
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictExeption("Email ja cadastrado " + email);
            }
        }catch (ConflictExeption e){
            throw new ConflictExeption("Email já cadastrado" + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscaUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado" + email)
        );
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

}
