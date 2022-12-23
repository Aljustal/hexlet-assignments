package exercise;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.DELETE;

import exercise.model.UserRole;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().and().sessionManagement().disable();

        // BEGIN
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                // Регистрация доступна всем пользователям
                .antMatchers(POST, "/users").permitAll()
                // Доступ к url /hello доступен аутентифицированным пользователям
                // с полномочиями "USER" или "ADMIN"
                .antMatchers("/users").hasAnyAuthority(UserRole.USER.name(), UserRole.ADMIN.name())
                // Доступ к url /admin и всем вложенным url, например /admin/hello
                // разрешён только с полномочиями "ADMIN"
                .antMatchers(DELETE,"/users/{id}").hasAuthority(UserRole.ADMIN.name())
                .and().httpBasic();
        // END
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService);
    }
}
