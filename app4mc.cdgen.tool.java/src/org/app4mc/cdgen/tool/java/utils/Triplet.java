package org.app4mc.cdgen.tool.java.utils;

// Triplet class
public class Triplet<U, V, T> {
	public final U first; // first field of a Triplet
	public final V second; // second field of a Triplet
	public final T third; // third field of a Triplet

	// Constructs a new Triplet with the given values
	public Triplet(final U first, final V second, final T third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	@Override
	public boolean equals(final Object o) {
		/* Checks specified object is "equal to" current object or not */

		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Triplet triplet = (Triplet) o;

		// call equals() method of the underlying objects
		if (!this.first.equals(triplet.first) || !this.second.equals(triplet.second)
				|| !this.third.equals(triplet.third)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		/*
		 * Computes hash code for an object by using hash codes of the
		 * underlying objects
		 */

		int result = this.first.hashCode();
		result = 31 * result + this.second.hashCode();
		result = 31 * result + this.third.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "(" + this.first + ", " + this.second + ", " + this.third + ")";
	}

	// Factory method for creating a Typed immutable instance of Triplet
	public static <U, V, T> Triplet<U, V, T> of(final U a, final V b, final T c) {
		return new Triplet<>(a, b, c);
	}
}