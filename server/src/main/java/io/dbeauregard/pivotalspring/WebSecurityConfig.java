package io.dbeauregard.pivotalspring;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.provisioning.UserDetailsManagerResourceFactoryBean;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/","/index.html", "/css/**", "/images/**" ,"/houses","/houses/**").permitAll()
                        .requestMatchers("/actuator/**","/error","/v3/**","/swagger-ui*/**").permitAll()
                        .requestMatchers("/ai","/ai/structured", "/ai/stream").permitAll()
                        .anyRequest().authenticated()) //Default, require Auth (best practice)
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .csrf(AbstractHttpConfigurer::disable); //Potential Security Risk for browser based clients
        return http.build();
    }

	@Bean
	public InMemoryUserDetailsManager userDetailsService() throws Exception {
        return UserDetailsManagerResourceFactoryBean.fromResourceLocation("classpath:users.properties").getObject();
	}
}
