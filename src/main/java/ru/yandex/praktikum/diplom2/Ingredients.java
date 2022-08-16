package ru.yandex.praktikum.diplom2;

import java.util.List;

public class Ingredients {

    List<String> ingredients;

    public Ingredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredients() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
