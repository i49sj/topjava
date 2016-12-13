package ru.javawebinar.topjava.to;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

/**
 * GKislin
 * 11.01.2015.
 */
public class MealWithExceed extends Meal{

    private final boolean exceed;

    public MealWithExceed(Meal meal, boolean exceed) {
        super(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        this.exceed = exceed;
    }

    public boolean isExceed() {
        return exceed;
    }


}
