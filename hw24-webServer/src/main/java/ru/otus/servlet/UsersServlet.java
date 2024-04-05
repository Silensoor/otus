package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import ru.otus.dao.UserDao;
import ru.otus.helpers.PasswordManager;
import ru.otus.model.User;
import ru.otus.services.TemplateProcessor;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;


@SuppressWarnings({"squid:S1948"})
public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "randomUser";
    private static final String TEMPLATE_ATTR_ALL_USER = "users";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_NAME = "name";

    private final UserDao userDao;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, UserDao userDao) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = getParamsMap();

        paramsMap.put(TEMPLATE_ATTR_ALL_USER,userDao.getAllUser());
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = getParamsMap();

        paramsMap.put(TEMPLATE_ATTR_ALL_USER,userDao.getAllUser());

        String login = request.getParameter(PARAM_NAME);
        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        userDao.saveUser(new User(null, name, login, new PasswordManager().hashPassword(password)));
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
    private Map<String, Object> getParamsMap(){
        Map<String, Object> paramsMap = new HashMap<>();
        userDao.findRandomUser().ifPresent(randomUser -> paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, randomUser));
        return paramsMap;
    }
}
