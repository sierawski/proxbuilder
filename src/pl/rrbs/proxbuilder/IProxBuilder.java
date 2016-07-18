package pl.rrbs.proxbuilder;

public interface IProxBuilder<T> {
	T build();
	void reset();
}
