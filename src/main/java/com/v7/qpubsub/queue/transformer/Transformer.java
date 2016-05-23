package com.v7.qpubsub.queue.transformer;

public interface Transformer<T> {

	public T transform(T message);
}
