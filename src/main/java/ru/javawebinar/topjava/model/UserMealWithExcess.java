package ru.javawebinar.topjava.model;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

public class UserMealWithExcess {
    //private final LocalDateTime dateTime;

    //private final String description;

    //private final int calories;

    public  List<UserMeal> userMeal ;

    public  boolean excess;

   /* public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
                    this.dateTime = dateTime;
                    this.description = description;
                    this.calories = calories;
                    this.excess = excess;
                }*/

    public UserMealWithExcess(List<UserMeal> _userMeal, boolean excess)
    {
        this.userMeal = _userMeal;
        this.excess = excess;
    }

    /*@Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }*/

}
