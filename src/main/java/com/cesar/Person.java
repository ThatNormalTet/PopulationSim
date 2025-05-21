package com.cesar;

import java.util.ArrayList;
import java.util.List;

public class Person {
    int id;
    boolean sex;
    List<Person> parents;
    List<Person> children = new ArrayList<>();
    private List<Person> degreeSearch = new ArrayList<>();
    public static int maxId = 0;

    public static int getNextIdPerson() {
        return ++Person.maxId;
    }

    public Person(int id, boolean sex, List<Person> parents) {
        this.id = id;
        this.sex = sex;
        this.parents = parents;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }

    public boolean hasChildren(){
        return !children.isEmpty();
    }

    public boolean isBrother(Person person){
        boolean isBrother = false;

        if (this.parents.isEmpty() || person.parents.isEmpty()){
            return false;
        }

        Person parent1 = this.parents.getFirst();
        Person parent2 = this.parents.getLast();
        Person parent3 = person.parents.getFirst();
        Person parent4 = person.parents.getLast();

        if (parent1 == parent3 || parent1 == parent4){
            isBrother = true;
        }

        if (parent2 == parent3 || parent2 == parent4){
            isBrother = true;
        }

        return isBrother;
    }

    public List<Person> getDegreeSearch() {
        return degreeSearch;
    }

    public void setDegreeSearch(List<Person> degreeSearch) {
        this.degreeSearch = degreeSearch;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id: \033[1;32m" + id + "\u001B[0m" +
                ", sex: " + (this.sex ? "\u001B[36m♂" : "\u001B[35m♀") +
                "\u001B[0m}";
    }
}
