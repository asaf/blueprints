package com.tinkerpop.blueprints.pgm.util.wrappers.partition.util;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.blueprints.pgm.Index;
import com.tinkerpop.blueprints.pgm.util.wrappers.partition.PartitionGraph;
import com.tinkerpop.blueprints.pgm.util.wrappers.partition.PartitionIndex;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PartitionIndexIterable<T extends Element> implements Iterable<Index<T>> {

    protected Iterable<Index<T>> iterable;
    private final PartitionGraph graph;

    public PartitionIndexIterable(final Iterable<Index<T>> iterable, final PartitionGraph graph) {
        this.iterable = iterable;
        this.graph = graph;
    }

    public Iterator<Index<T>> iterator() {
        return new PartitionIndexIterator();
    }

    private class PartitionIndexIterator implements Iterator<Index<T>> {
        private final Iterator<Index<T>> itty = iterable.iterator();

        public void remove() {
            this.itty.remove();
        }

        public boolean hasNext() {
            return this.itty.hasNext();
        }

        public Index<T> next() {
            return new PartitionIndex<T>(this.itty.next(), graph);
        }
    }

}
