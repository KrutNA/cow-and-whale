package com.krutna.animalguessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App implements Runnable {

    private final GuessingGame<String, String> game;

    private final BufferedReader reader;

    public App(final GuessingGame<String, String> game) {
        this.game = game;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) throws IOException {
        var game = new GuessingGame<String, String>();
        var livesOnLand = game.getAndPutTrait("lives on land");

        game.putEntity("cat", new HashSet<>() {{ add(livesOnLand); }});
        game.putEntity("whale",  new HashSet<>() {{ add(livesOnLand.getOpposite()); }});

        var application = new App(game);
        application.run();
    }

    @Override
    public void run() {
        var toContinue = true;
        while (toContinue) {
            System.out.println("Make a guess about an animal, and I'll try to guess.");

            var traits = readTraits();

            var animals = game.findEntities(traits);
            switch (animals.size()) {
                case 0 -> processOnZeroAnimals(traits);
                case 1 -> processOnOneAnimal(traits, animals.get(0));
                default -> processOnMultipleAnimals(traits, animals);
            }

            { // for debug
                System.out.println(game.getKnownTraits());
                System.out.println(game);
            }

            System.out.print("Continue? [yes, no]: ");
            toContinue = readLine().equals("yes");
        }
    }

    private Set<Trait<String>> readTraits() {
        var traits = new HashSet<Trait<String>>();

        for (var trait : game.getKnownTraits()) {
            System.out.printf("Does this animal %s? [yes, no]: ", trait);

            var isDirect = readLine().equals("yes");
            var knownTrait = game.getTrait(trait, isDirect);
            traits.add(knownTrait);
        }

        return traits;
    }

    private void processOnZeroAnimals(Set<Trait<String>> traits) {
        System.out.print("There is no animal with this traits, name your one: ");
        var animal = readLine();
        game.putEntity(animal, traits);
    }

    private void processOnOneAnimal(Set<Trait<String>> traits, String animal) {
        System.out.printf("There is one found animal: %s. Is this the riddled one? [yes, no]: ", animal);

        var isRiddled = readLine().equals("yes");
        if (isRiddled) {
            game.putEntity(animal, traits);
        } else {
            System.out.print("Which animal did you riddle? ");
            var riddledAnimal = readLine();

            var trait = readDifferentTraitTo(riddledAnimal, traits);
            game.addTrait(animal, trait.getOpposite());
        }
    }

    private void processOnMultipleAnimals(Set<Trait<String>> traits, List<String> animals) {
        System.out.printf("There is multiple guessed animals: %s\n", animals);

        System.out.print("Name your one: ");
        var newAnimal = readLine();
        animals.remove(newAnimal);

        var trait = readDifferentTraitTo(newAnimal, traits);

        var oppositeTrait = trait.getOpposite();
        for (var animal : animals) {
            game.addTrait(animal, oppositeTrait);
        }
    }

    private Trait<String> readDifferentTraitTo(String animal, Set<Trait<String>> traits) {
        System.out.print("How it's different from? ");

        var trait = game.getAndPutTrait(readLine());
        traits.add(trait);
        game.putEntity(animal, traits);

        return trait;
    }

    private String readLine() {
        try {
            return reader.readLine().trim().toLowerCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
