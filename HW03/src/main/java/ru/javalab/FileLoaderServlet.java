package ru.javalab;

import com.fileloader.FileLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class FileLoaderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<String> url = new ArrayList<>();
        Arrays.stream(req.getReader().readLine().split("&")).forEach(param -> {
            if (param.split("=").length == 2) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                if (name.matches("^url[0-9]+$")) {
                    try {
                        url.add(URLDecoder.decode(value, StandardCharsets.UTF_8.name()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        FileLoader fileLoader = new FileLoader();
        fileLoader.load(Arrays.copyOf(url.toArray(), url.size(), String[].class));
    }
}
