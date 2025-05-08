package io.github.mortuusars.thief.world;

import java.util.Optional;

public enum PotentialCrime {
    NONE(Optional.empty()),
    LIGHT(Optional.of(Crime.LIGHT)),
    MEDIUM(Optional.of(Crime.MEDIUM)),
    HEAVY(Optional.of(Crime.HEAVY));

    private final Optional<Crime> offence;

    PotentialCrime(Optional<Crime> offence) {
        this.offence = offence;
    }

    public Optional<Crime> getCrime() {
        return offence;
    }
}
