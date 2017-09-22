package model;

import java.util.ArrayList;

/**
 * simple list for the children. Has some convience implementations
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */
public class TreeObjectList<T> extends ArrayList<T> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public TreeObjectList() {
		super();
	}

	@Override // only looks if the List contains an Object with value o
	public boolean contains(final Object o) {
		String s = (String) o;
		for (int i = 0; i < this.size(); i++) {
			TreeObject p = (TreeObject) this.get(i);
			if (p.getValue().equals(s)) {
				return true;
			}
		}
		return false;
	}

	//
	public TreeObject getObject(final String v) {
		for (int i = 0; i < this.size(); i++) {
			TreeObject p = (TreeObject) this.get(i);
			if (p.getValue().equals(v)) {
				return p;
			}
		}
		return new TreeObject("2", null, 2, 0);
	}

	//
	public int indexOf(final String v) {
		for (int i = 0; i < this.size(); i++) {
			TreeObject p = (TreeObject) this.get(i);
			if (p.getValue().equals(v)) {
				return i;
			}
		}
		return -1;
	}

}
