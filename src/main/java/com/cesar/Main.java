package com.cesar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Main {
    public static List<List<Person>> generations = new ArrayList<>();
    public static List<Person> population = new ArrayList<>();
    public static List<Person> newborns = new ArrayList<>();
    public static List<Couple> couples = new ArrayList<>();
    public static boolean inbreeding = true;

    public static void main(String[] args) {
        List<Person> parents = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Random rng = new Random();
            population.add(new Person(i, rng.nextBoolean(), parents));
        }

        nextGen(1, 2);



    }

    public static void nextGen(int gens, int children){
        for (int i = 0; i < gens; i++) {
            coupleUp();
            for (Couple couple : couples){
                for (int j = 0; j < children; j++) {
                    couple.reproduce();
                }
            }
            generations.add(population);
            population.clear();
            population.addAll(newborns);
            newborns.clear();
        }
    }

    public boolean isInbred(){
        for (Person person : population){
            if (person.parents.getFirst().isBrother(person.parents.getLast())){
                return true;
            }
        }
        return false;
    }

    public static void coupleUp(){
        List<Person> males = new ArrayList<>();
        List<Person> females = new ArrayList<>();

        for (Person person : population){
            if (person.sex){
                males.add(person);
            }else females.add(person);
        }

        List<Person> more = males.size() > females.size() ? males : females;
        List<Person> less = males.size() > females.size() ? females : males;



        while (!more.isEmpty()) {
            Person moreFirst = more.getFirst();
            boolean match = false;
            int indexRemove = 0;

            for (int i = 0; i < less.size(); i++) {
                Person lessI = less.get(i);
                if (moreFirst.isBrother(lessI)){
                    if (!inbreeding){
                        continue;
                    }
                }
                indexRemove = i;
                match = true;
                break;
            }
            if (match){
                couples.add(new Couple(moreFirst, less.get(indexRemove)));
                less.remove(indexRemove);
            }
            more.removeFirst();
        }
    }

    public static int getMaxID(){
        int maxId = 0;

        for (Person person : population){
            if (person.id > maxId){
                maxId = person.id;
            }
        }

        return maxId;
    }
}
