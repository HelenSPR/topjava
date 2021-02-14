package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.createTo;


public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal));
    }

    @Override
    public Meal save(Meal meal) {
        if (!meal.getUserId().equals(null)) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                repository.put(meal.getId(), meal);
                return meal;
            }
            // handle case: update, but not present in storage
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        if (!userId.equals(null)) {
            if (IsSelfMeal(id, userId))
                return repository.remove(id) != null;
        }

        return false;

    }

    @Override
    public Meal get(int id, Integer userId) {
        //  Integer userId = getCurrentUserId();
        if (!userId.equals(null)) {
            if (IsSelfMeal(id, userId))
                return repository.get(id);
        }

        return null;
    }

    @Override
    public Collection<MealTo> getAll() {
        List<Meal> meals =
                repository
                        .values()
                        .stream()
                        .sorted((o1, o2) -> (o1.getDate().compareTo(o2.getDate()))).collect(Collectors.toList());
        Collections.reverse(meals);
        return ConvertToMealTo(meals);
    }

    @Override
    public Meal create(Meal meal, Integer userId) {
        if (!userId.equals(null)) {
            if (IsSelfMeal(meal.getId(), userId)) {
                if (meal.isNew()) {
                    meal.setId(counter.incrementAndGet());
                    meal.setUserId(userId);
                    repository.put(meal.getId(), meal);
                    return meal;
                }
            }
        }

        return null;
    }

    ///to do
    @Override
    public boolean update(Meal meal, int id, Integer userId) {
        return false;
    }

    private Boolean IsSelfMeal(int id, int userId) {
        Meal meal = repository.entrySet().stream().filter(m -> m.getValue().getId() == id).findFirst().get().getValue();
        if (meal != null && meal.getUserId() == userId) return true;

        return false;
    }

    private Collection<MealTo> ConvertToMealTo(List<Meal> meals) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > DEFAULT_CALORIES_PER_DAY))
                .collect(Collectors.toList());
    }


}

