package com.ranum.adventure.bna.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ranum.adventure.bna.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SpringSecurity implements WebMvcConfigurer {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/register/**").permitAll()
						.requestMatchers("/assets/images/**").permitAll().requestMatchers("/assets/**").permitAll()
						.requestMatchers("/", "/index-fe").permitAll().requestMatchers("/fe/**").permitAll()
						.requestMatchers("/berita/**").permitAll().requestMatchers("/kontak/**").permitAll()
						.requestMatchers("/kontak/submit").permitAll()
						.requestMatchers("/admin/**", "/dashboard/**", "/umkm/**", "/berita/**", "/slider/**")
						.hasRole("ADMIN").requestMatchers("/user/**").hasRole("USER").requestMatchers("/**")
						.authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
//						.defaultSuccessUrl("/index")
						.successHandler(authenticationSuccessHandler()).permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/") // Kembali ke halaman index setelah logout berhasil
						.permitAll());
		return http.build();
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("fe/index-fe");
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}

	private class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			for (GrantedAuthority authority : authorities) {
				if (authority.getAuthority().equals("ROLE_ADMIN")) {
					HttpSession session = request.getSession();
					Long userId = userService.getUserIdByUsername(authentication.getName());
					session.setAttribute("userId", userId);
					response.sendRedirect("/admin/dashboard");
					return;
				} else if (authority.getAuthority().equals("ROLE_USER")) {
					HttpSession session = request.getSession();
					Long userId = userService.getUserIdByUsername(authentication.getName());
					session.setAttribute("userId", userId);
					response.sendRedirect("/user/home");
					return;
				}
			}
			// Default redirect for other roles
			response.sendRedirect("/");
		}
	}
	
	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry
	            .addResourceHandler("/uploads/**")
	            .addResourceLocations("file:/uploads/");
	    }
}
