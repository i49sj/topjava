package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);
    private MealRestController mealController;

    private MealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryMealRepositoryImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        repository.save(meal);
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        Integer userId = (Integer)request.getSession().getAttribute("userId");
        if(userId == null){
            response.sendRedirect("./");
            return;
        }

        if (StringUtils.isEmpty(action)) {
            LOG.info("getAll");
            request.setAttribute("mealList",
                    mealController.getWithFilter(userId,
                            TimeUtil.parseDate((String) request.getSession().getAttribute("fromDate")),
                            TimeUtil.parseDate((String) request.getSession().getAttribute("toDate")),
                            TimeUtil.parseTime((String) request.getSession().getAttribute("fromTime")),
                            TimeUtil.parseTime((String) request.getSession().getAttribute("toTime"))));

            request.getRequestDispatcher("mealList.jsp").forward(request, response);

        } else if ("delete".equals(action)) {
            Integer mealId = null;

            try {
                mealId = getId(request);
                LOG.info("Delete {}", mealId);
                mealController.delete(mealId, userId);
                response.sendRedirect("meals");

            } catch (Exception e) {
                LOG.error(String.format("Could not perform deleting - a meal with id %d is not found!", mealId));
                request.setAttribute("message", "Ups! A meal is not found!");
                request.getRequestDispatcher("info.jsp").forward(request, response);

            }
        } else if ("create".equals(action) || "update".equals(action)) {

            try {
                Meal meal = action.equals("create") ?
                        new Meal(LocalDateTime.now().withNano(0).withSecond(0), "", 1000) :
                        mealController.get(getId(request), userId);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("mealEdit.jsp").forward(request, response);

            } catch (Exception e) {
                LOG.error("Could not perform updating - a meal is not found!");
                request.setAttribute("message", "Ups! A meal is not found!");
                request.getRequestDispatcher("info.jsp").forward(request, response);
            }
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
