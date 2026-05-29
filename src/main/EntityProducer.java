package main;

import entities.Entity;

import java.lang.reflect.Constructor;

public record EntityProducer(Constructor<? extends Entity> constructor, float x, float y) {
}
