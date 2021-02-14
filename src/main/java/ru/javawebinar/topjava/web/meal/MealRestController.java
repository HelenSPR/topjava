package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public class MealRestController {
    private MealService service;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public MealTo get(int id){
        log.info("get");
        Integer userId = getCurrentUserId();
        if (!userId.equals(null))
            return service.get(id, userId);

        return null;
    }

    @Autowired
    public Collection<MealTo> getAll(){
        log.info("getAll");
        return service.getAll();
    }

    @Autowired
    public Meal create(Meal meal){
        log.info("create");
        checkNew(meal);
        Integer userId = getCurrentUserId();
        if (!userId.equals(null)) {
            meal.setUserId(userId);
            return service.create(meal);
        }

        return null;
    }

    @Autowired
    public void update(Meal meal, int id){
        log.info("update");
        assureIdConsistent(meal, id);

        Integer userId = getCurrentUserId();
        if (!userId.equals(null))
            service.update(meal, id, userId);
    }

    @Autowired
    public void delete(int id){
        log.info("delete");
        Integer userId = getCurrentUserId();
        if (!userId.equals(null))
            service.delete(id, userId);
    }

    private Integer getCurrentUserId(){
        return SecurityUtil.authUserId();
    }
}