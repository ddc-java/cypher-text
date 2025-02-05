package edu.cnm.deepdive.cypherText.service;

import edu.cnm.deepdive.cypherText.model.entity.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Profile("service")
@Service
public class UserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

  private final AbstractUserService service;

  public UserConverter(AbstractUserService service) {
    this.service = service;
  }

  @Override
  public UsernamePasswordAuthenticationToken convert(Jwt source) {
    Collection<SimpleGrantedAuthority> grants =
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    User user = service.getOrCreate(source.getSubject(), source.getClaimAsString("name"));
    return new UsernamePasswordAuthenticationToken(user, source.getTokenValue(), grants);
  }
}
