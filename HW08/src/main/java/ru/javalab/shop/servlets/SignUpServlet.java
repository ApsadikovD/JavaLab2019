package ru.javalab.shop.servlets;

import ru.javalab.di.ApplicationContext;
import ru.javalab.shop.dto.UserDto;
import ru.javalab.shop.service.login.SignUpServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {
    private ApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.context = (ApplicationContext) servletContext.getAttribute("context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/signup.ftlh").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignUpServiceImpl signUpService = context.getComponent(SignUpServiceImpl.class, "signUpService");
        UserDto userDto = signUpService.login(req.getParameter("email"), req.getParameter("password"));
        System.out.println(userDto.getId() + " : " + userDto.getEmail());
        resp.sendRedirect("/");
    }
}
