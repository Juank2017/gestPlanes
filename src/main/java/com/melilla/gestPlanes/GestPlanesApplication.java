package com.melilla.gestPlanes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

@SpringBootApplication
public class GestPlanesApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(GestPlanesApplication.class, args);
	}
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException{
        super.onStartup(servletContext);
    }
}
