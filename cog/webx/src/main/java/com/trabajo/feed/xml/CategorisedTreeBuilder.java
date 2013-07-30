package com.trabajo.feed.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trabajo.jpa.JPACategorised;
import com.trabajo.utils.Strings;

public class CategorisedTreeBuilder<T extends JPACategorised<T>> {

	private StringBuilder sb = new StringBuilder();
	private String rootLabel;

	public CategorisedTreeBuilder(List<T> results, String rootLabel) {
		
		this.rootLabel=rootLabel;
		
		Map<String, INode<T>> map = new HashMap<>();

		INode<T> root = new RootNode(map);
		map.put("___root", root);

		for (T j : results) {
			StringBuilder v = new StringBuilder();
			v.append(j.getCategory());
			v.append('.');
			v.append(j.getName());

			if (map.get(v.toString()) == null) {
				String path = v.toString();
				root.createChild(path, map);

				INode<T> n = map.get(path);
				n.setDef(j);
			}

		}

		for (T j : results) {
			INode<T> n = map.get(j.getCategory());
			// TODO enrich
		}

		map.get("___root").serialize(sb);
	}

	@Override
	public String toString() {
		return sb.toString();
	}

	interface INode<T> {
		void createChild(String name, Map<String, INode<T>> map);

		void setDef(T j);

		void serialize(StringBuilder sb);

		List<INode<T>> kids();

		String comp();
	}

	class RootNode extends Node<T> {

		RootNode(Map<String, INode<T>> map) {
			this.name = "___root";
			map.put(this.name, this);
		}

		@Override
		public void serialize(StringBuilder sb) {
			sb.append("<?xml version='1.0' encoding='UTF-8'?><tree id=\"0\">");
			sb.append("<item text=\"");
			sb.append(rootLabel);
			sb.append("\" id=\"ignore\" open=\"1\" child=\"1\">");
			if (kids() != null) {
				for (INode<T> n : kids()) {
					n.serialize(sb);
				}
			}else {
				sb.append("<item id=\"ignoretoo\" text=\"(nothing to display)\"/>");
			}
			sb.append("</item></tree>");
		}
	}

	class Node<U> implements INode<U> {

		String name;
		String comp;
		INode<U> parent;
		List<INode<U>> next;
		private U def;

		Node() {
			super();
		}

		public List<INode<U>> kids() {
			return next;
		}

		public void createChild(String name, Map<String, INode<U>> map) {

			if (kids() == null) {
				next = new ArrayList<>();
			}

			if (name.indexOf('.') == -1) {
				for (INode<U> n : kids()) {
					if (n.comp().equals(name)) {
						return;
					}
				}

				Node<U> c = new Node<U>();
				c.parent = this;
				c.comp = name;
				c.name = this.name.equals("___root") ? name : this.name + "."
						+ name;
				next.add(c);
				map.put(c.name, c);

			} else {

				String fcomp = Strings.beforeFirst(name, '.');

				for (INode<U> n : kids()) {
					if (n.comp().equals(fcomp)) {
						n.createChild(Strings.afterFirst(name, '.'), map);
						return;
					}
				}

				Node<U> c = new Node<U>();
				c.parent = this;
				c.comp = fcomp;

				if (this.name.equals("___root"))
					c.name = fcomp;
				else
					c.name = this.name + "." + fcomp;

				next.add(c);
				map.put(c.name, c);
				c.createChild(Strings.afterFirst(name, '.'), map);
			}
		}

		@Override
		public void serialize(StringBuilder sb) {
			sb.append("<item text=\"" + this.comp + "\" id=\"" + this.name
					+ "\"" + getImage() + getChild()
					+ ">"+userData());
			if (kids() != null) {
				for (INode<U> n : kids()) {
					n.serialize(sb);
				}
			}
			sb.append("</item>");
		}

		private String getChild() {
			if (def == null)
				return " child=\"0\"";
			else
				return " child=\"1\"";
		}

		private String getImage() {
			if (def != null)
				return " im0=\"process.gif\"";
			else
				return "";
		}

		@Override
		public String comp() {
			return comp;
		}

		@Override
		public void setDef(U j) {
			this.def = j;

		}
		
		private String userData() {
			return "";
		}
	}
}
