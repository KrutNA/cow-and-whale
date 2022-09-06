package com.krutna.animalguessing;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents class with self-learning based on new traits and objects
 * @param <E> type of entity
 * @param <T> type of traits parameter
 */
public class GuessingGame<E, T> {

    private final Map<E, Set<Trait<T>>> entitiesWithKnownTraits = new HashMap<>();
    /**
     * Traits are cached, so we'll use already existing trait instances if possible. <br/>
     * Also equals will stop on "==" on set's elements comparing.
     */
    private final Map<T, Trait<T>> knownTraits = new HashMap<>();

    public GuessingGame() {}

    public Set<T> getKnownTraits() { return knownTraits.keySet(); }

    /**
     * Get cached trait directly. <br/>
     * Use only if you sure there is a trait with specified name.
     * @param traitName name to get trait
     * @param isDirect is this trait a direct or opposite
     * @return extracted trait
     */
    public Trait<T> getTrait(T traitName, boolean isDirect) {
        var knownTrait = knownTraits.get(traitName);
        return isDirect ? knownTrait : knownTrait.getOpposite();
    }

    /**
     * Get trait with specified name. <br/>
     * If there is no trait it will be created.
     * @param traitName name to get trait
     * @return extracted or newly created trait
     */
    public Trait<T> getAndPutTrait(T traitName) {
        var trait = knownTraits.putIfAbsent(traitName, new Trait<>(traitName));
        return trait == null ? knownTraits.get(traitName) : trait;
    }

    /**
     * Add trait directly to specified entity. <br/>
     * Could throw NPE of there is no entity.
     * @param entity entity for traits
     * @param trait traits of entity
     */
    public void addTrait(E entity, Trait<T> trait) {
        entitiesWithKnownTraits.get(entity).add(trait);
    }

    public void addTrait(E entity, T traitName, boolean isDirect) {
        var knownTrait = getAndPutTrait(traitName);
        if (!isDirect) knownTrait = knownTrait.getOpposite();
        addTrait(entity, knownTrait);
    }

    /**
     * Save entity with specified set of traits.
     * @param entity entity to save
     * @param traits new traits of entity
     */
    public void putEntity(E entity, Set<Trait<T>> traits) {
        entitiesWithKnownTraits.put(entity, traits);
    }

    /**
     * Get all entities which traits are subset of provided traits set.
     * @param traits traits to search for
     * @return mutable list of found entities
     */
    public List<E> findEntities(Set<Trait<T>> traits) {
        return entitiesWithKnownTraits.entrySet()
                .stream()
                .filter(entitiesWithTraits -> traits.containsAll(entitiesWithTraits.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString() {
        return entitiesWithKnownTraits.toString();
    }
}
