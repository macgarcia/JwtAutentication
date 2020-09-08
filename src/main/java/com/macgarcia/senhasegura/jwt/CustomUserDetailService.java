package com.macgarcia.senhasegura.jwt;

import com.macgarcia.senhasegura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UsuarioRepository dao;

    @Autowired
    public CustomUserDetailService(UsuarioRepository dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var usuario = Optional.ofNullable(dao.findByLogin(login))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        var authorityList = AuthorityUtils.createAuthorityList("ROLE_USER");
        return new User(usuario.getLogin(), usuario.getSenha(), authorityList);
    }
}
