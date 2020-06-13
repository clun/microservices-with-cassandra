package org.ff4j.sample.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.datastax.oss.driver.api.core.CqlSession;

@Controller
public class HomeController {
    
    /** Some logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    public CqlSession cqlSession;
    
    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model) {
        LOGGER.info(" + Rendering home page...");
        LOGGER.info("[OK] Success");
        return "home";
    }
    

}
