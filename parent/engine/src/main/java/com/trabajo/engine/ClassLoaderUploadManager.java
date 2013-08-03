package com.trabajo.engine;

//TODO - have grid contain a switch to denote whether or not
// a classloader jar contains cog annotated classes

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.TreeMap;

import com.trabajo.DefinitionVersion;
import com.trabajo.ValidationException;
import com.trabajo.utils.XMLSBHelper;
import com.trabajo.values.Category;
import com.trabajo.values.Description;

public class ClassLoaderUploadManager implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<DefinitionVersion, ClassLoaderUpload> map = new HashMap<>();

	public synchronized void prioritise(int sequence, String dv, boolean up) {
		ClassLoaderUpload clu = map.get(DefinitionVersion.parse(dv));

		if (clu == null) {
			return;
		}

		clu.prioritise(sequence, up);
		System.out.println(clu);
	}

	public synchronized ClassLoaderUpload get(DefinitionVersion dv) {
		return map.get(dv);
	}

	public synchronized void remove(DefinitionVersion dv) {

		System.out.println(map.remove(dv));
	}

	public synchronized void create(DefinitionVersion dv, String cat, String desc) throws ValidationException {
		System.out.println(edit(dv, new Category(cat), new Description(desc, 255)));
	}

	public synchronized ClassLoaderUpload.JarUpload addUpload(Path target, DefinitionVersion dv, boolean isJarAnnotated, String originalFileName, Category cat,
			Description desc) throws ValidationException {
		return edit(dv, cat, desc).addUpload(target, originalFileName, isJarAnnotated);
	}

	public synchronized void delete(String dv, int sequence) {
		ClassLoaderUpload clu = map.get(DefinitionVersion.parse(dv));
		if (clu == null) {
			return;
		}
		clu.delete(sequence);
		System.out.println(clu);
	}

	public synchronized ClassLoaderUpload edit(DefinitionVersion dv, Category cat, Description desc) {
		ClassLoaderUpload clu = map.get(dv);

		if (clu == null) {
			clu = new ClassLoaderUpload(dv, cat, desc);
			map.put(clu.getVersion(), clu);
		}

		clu.setCategory(cat);
		clu.setDesc(desc);
		return clu;
	}

	public static class ClassLoaderUpload implements Serializable {
		private static final long serialVersionUID = 1L;

		public static class JarUpload implements Comparable<JarUpload>, Serializable {
			private static final long serialVersionUID = 1L;

			@Override
			public int hashCode() {
				return originalFileName.hashCode();
			}

			@Override
			public boolean equals(Object other) {
				return originalFileName.equals(other);
			}

			public int sequence;
			public boolean isJarAnnotated;
			public String originalFileName;
			public Path target;
			public Properties props;

			public JarUpload(int sequence, boolean isJarAnnotated, String originalFileName, Path target) {
				super();
				this.sequence = sequence;
				this.isJarAnnotated = isJarAnnotated;
				this.originalFileName = originalFileName;
				this.target = target;
			}

			@Override
			public int compareTo(JarUpload o) {
				if (this.sequence > o.sequence) {
					return 1;
				} else if (this.sequence == o.sequence) {
					return 0;
				} else {
					return -1;
				}
			}

			public URL url() {
				try {
					return target.toUri().toURL();
				} catch (MalformedURLException e) {
					throw new RuntimeException(e); // will never happen: already validated
				}
			}

			public String toString() {
				return "" + sequence + " " + originalFileName;
			}

			public String asClDHXRow() {
				StringBuilder sb = new StringBuilder();
				XMLSBHelper xml = new XMLSBHelper(sb);
				String seq = String.valueOf(sequence);
				xml.elm("row")
						.attr("id", seq)
						.closeTag()
						.elm("cell")
						.closeTag()
						.append(String.valueOf(sequence))
						.closeElm("cell")
						.elm("cell")
						.closeTag()
						.append(originalFileName)
						.closeElm("cell")
						.elm("cell")
						.closeTag()
						.append(
								"<![CDATA[<a href=\"javascript:FormAdPrEdClNwJo.obj.up(" + seq
										+ ")\"><img style=\"margin-top: 2px; width: 16px; height: 16px\" src=\"img/up.png\"/></a>]]>")
						.closeElm("cell")
						.elm("cell")
						.closeTag()
						.append(
								"<![CDATA[<a href=\"javascript:FormAdPrEdClNwJo.obj.down(" + seq
										+ ")\"><img style=\"margin-top: 2px; width: 16px; height: 16px\" src=\"img/down.png\"/></a>]]>")
						.closeElm("cell")
						.elm("cell")
						.closeTag()
						.append(
								"<![CDATA[<a href=\"javascript:FormAdPrEdClNwJo.obj.del(" + seq
										+ ")\"><img style=\"margin-top: 2px; width: 16px; height: 16px\" src=\"img/del.png\"/></a>]]>").closeElm("cell").closeElm("row");
				return sb.toString();
			}

			public String asSvDHXRow() {
				StringBuilder sb = new StringBuilder();
				XMLSBHelper xml = new XMLSBHelper(sb);
				String seq = String.valueOf(sequence);
				xml.elm("row")
						.attr("id", seq)
						.closeTag()
						.elm("cell")
						.closeTag()
						.append(String.valueOf(sequence))
						.closeElm("cell")
						.elm("cell")
						.closeTag()
						.append(originalFileName)
						.closeElm("cell")
						.elm("cell")
						.closeTag()
						.append(
								"<![CDATA[<a href=\"javascript:FormAdPrEdSvNwJo.obj.up(" + seq
										+ ")\"><img style=\"margin-top: 2px; width: 16px; height: 16px\" src=\"img/up.png\"/></a>]]>")
						.closeElm("cell")
						.elm("cell")
						.closeTag()
						.append(
								"<![CDATA[<a href=\"javascript:FormAdPrEdSvNwJo.obj.down(" + seq
										+ ")\"><img style=\"margin-top: 2px; width: 16px; height: 16px\" src=\"img/down.png\"/></a>]]>")
						.closeElm("cell")
						.elm("cell")
						.closeTag()
						.append(
								"<![CDATA[<a href=\"javascript:FormAdPrEdSvNwJo.obj.del(" + seq
										+ ")\"><img style=\"margin-top: 2px; width: 16px; height: 16px\" src=\"img/del.png\"/></a>]]>").closeElm("cell").closeElm("row");
				return sb.toString();
			}
		}

		public TreeMap<Integer, JarUpload> jarSequenceMap = new TreeMap<>();
		public DefinitionVersion version;
		public Category category;
		public Description desc;

		public Description getDesc() {
			return desc;
		}

		public void setCategory(Category cat) {
			this.category = cat;
		}

		public void setDesc(Description desc) {
			this.desc = desc;
		}

		public void delete(int sequence) {
			jarSequenceMap.remove(sequence);
			int i = 1;
			try {
				for (Integer key = jarSequenceMap.firstKey(); key != null; key = jarSequenceMap.higherKey(key)) {
					JarUpload ju = jarSequenceMap.get(key);
					jarSequenceMap.remove(ju.sequence);
					ju.sequence = i++;
					jarSequenceMap.put(ju.sequence, ju);
				}
			} catch (NoSuchElementException e) {
				// ignore
			}
		}

		public ClassLoaderUpload(DefinitionVersion dv, Category cat, Description desc) {
			this.version = dv;
			this.category = cat;
			this.desc = desc;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();

			try {
				for (Integer key = jarSequenceMap.firstKey(); key != null; key = jarSequenceMap.higherKey(key)) {
					sb.append(jarSequenceMap.get(key));
				}
			} catch (NoSuchElementException e) {
				// ignore
			}
			return sb.toString();
		}

		public void prioritise(int sequence, boolean up) {
			int inc = up ? -1 : 1;

			synchronized (jarSequenceMap) {
				if (jarSequenceMap.containsKey(sequence) && jarSequenceMap.containsKey(sequence + inc)) {
					JarUpload that = jarSequenceMap.get(sequence);
					JarUpload other = jarSequenceMap.get(sequence + inc);

					jarSequenceMap.remove(that.sequence);
					jarSequenceMap.remove(other.sequence);

					int tmp = that.sequence;
					that.sequence = other.sequence;
					other.sequence = tmp;

					jarSequenceMap.put(that.sequence, that);
					jarSequenceMap.put(other.sequence, other);
				}
			}
		}

		public DefinitionVersion getVersion() {
			return version;
		}

		public JarUpload addUpload(Path target, String originalFileName, boolean isJarAnnotated) {
			int nextKey = 0;

			synchronized (jarSequenceMap) {
				try {
					nextKey = jarSequenceMap.lastKey() + 1;
				} catch (NoSuchElementException e) {
					nextKey = 1;
				}
				JarUpload ju = new JarUpload(nextKey, isJarAnnotated, originalFileName, target);

				if (jarSequenceMap.containsValue(ju)) {
					nextKey = jarSequenceMap.remove(ju).sequence;
					ju = new JarUpload(nextKey, isJarAnnotated, originalFileName, target);
				}
				jarSequenceMap.put(nextKey, ju);
				return ju;
			}
		}

		public Category getCatgeory() {
			return category;
		}

		public List<JarUpload> getJars() {
			final List<JarUpload> entries = new ArrayList<>(jarSequenceMap.size());
			synchronized(jarSequenceMap) {
				try{
					for (Integer key = jarSequenceMap.firstKey(); key != null; key = jarSequenceMap.higherKey(key)) {
						entries.add(jarSequenceMap.get(key));
					}
				}catch(NoSuchElementException e){
					//ignore
				}
			}
			return entries;
		}

		public URL[] getURLs() {
			synchronized (jarSequenceMap) {
				if (jarSequenceMap.size() == 0)
					return new URL[0];

				URL[] urls = new URL[jarSequenceMap.size()];

				int i = 0;
				for (Integer key = jarSequenceMap.firstKey(); key != null; key = jarSequenceMap.higherKey(key)) {
					JarUpload ju = jarSequenceMap.get(key);
					try {
						urls[i++] = ju.target.toUri().toURL();
					} catch (MalformedURLException e) {
						throw new RuntimeException(e); // will never happen: already
																						// validated
					}
				}
				return urls;
			}
		}

		public String[] getOriginalNames() {
			if (jarSequenceMap.size() == 0)
				return new String[0];

			String[] names = new String[jarSequenceMap.size()];

			int i = 0;
			for (Integer key = jarSequenceMap.firstKey(); key != null; key = jarSequenceMap.higherKey(key)) {
				JarUpload ju = jarSequenceMap.get(key);
				names[i++] = ju.originalFileName;
			}
			return names;
		}

		public String getJarOrder(String type) {
			StringBuilder sb = new StringBuilder();
			XMLSBHelper xml = new XMLSBHelper(sb);
			xml.elm("rows").closeTag();

			for (JarUpload ju : getJars()) {
				switch (type) {
				case "Cl":
					sb.append(ju.asClDHXRow());
					break;
				case "Sv":
					sb.append(ju.asSvDHXRow());
					break;
				}
			}

			xml.closeElm("rows");
			System.out.println(sb.toString());
			return sb.toString();
		}
	}

	public String getJarOrder(DefinitionVersion version, String type) {
		return map.get(version).getJarOrder(type);
	}

}
