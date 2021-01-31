package ru.javawebinar.topjava.util;

import javafx.util.Pair;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
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
        int bufSum = 0;
        Pair<Integer, Boolean> bufPair;
        HashMap<LocalDate, Pair<Integer, Boolean>> dateForExcess = new HashMap<>();
        List<UserMealWithExcess> mealsResult = new ArrayList<>();


        for (UserMeal userMeal : meals) {
            if (dateForExcess.containsKey(userMeal.getDateTime().toLocalDate())) {
                bufSum = dateForExcess.get(userMeal.getDateTime().toLocalDate()).getKey();
                bufPair = new Pair<>(bufSum + userMeal.getCalories(), bufSum + userMeal.getCalories() > caloriesPerDay);
                dateForExcess.put(userMeal.getDateTime().toLocalDate(), bufPair);
            } else {
                bufPair = new Pair<>(userMeal.getCalories(), userMeal.getCalories() > caloriesPerDay);
                dateForExcess.put(userMeal.getDateTime().toLocalDate(), bufPair);
            }
        }
        for(UserMeal userMeal : meals){
            if (TimeUtil.isBetweenHalfOpen(userMeal.getLocalTime(), startTime, endTime)) {
                mealsResult.add(new UserMealWithExcess(userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                          dateForExcess.get(userMeal.getDateTime().toLocalDate()).getValue() ));
            }
        }

        return mealsResult;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> maps = meals.stream()
                                        .collect(
                                                Collectors.groupingBy(UserMeal::getDate,
                                                Collectors.summingInt(UserMeal::getCalories)));

        List<UserMealWithExcess> result = meals.stream().filter(s -> TimeUtil.isBetweenHalfOpen(s.getLocalTime(), startTime, endTime)).map(o-> {
            return new UserMealWithExcess(o.getDateTime(), o.getDescription(), o.getCalories(), (maps.get(o.getDate()) > caloriesPerDay));
        }).collect(Collectors.toList());

        return result;
    }
}
