package io.github.n4th4not.gravity.api;

import java.util.UUID;

/** Prevents an update for one world or a recycled numeric ID from changing another entity. */
public record GravityIdentity(String world, int id, UUID uuid) {}
