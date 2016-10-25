package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by User on 21.10.2016.
 */
public class MealDAOMap implements MealDAO {
    private AtomicInteger count = new AtomicInteger(0);
    private static final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    public MealDAOMap(){
        for (Meal meal: MealsUtil.meals){
            meal.setId(count.addAndGet(1));
            mealsMap.put(meal.getId(), meal);
        }
    }

    @Override
    public List<Meal> getData() {
        return new ArrayList<>(mealsMap.values());
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setId(count.addAndGet(1));
        mealsMap.put(meal.getId(), meal);
    }

    @Override
    public void updateMeals(Map<String, String[]> mapUpdated) {
        String[] ids = mapUpdated.get("id");
        int[] idInts = new int[ids.length];
        for (int i = 0;i < ids.length; i++)
        {idInts[i] = Integer.parseInt(ids[i]);}
        String[] calories = mapUpdated.get("calories");
        String[] descriptions = mapUpdated.get("description");
        String[] dateTimes = mapUpdated.get("dateTime");
        LocalDateTime[] localDateTimes = new LocalDateTime[ids.length];
        for (int i = 0;i < dateTimes.length; i++)
        {
            localDateTimes[i] = TimeUtil.formatStringToLocalDateTime(dateTimes[i]);
        }

        for (int j = 0;j < idInts.length; j++)
        {
            Meal meal = mealsMap.get(idInts[j]);
            if (meal!=null) {
                meal.setDescription(descriptions[j]);
                meal.setCalories(Integer.parseInt(calories[j]));
                meal.setDateTime(localDateTimes[j]);
            }
        }
    }

    @Override
    public void updateOneMeal(int mealId, String description, int calories, String dateString) {
        Meal meal = mealsMap.get(mealId);
        if (meal != null){
            meal.setDescription(description);
            meal.setCalories(calories);
            meal.setDateTime(TimeUtil.formatStringToLocalDateTime(dateString));
        }
    }

    @Override
    public void deleteMeal(int id) {
        mealsMap.remove(id);
    }

    @Override
    public Meal getMealById(int id) {
        return mealsMap.get(id);
    }
}
