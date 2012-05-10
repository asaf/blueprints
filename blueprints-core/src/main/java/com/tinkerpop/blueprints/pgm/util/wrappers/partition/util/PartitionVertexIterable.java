package com.tinkerpop.blueprints.pgm.util.wrappers.partition.util;

import com.tinkerpop.blueprints.pgm.CloseableIterable;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.util.wrappers.partition.PartitionGraph;
import com.tinkerpop.blueprints.pgm.util.wrappers.partition.PartitionVertex;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PartitionVertexIterable implements CloseableIterable<Vertex> {

    private final Iterable<Vertex> iterable;
    private final PartitionGraph graph;


    public PartitionVertexIterable(final Iterable<Vertex> iterable, final PartitionGraph graph) {
        this.iterable = iterable;
        this.graph = graph;
    }

    public Iterator<Vertex> iterator() {
        return new PartitionVertexIterator();
    }

    public void close() {
        if (this.iterable instanceof CloseableIterable) {
            ((CloseableIterable) iterable).close();
        }
    }

    private class PartitionVertexIterator implements Iterator<Vertex> {

        private final Iterator<Vertex> itty = iterable.iterator();
        private PartitionVertex nextVertex;

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            if (null != this.nextVertex) {
                return true;
            }
            while (this.itty.hasNext()) {
                final Vertex vertex = this.itty.next();
                if (graph.isInPartition(vertex)) {
                    this.nextVertex = new PartitionVertex(vertex, graph);
                    return true;
                }
            }
            return false;

        }

        public Vertex next() {
            if (null != this.nextVertex) {
                final PartitionVertex temp = this.nextVertex;
                this.nextVertex = null;
                return temp;
            } else {
                while (this.itty.hasNext()) {
                    final Vertex vertex = this.itty.next();
                    if (graph.isInPartition(vertex)) {
                        return new PartitionVertex(vertex, graph);
                    }
                }
                throw new NoSuchElementException();
            }
        }
    }
}
