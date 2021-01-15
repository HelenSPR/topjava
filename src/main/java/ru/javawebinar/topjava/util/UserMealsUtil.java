package ru.javawebinar.topjava.util;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserMealsUtil
{
    public static void main(String[] args)
    {

                List<UserMeal> meals = Arrays.asList(
                                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
                                );
        //UserMealWithExcess userWithMeal = new UserMealWithExcess(meals, false);
        UserMealWithExcess mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.userMeal.forEach(System.out::println);
        if (mealsTo.excess)
            System.out.println("Calories are higer!!!");
        else
            System.out.println("Calories are ok :) ");

        System.out.println("******************* STREAM ******************");
        UserMealWithExcess mealsToStream = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToStream.userMeal.forEach(System.out::println);
        if (mealsToStream.excess)
            System.out.println("Calories are higer!!!");
        else
            System.out.println("Calories are ok :) ");
                            }


            //циклом за 1 проход
            public static UserMealWithExcess filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay)
            {
                int Sum = 0;
                List<UserMeal> mealsResult = new ArrayList<UserMeal>();
                LocalTime currentTime = LocalTime.of(0,0);

                for(int i = 0; i< meals.size(); i++)
                {
                    if (meals.get(i).GetLocalTime().isAfter(startTime) && meals.get(i).GetLocalTime().isBefore(endTime)) {
                        mealsResult.add(meals.get(i));
                        Sum += meals.get(i).getCalories();
                    }
                }

                UserMealWithExcess result = new UserMealWithExcess(mealsResult, Sum > caloriesPerDay ? true : false) ;

                return result;
            }

            public static UserMealWithExcess filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay)
            {
                List<UserMeal> mealsResult = meals.stream().filter(s->s.GetLocalTime().isAfter(startTime) && s.GetLocalTime().isBefore(endTime)).collect(Collectors.toList());
                int currentColories = mealsResult.stream().mapToInt(o->o.getCalories()).sum();
                System.out.println(currentColories);
                System.out.println(caloriesPerDay);
                System.out.println(currentColories > caloriesPerDay);
                return new UserMealWithExcess(mealsResult,  (currentColories > caloriesPerDay ? true : false));
            }
}
