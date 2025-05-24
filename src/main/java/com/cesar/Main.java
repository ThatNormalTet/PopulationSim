package com.cesar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static List<List<Person>> generations = new ArrayList<>();
    public static List<Person> population = new ArrayList<>();
    public static List<Person> newborns = new ArrayList<>();
    public static List<Couple> couples = new ArrayList<>();
    public static boolean inbreeding = true;
    public static int maxID = 0;

    public static void main(String[] args){
        long startTime = System.currentTimeMillis();

        List<Person> parents = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Random rng = new Random();
            population.add(new Person(maxID, rng.nextBoolean(), parents));
            maxID++;
        }

        nextGen(30, 4);

        System.out.println(population.size());

        long totalTime = System.currentTimeMillis() - startTime;
        float seconds = (float) totalTime /1000;
        System.out.println(seconds);
    }

    public boolean isInbred(){
        for (Person person : population){
            if (person.parents.getFirst().isBrother(person.parents.getLast())){
                return true;
            }
        }
        return false;
    }

    public static void nextGen(int gens, int children) {
        for (int i = 0; i < gens; i++) {
            System.out.println("GENERATING GEN" + i);
            long genStartTime = System.currentTimeMillis();

            divide();

            for (Couple couple : couples){
                for (int j = 0; j < children; j++) {
                    couple.reproduce();
                }
            }

            couples.clear();

            generations.add(population);
            population.clear();
            population.addAll(newborns);
            newborns.clear();

            System.out.println(population.size());

            long genGenerationTime = System.currentTimeMillis() - genStartTime;
            float genSeconds = (float) genGenerationTime /1000;
            float genMinutes = genSeconds/60 > 0.001 ? genSeconds/60 : 0;
            System.out.println("GENERATED GEN" + i + " in " + genSeconds + " seconds (" + genMinutes + " minutes)");
        }
    }

    public static void divide(){
        if (population.size() < 100000){
            couples.addAll(coupleUp(population));
            return;
        }

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        List<Callable<List<Couple>>> tasks = new ArrayList<>();

        List<List<Person>> personLists = new ArrayList<>();

        cores = 50000; //Cambiar para aumentar la velocidad de generacion. 50000 ofrece la mejor velocidad sin impactar la generacion
        int range = population.size()/cores;
        for (int i = 0; i < cores; i++) {
            personLists.add(new ArrayList<>());
            for (int j = range*i; j < range*(i+1); j++) {
                personLists.get(i).add(population.get(j));
            }
        }

        int remaining = population.size()-(range*cores);
        for (int i = 0; i < remaining; i++) {
            personLists.getLast().add(population.get((range*cores)+i));
        }

        for (int i = 0; i < cores; i++) {
            int finalI = i;
            tasks.add(() -> coupleUp(personLists.get(finalI)));
        }

        List<Future<List<Couple>>> results;

        try {
            results = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            for (Future<List<Couple>> listFuture : results){
                couples.addAll(listFuture.get());
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Couple> coupleUp(List<Person> peopleList) {
        List<Couple> coupleList = new ArrayList<>();
        List<Person> males = new ArrayList<>();
        List<Person> females = new ArrayList<>();

        for (Person person : peopleList){
            if(person.sex){
                males.add(person);
                continue;
            }
            females.add(person);
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
                coupleList.add(new Couple(moreFirst, less.get(indexRemove)));
                less.remove(indexRemove);
            }
            more.removeFirst();
        }

        return coupleList;
    }

    public static int getMaxID(){
        int maxId = maxID;
        maxID++;

        return maxId;
    }

    public static void printGenerations(){
        for (int i = 0; i < generations.size(); i++) {
            StringBuilder msg = new StringBuilder();
            msg.append("GENERATION ").append(i).append("\n");
            for (Person person : generations.get(i)){
                msg.append(person.toString()).append("\n");
            }
            System.out.println(msg);
        }
    }
}
