package com.upgrad.tms.administration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface ConceptsLearned {
    enum DifficultyLevel {EASY, MEDIUM, HARD}
    String[] concepts();
    DifficultyLevel difficultyLevel() default DifficultyLevel.MEDIUM;
}
