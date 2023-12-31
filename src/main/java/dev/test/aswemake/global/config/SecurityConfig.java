package dev.test.aswemake.global.config;


import dev.test.aswemake.global.jwt.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationManagerConfig authenticationManagerConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .formLogin().disable()
                .csrf().disable()
                .cors()
                .and()
                .apply(authenticationManagerConfig)
                .and()
                .httpBasic().disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                //MemberController
                .antMatchers(HttpMethod.POST, "/api/member").permitAll()
                .antMatchers(HttpMethod.POST, "/api/member/login").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/member").authenticated()

                //ProductController
                .antMatchers(HttpMethod.GET, "/api/product/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/product").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/product/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/product/**").authenticated()

                //OrderController
                .antMatchers(HttpMethod.POST, "/api/order").authenticated()
                .antMatchers(HttpMethod.GET, "/api/order").authenticated()
                .antMatchers(HttpMethod.GET, "/api/order/**").authenticated()

                //CouponController
                .antMatchers(HttpMethod.POST, "/api/coupon").authenticated()
                .antMatchers(HttpMethod.POST, "/api/coupon/pay").authenticated()


                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .build();
    }
}