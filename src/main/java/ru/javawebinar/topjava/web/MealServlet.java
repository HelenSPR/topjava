package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet{
    private static final Logger log = getLogger(MealServlet.class);
    List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        //request.getRequestDispatcher("/meals.jsp").forward(request, response);
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        writer.println("<h2>Welcome to MealsServlet</h2>");
        writer.println("<ul>");
        try {
            for(MealTo mt : getMealsTo()) {
                writer.println("<li> <p style='color:" + (mt.Excess() ? "red" : "green") + "'>" + mt.toString() + "</p></li>");
            }
        } finally {
            writer.close();
        }
        writer.println("</ul>");
    }

    private List<MealTo> getMealsTo(){
        Random random = new Random();
        return  meals.stream()
                .map(o -> new MealTo(o.getDateTime(), o.getDescription(), o.getCalories(), random.nextBoolean()))
                .collect(Collectors.toList());
    }

}
