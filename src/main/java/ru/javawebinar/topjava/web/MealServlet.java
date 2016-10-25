package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealDAOMap;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by User on 20.10.2016.
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private static MealDAO mealDAO = new MealDAOMap();

    private void refresh(HttpServletRequest req)
    {
        List<Meal> meals = mealDAO.getData();
        List<MealWithExceed> mealWithExceeds = MealsUtil.getWithExceeded(meals,2000);
        req.setAttribute("list",mealWithExceeds);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        if ("multiedit".equals(req.getParameter("action")))
            mealDAO.updateMeals(req.getParameterMap());

        else {
            if (!"".equals(req.getParameter("id"))) {
                int id = Integer.parseInt(req.getParameter("id"));
                int calories = (("".equals(req.getParameter("calories"))) ? 0 : Integer.parseInt(req.getParameter("calories")));

                mealDAO.updateOneMeal
                        (id, req.getParameter("description"), calories, req.getParameter("dateTime"));
            }
            else
            {
                int calories = (("".equals(req.getParameter("calories"))) ? 0 : Integer.parseInt(req.getParameter("calories")));
                Meal meal = new Meal(TimeUtil.formatStringToLocalDateTime(req.getParameter("dateTime")),req.getParameter("description"),calories);
                mealDAO.addMeal(meal);
            }
        }

        refresh(req);
        req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("tralala");
        if ("multiedit".equals(req.getParameter("action")))
        {
            refresh(req);
            req.getRequestDispatcher("/mealListEdit.jsp").forward(req, resp);
        }
        else
        if ("edit".equals(req.getParameter("action")))
        {
            int id = Integer.parseInt(req.getParameter("id"));
            Meal meal = mealDAO.getMealById(id);
            req.setAttribute("meal",meal);
            req.getRequestDispatcher("/meal.jsp").forward(req, resp);
        }
        else
        if ("add".equals(req.getParameter("action")))
        {
            req.getRequestDispatcher("/meal.jsp").forward(req, resp);
        }
        else
        if ("delete".equals(req.getParameter("action")))
        {
            int id = Integer.parseInt(req.getParameter("id"));
            mealDAO.deleteMeal(id);

            refresh(req);
            req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
        }
        else {
            refresh(req);
            req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
        }

//        resp.sendRedirect("mealList.jsp");
    }
}
