package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<MealTo> getAll() {
        return repository.getAll().stream().sorted((a, b) -> a.getDateTime().compareTo(b.getDateTime())).collect(Collectors.toList());
    }

    public MealTo get(int id, Integer userId) {
        Meal meal = checkNotFoundWithId(repository.get(id, userId), id);
        return Convert(meal);
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void delete(int id, Integer userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public void update(Meal meal, int id, Integer userId) {
        checkNotFoundWithId(repository.update(meal, id, userId), id);
    }

    private MealTo Convert(Meal meal) {
        return null;
    }

    private List<MealTo> ConvertList(List<Meal> meals) {
        return null;
    }

}