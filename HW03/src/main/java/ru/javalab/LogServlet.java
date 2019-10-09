package ru.javalab;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogServlet implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String method = req.getMethod();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String dateString = formatter.format(date);

        FileWriter file = new FileWriter("log.txt", true);
        PrintWriter pw = new PrintWriter(file);
        pw.append("Date: ").append(dateString)
                .append(", Method: ").append(method)
                .append(", Address: ").append(req.getRequestURL())
                .append(", IP: ").append(req.getRemoteAddr())
                .append("\r\n");
        pw.flush();
        pw.close();
        file.close();
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
