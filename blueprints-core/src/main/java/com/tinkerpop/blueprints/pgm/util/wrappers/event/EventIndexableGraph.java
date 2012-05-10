package com.tinkerpop.blueprints.pgm.util.wrappers.event;


import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.blueprints.pgm.Index;
import com.tinkerpop.blueprints.pgm.IndexableGraph;
import com.tinkerpop.blueprints.pgm.Parameter;
import com.tinkerpop.blueprints.pgm.util.wrappers.WrapperGraph;
import com.tinkerpop.blueprints.pgm.util.wrappers.event.util.EventIndexIterable;

import java.util.Set;

/**
 * EventIndexableGraph is merely a proxy to index methods exposing EventGraph methods in the "evented" way. Like the
 * EventGraph it extends from, this graph implementations raise notifications to the listeners for the
 * following events: new vertex/edge, vertex/edge property changed, vertex/edge property removed,
 * vertex/edge removed.
 *
 * @author Stephen Mallette
 */
public class EventIndexableGraph<T extends IndexableGraph> extends EventGraph<T> implements IndexableGraph, WrapperGraph<T> {

    public EventIndexableGraph(final T baseIndexableGraph) {
        super(baseIndexableGraph);
    }

    public void dropIndex(final String name) {
        this.getBaseGraph().dropIndex(name);
    }

    public <T extends Element> Index<T> createIndex(final String indexName, final Class<T> indexClass, final Parameter... indexParameters) {
        return new EventIndex<T>(this.getBaseGraph().createIndex(indexName, indexClass, indexParameters), this.graphChangedListeners);
    }

    public <T extends Element> Index<T> getIndex(final String indexName, final Class<T> indexClass) {
        final Index<T> index = this.baseGraph.getIndex(indexName, indexClass);
        if (null == index)
            return null;
        else
            return new EventIndex<T>(index, this.graphChangedListeners);
    }

    public Iterable<Index<? extends Element>> getIndices() {
        return new EventIndexIterable(this.baseGraph.getIndices(), this.graphChangedListeners);
    }

    public <T extends Element> void dropKeyIndex(String key, Class<T> elementClass) {
        this.baseGraph.dropKeyIndex(key, elementClass);
    }

    public <T extends Element> void createKeyIndex(String key, Class<T> elementClass) {
        this.baseGraph.createKeyIndex(key, elementClass);
    }

    public <T extends Element> Set<String> getIndexedKeys(Class<T> elementClass) {
        return this.baseGraph.getIndexedKeys(elementClass);
    }
}
