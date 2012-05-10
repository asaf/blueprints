package com.tinkerpop.blueprints.pgm.util.wrappers.event;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.StringFactory;
import com.tinkerpop.blueprints.pgm.util.wrappers.WrapperGraph;
import com.tinkerpop.blueprints.pgm.util.wrappers.event.listener.GraphChangedListener;
import com.tinkerpop.blueprints.pgm.util.wrappers.event.util.EventEdgeIterable;
import com.tinkerpop.blueprints.pgm.util.wrappers.event.util.EventVertexIterable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An EventGraph is a wrapper to existing Graph implementations and provides for graph events to be raised
 * to one or more listeners on changes to the Graph.  Notifications to the listeners occur for the
 * following events: new vertex/edge, vertex/edge property changed, vertex/edge property removed,
 * vertex/edge removed.
 * <p/>
 * The limiting factor to events being raised is related to out-of-process functions changing graph elements.
 * <p/>
 * To gather events from EventGraph, simply provide an implementation of the {@link GraphChangedListener} to
 * the EventGraph by utilizing the addListener method.  EventGraph allows the addition of multiple GraphChangedListener
 * implementations.  Each listener will be notified in the order that it was added.
 *
 * @author Stephen Mallette
 */
public class EventGraph<T extends Graph> implements Graph, WrapperGraph<T> {

    protected final T baseGraph;

    protected final List<GraphChangedListener> graphChangedListeners = new ArrayList<GraphChangedListener>();

    public EventGraph(final T baseGraph) {
        this.baseGraph = baseGraph;
    }

    public void removeAllListeners() {
        this.graphChangedListeners.clear();
    }

    public void addListener(final GraphChangedListener listener) {
        this.graphChangedListeners.add(listener);
    }

    public Iterator<GraphChangedListener> getListenerIterator() {
        return this.graphChangedListeners.iterator();
    }

    public void removeListener(final GraphChangedListener listener) {
        this.graphChangedListeners.remove(listener);
    }

    protected void onVertexAdded(final Vertex vertex) {
        for (GraphChangedListener listener : this.graphChangedListeners) {
            listener.vertexAdded(vertex);
        }
    }

    protected void onVertexRemoved(final Vertex vertex) {
        for (GraphChangedListener listener : this.graphChangedListeners) {
            listener.vertexRemoved(vertex);
        }
    }

    protected void onEdgeAdded(final Edge edge) {
        for (GraphChangedListener listener : this.graphChangedListeners) {
            listener.edgeAdded(edge);
        }
    }

    protected void onEdgeRemoved(final Edge edge) {
        for (GraphChangedListener listener : this.graphChangedListeners) {
            listener.edgeRemoved(edge);
        }
    }

    /**
     * Raises a vertexAdded event.
     */
    public Vertex addVertex(final Object id) {
        final Vertex vertex = this.baseGraph.addVertex(id);
        if (vertex == null) {
            return null;
        } else {
            this.onVertexAdded(vertex);
            return new EventVertex(vertex, this.graphChangedListeners);
        }
    }

    public Vertex getVertex(final Object id) {
        final Vertex vertex = this.baseGraph.getVertex(id);
        if (vertex == null) {
            return null;
        } else {
            return new EventVertex(vertex, this.graphChangedListeners);
        }
    }

    /**
     * Raises a vertexRemoved event.
     */
    public void removeVertex(final Vertex vertex) {
        Vertex vertexToRemove = vertex;
        if (vertex instanceof EventVertex) {
            vertexToRemove = ((EventVertex) vertex).getBaseVertex();
        }

        this.baseGraph.removeVertex(vertexToRemove);
        this.onVertexRemoved(vertex);
    }

    public Iterable<Vertex> getVertices() {
        return new EventVertexIterable(this.baseGraph.getVertices(), this.graphChangedListeners);
    }

    public Iterable<Vertex> getVertices(final String key, final Object value) {
        return new EventVertexIterable(this.baseGraph.getVertices(key, value), this.graphChangedListeners);
    }

    /**
     * Raises an edgeAdded event.
     */
    public Edge addEdge(final Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
        Vertex outVertexToSet = outVertex;
        if (outVertex instanceof EventVertex) {
            outVertexToSet = ((EventVertex) outVertex).getBaseVertex();
        }

        Vertex inVertexToSet = inVertex;
        if (inVertex instanceof EventVertex) {
            inVertexToSet = ((EventVertex) inVertex).getBaseVertex();
        }

        final Edge edge = this.baseGraph.addEdge(id, outVertexToSet, inVertexToSet, label);
        if (edge == null) {
            return null;
        } else {
            this.onEdgeAdded(edge);
            return new EventEdge(edge, this.graphChangedListeners);
        }
    }

    public Edge getEdge(final Object id) {
        final Edge edge = this.baseGraph.getEdge(id);
        if (edge == null) {
            return null;
        } else {
            return new EventEdge(edge, this.graphChangedListeners);
        }
    }

    /**
     * Raises an edgeRemoved event.
     */
    public void removeEdge(final Edge edge) {
        Edge edgeToRemove = edge;
        if (edge instanceof EventEdge) {
            edgeToRemove = ((EventEdge) edge).getBaseEdge();
        }

        this.baseGraph.removeEdge(edgeToRemove);
        this.onEdgeRemoved(edge);
    }

    public Iterable<Edge> getEdges() {
        return new EventEdgeIterable(this.baseGraph.getEdges(), this.graphChangedListeners);
    }

    public Iterable<Edge> getEdges(final String key, final Object value) {
        return new EventEdgeIterable(this.baseGraph.getEdges(key, value), this.graphChangedListeners);
    }

    public void shutdown() {
        this.baseGraph.shutdown();
    }

    public String toString() {
        return StringFactory.graphString(this, this.baseGraph.toString());
    }

    @Override
    public T getBaseGraph() {
        return this.baseGraph;
    }
}
