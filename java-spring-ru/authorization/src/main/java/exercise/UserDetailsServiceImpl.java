package exercise;

import exercise.model.User;
import exercise.model.UserRole;
import exercise.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // BEGIN
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        UserRole userRole = user.getRole();

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(userRole.toString())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities
        );
        // END
    }
}
