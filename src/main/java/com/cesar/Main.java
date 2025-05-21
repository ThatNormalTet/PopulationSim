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
            population.add(new Person(Person.getNextIdPerson(), rng.nextBoolean(), parents));

        }
        generations.add(new ArrayList<>(population)); // Añadir generación 0
        nextGen(1, 2);


        printGenerations();

    }

    public static void printGenerations() {
        System.out.println("Árbol genealógico por generaciones:");
        if (generations.isEmpty()) {
            System.out.println("No hay generaciones para mostrar.");
            return;
        }

        for (int genIndex = 0; genIndex < generations.size(); genIndex++) {
            List<Person> generation = generations.get(genIndex);
            System.out.println("\nGeneración " + genIndex + ":");

            for (Person person : generation) {
                printTree(person, 0, new ArrayList<>());
            }

        }
    }

    private static void printTree(Person person, int depth, List<Integer> visited) {
        if (visited.contains(person.id)) {
            return; // Evita ciclos
        }
        visited.add(person.id);

        String sexStr = (person.sex ? "\u001B[36m♂" : "\u001B[35m♀") + "\u001B[0m";
        String indent = sexStr + " " + "\t".repeat(depth);
        System.out.printf(indent + "- %s: \n", person);

        for (Person child : person.children) {
            printTree(child, depth + 1, visited);
        }
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
            } else females.add(person);
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


}
