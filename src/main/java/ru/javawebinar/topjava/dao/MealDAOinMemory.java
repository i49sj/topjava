package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by User on 23.10.2016.
 */
public class MealDAOinMemory implements MealDAO {
    private AtomicInteger count = new AtomicInteger(0);

    private static final List<Meal> meals = MealsUtil.meals;
    public MealDAOinMemory(){

    }

    @Override
    public List<Meal> getData() {
        return meals;
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setId(count.addAndGet(1));
        meals.add(meal);
    }

    @Override
    public void updateMeals(Map<String, String[]> mapUpdated) {
        String[] ids = mapUpdated.get("id");
        int[] idInts = new int[ids.length];
        for (int i=0;i<ids.length;i++)
        {idInts[i] = Integer.parseInt(ids[i]);}
        String[] calories = mapUpdated.get("calories");
        String[] descriptions = mapUpdated.get("description");
        String[] dateTimes = mapUpdated.get("dateTime");
        LocalDateTime[] localDateTimes = new LocalDateTime[ids.length];
        for (int i=0;i<dateTimes.length;i++)
        {
            localDateTimes[i] = TimeUtil.formatStringToLocalDateTime(dateTimes[i]);
        }

        for (Meal meal: meals)
        {
            for (int j=0;j<idInts.length;j++)
            {
                if (meal.getId()==idInts[j]) {
                    meal.setCalories(Integer.parseInt(calories[j]));
                    meal.setDescription(descriptions[j]);
                    meal.setCalories(Integer.parseInt(calories[j]));
                    meal.setDateTime(localDateTimes[j]);
                    break;
                }
            }
        }
    }

    @Override
    public void updateOneMeal(int mealId, String description, int calories, String dateString) {
        for(Meal meal : meals){
            if (meal.getId() == mealId){
                meal.setDescription(description);
                meal.setCalories(calories);
                meal.setDateTime(TimeUtil.formatStringToLocalDateTime(dateString));
            }
        }
    }

    @Override
    public void deleteMeal(int id) {
        Iterator<Meal> it = meals.iterator();
        while (it.hasNext()) {
            Meal meal = it.next();
            if (meal.getId() == id) {
                it.remove();
                break;
            }
        }
    }

    @Override
    public Meal getMealById(int id) {
        for (Meal meal:meals)
        {
            if (meal.getId() == id)
            {
                return meal;
            }
        }
        return null;
    }
}
