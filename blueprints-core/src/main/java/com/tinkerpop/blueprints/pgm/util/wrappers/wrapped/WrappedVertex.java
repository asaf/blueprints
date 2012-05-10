package com.tinkerpop.blueprints.pgm.util.wrappers.wrapped;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Query;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.DefaultQuery;
import com.tinkerpop.blueprints.pgm.util.wrappers.wrapped.util.WrappedEdgeIterable;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class WrappedVertex extends WrappedElement implements Vertex {

    public WrappedVertex(final Vertex rawVertex) {
        super(rawVertex);
    }

    public Iterable<Edge> getOutEdges(final String... labels) {
        return new WrappedEdgeIterable(((Vertex) this.rawElement).getOutEdges(labels));
    }

    public Iterable<Edge> getInEdges(final String... labels) {
        return new WrappedEdgeIterable(((Vertex) this.rawElement).getInEdges(labels));
    }

    public Query query() {
        return new DefaultQuery(this);
    }

    public Vertex getBaseVertex() {
        return (Vertex) this.rawElement;
    }
}
