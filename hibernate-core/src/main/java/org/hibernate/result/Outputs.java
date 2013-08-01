/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2013, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.result;

/**
 * Represents the outputs of executing a JDBC statement accounting for mixing of result sets and update counts
 * hiding the complexity (IMO) of how this is exposed in the JDBC API.
 * <p/>
 * The outputs are exposed as a group of {@link Output} objects, each representing a single result set or update count.
 * Conceptually, Result presents those Returns as an iterator.
 *
 * @author Steve Ebersole
 */
public interface Outputs {
	/**
	 * Retrieve the current Output object.
	 *
	 * @return The current Output object.  Can be {@code null}
	 */
	public Output getCurrentOutput();

	/**
	 * Are there any more Output objects associated with {@code this}?
	 *
	 * @return {@code true} means there are more Output objects available via {@link #getNextOutput()}; {@code false}
	 * indicates that calling {@link #getNextOutput()} will certainly result in an exception.
	 */
	public boolean hasMoreOutput();

	/**
	 * Retrieve the next return
	 *
	 * @return The next return.
	 *
	 * @throws NoMoreReturnsException Thrown if there are no more returns associated with this Result, as would
	 * have been indicated by a {@code false} return from {@link #hasMoreOutput()}.
	 */
	public Output getNextOutput() throws NoMoreReturnsException;
}