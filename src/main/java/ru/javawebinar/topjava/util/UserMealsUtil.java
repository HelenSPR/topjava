package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {

        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExcess> mealsToStream = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }


    //lookup 1 time
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        int Sum = 0;
        List<UserMealWithExcess> mealsResult = new ArrayList<>();
        Excess excess = new Excess();

        //for (int i = 0; i < meals.size(); i++)
        for(UserMeal userMeal : meals){
            //if (meals.get(i).GetLocalTime().isAfter(startTime) && meals.get(i).GetLocalTime().isBefore(endTime))
            if (TimeUtil.isBetweenHalfOpen(userMeal.getLocalTime(), startTime, endTime)){
                mealsResult.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess));
                Sum += userMeal.getCalories();
            }
        }

        if (Sum > caloriesPerDay) {
            excess.isExcess = true;
        };

        return mealsResult;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Excess excess = new Excess();

        List<UserMeal> mealsResult = meals.stream().filter(s -> TimeUtil.isBetweenHalfOpen(s.getLocalTime(), startTime, endTime)).collect(Collectors.toList());//s.getLocalTime().isAfter(startTime) && s.getLocalTime().isBefore(endTime)).collect(Collectors.toList());
        excess.isExcess = mealsResult.stream().mapToInt(s->s.getCalories()).sum() > caloriesPerDay;

        for(UserMeal userMeal: mealsResult) {
            result.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess));
        }

        return result;
    }
}
