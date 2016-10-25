package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.Map;

/**
 * Created by User on 21.10.2016.
 */
public interface MealDAO {
    List<Meal> getData();
    void addMeal(Meal meal);
    void updateMeals(Map<String, String[]> mapUpdated);
    void updateOneMeal(int mealId, String description, int calories, String dateString);
    void deleteMeal(int id);
    Meal getMealById(int id);
}
