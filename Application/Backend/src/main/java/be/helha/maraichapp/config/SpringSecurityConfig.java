package be.helha.maraichapp.config;



import be.helha.maraichapp.services.UserService;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtFilter jwtFilter) throws Exception {
        return
                httpSecurity
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(
                                authorize -> {
                                    //.Auth Controller
                                    authorize.requestMatchers(POST, "/signup").permitAll();
                                    authorize.requestMatchers(POST, "/activation").permitAll();
                                    authorize.requestMatchers(POST, "/login").permitAll();
                                    //.Category Controller
                                    authorize.requestMatchers(GET, "/categories/**").permitAll();
                                    authorize.requestMatchers(POST, "/categories/**").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(PUT, "/categories/**").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(DELETE, "/categories/**").hasRole("ADMINISTRATOR");
                                    //.Image Controller
                                    authorize.requestMatchers(GET, "/images/**").permitAll();
                                    authorize.requestMatchers(POST, "/images/**").hasAnyRole("MARAICHER", "ADMINISTRATOR");
                                    //.Order Controller
                                    authorize.requestMatchers(GET, "/orders/getAll").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/orders/get/{id}").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/orders/get/customer/{customerId}").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/orders/get/shop/{shopId}").hasAnyRole("MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(POST, "/orders/addOrder").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(PUT, "/orders/update/order").hasAnyRole("ADMINISTRATOR", "MARAICHER");
                                    authorize.requestMatchers(DELETE, "/orders/delete/{id}").hasRole("ADMINISTRATOR");
                                    //.Product Controller
                                    authorize.requestMatchers(GET, "/products/get-all").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/products/get-all-by-categories").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/products/getQuantityAvailable/{id}").permitAll();
                                    authorize.requestMatchers(GET, "/products/get-all-by-shop/{id}").permitAll();
                                    authorize.requestMatchers(POST, "/products/new").hasAnyRole("MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(PUT, "/products/update/{id}").hasAnyRole("MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(DELETE, "/products/delete/{id}").hasAnyRole("MARAICHER", "ADMINISTRATOR");
                                    //.Request Controller
                                    authorize.requestMatchers(GET, "/requests/get/{id}").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/requests/getAll").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(POST, "/requests/addRequest").hasAnyRole("CUSTOMER", "ADMINISTRATOR");
                                    authorize.requestMatchers(PUT, "/requests/update/request").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(DELETE, "/requests/delete/{id}").hasRole("ADMINISTRATOR");
                                    //.Reservation Controller
                                    authorize.requestMatchers(POST, "/reservations/addReservation").hasAnyRole("CUSTOMER","MARAICHER","ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/reservations/getAll").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(GET,"/reservations/getShoppingCartUser/{idUser}").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(PUT, "/reservations/update").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(DELETE, "/reservations/delete/{id}").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(DELETE, "/reservations/deleteShoppingCart/{idUser}").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/reservations/existShoppingCart/{idUser}/{idShop}").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    //.Shop Controller
                                    authorize.requestMatchers(GET, "/shop/getAll").permitAll();
                                    authorize.requestMatchers(GET, "/shop/getAllAdmin").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/shop/getById/{id}").permitAll();
                                    authorize.requestMatchers(GET, "/shop/owner/{id}").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/shop/getByName/{name}").permitAll();
                                    authorize.requestMatchers(POST, "/shop/add").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(PUT, "/shop/update").hasAnyRole("MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/shop/turnOnOrOff/{idShop}").hasAnyRole("MARAICHER","ADMINISTRATOR");
                                    authorize.requestMatchers(DELETE, "/shop/delete/{id}").hasRole("ADMINISTRATOR");
                                    //.User Controller
                                    authorize.requestMatchers(GET, "/users/get/{id}").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/users/getByRank/{rank}").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(GET, "/users/getAll").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(GET,"/users/getAllRanks").permitAll();
                                    authorize.requestMatchers(POST, "/users/newUser").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(PUT, "/users/update/admin").hasRole("ADMINISTRATOR");
                                    authorize.requestMatchers(PUT, "/users/update/restricted").hasAnyRole("CUSTOMER", "MARAICHER", "ADMINISTRATOR");
                                    authorize.requestMatchers(DELETE, "/users/delete/{id}").hasRole("ADMINISTRATOR");
                                }
                        )
                        .sessionManagement(httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .cors(Customizer.withDefaults())
                        .build();
    }

    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:4200"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
    @Bean
    public AuthenticationManager authenticaticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }
}
