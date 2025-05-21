package com.cesar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Couple {
    Person partner1;
    Person partner2;

    public Couple(Person partner1, Person partner2) {
        this.partner1 = partner1;
        this.partner2 = partner2;
    }

    public void reproduce(){
        Random rng = new Random();
        int id = Main.getMaxID() + 1;
        boolean sex = rng.nextBoolean();

        List<Person> parents = new ArrayList<>();
        parents.add(partner1);
        parents.add(partner2);

        Person child = new Person(id, sex, parents);
        Main.newborns.add(child);
        partner1.children.add(child);
        partner2.children.add(child);
    }
}
