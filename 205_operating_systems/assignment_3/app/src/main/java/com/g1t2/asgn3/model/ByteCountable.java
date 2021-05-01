package com.g1t2.asgn3.model;

/**
 * Objects implementing ByteCountable can provide custom behaviour for defining its computed memory
 * footprint. This is especially useful if a stable interface is required to keep track of an
 * object's memory footprint.
 */
public interface ByteCountable {
    /**
     * Get the amount of space the object occupies in memory
     * @return The size of the object in bytes
     */
    long getByteCount();
}
