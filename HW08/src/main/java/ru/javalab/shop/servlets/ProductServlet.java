package ru.javalab.shop.servlets;

import ru.javalab.di.ApplicationContext;
import ru.javalab.shop.model.Product;
import ru.javalab.shop.service.product.ProductService;
import ru.javalab.shop.service.product.ProductServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductServlet extends HttpServlet {
    private ApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.context = (ApplicationContext) servletContext.getAttribute("context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = context.getComponent(ProductServiceImpl.class, "productService");
        List<Product> productList = productService.getProducts();
        req.setAttribute("products", productList);
        req.getRequestDispatcher("index.ftlh").forward(req, resp);
    }
}
