package au.ambition.portfoliowtcalc.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amol Kshirsagar
 *
 */
public class Fund<T> {

	private T data = null;

	private List<Fund<T>> children = new ArrayList<>();

	private Fund<T> parent = null;

	public Fund(T data) {
		this.data = data;
	}

	public Fund<T> addChild(Fund<T> child) {
		child.setParent(this);
		this.children.add(child);
		return child;
	}

	public void addChildren(List<Fund<T>> children) {
		children.forEach(each -> each.setParent(this));
		this.children.addAll(children);
	}

	public List<Fund<T>> getChildren() {
		return children;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	private void setParent(Fund<T> parent) {
		this.parent = parent;
	}

	public Fund<T> getParent() {
		return parent;
	}

}