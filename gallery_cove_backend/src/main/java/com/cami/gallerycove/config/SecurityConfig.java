package com.cami.gallerycove.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/user/getTop3Artists", "/user/getAllArtists", "/user/get/*", "/user/updateProfile/*", "/user/getToken/*",
                                "/user/delete/*", "/user/getAllVisitors", "/user/updatePassword", "/user/sendEmailToAdmin").permitAll()
                        .requestMatchers("/artwork/DTOforFE", "/artwork/getAllOldToNew", "/artwork/getAllByPriceAsc", "/artwork/getAllByPriceDesc",
                                "/artwork/getAllRandom", "/artwork/get/*", "/artwork/getAllByUser/*", "/artwork/add", "/artwork/get3Random", "/artwork/delete/*", "/artwork/update/*",
                                "/artwork/getAllByCategory/*").permitAll()
                        .requestMatchers("/event/DTOforFE", "/event/DTOforFEPast", "/event/join", "/event/leave", "/event/getAllThatFollowByUser/*",
                                "/event/get/*", "/event/update/*", "/event/delete/*", "/event/whereJoined/*", "/event/add", "/event/getAllThatPassedByUser/*").permitAll()
                        .requestMatchers("/review/getAllByArtwork/*", "/review/add", "/review/update/*", "/review/delete/*").permitAll()
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/favorite/getAllByUser/*", "/favorite/delete", "/favorite/add").permitAll()
                        .requestMatchers("/category/getAll").permitAll()
                        .requestMatchers("/artwork_image/add", "/artwork_image/deleteAllImagesOfArtwork/*").permitAll()
                        .requestMatchers("/forgot_password/sendVerificationCode", "/forgot_password/verifyCode").permitAll()
                );

        return httpSecurity.build();
    }
}
