package org.net9.redbud.util;

import java.util.Comparator;

import org.net9.redbud.storage.hibernate.category.Category;
import org.net9.redbud.storage.hibernate.depts.Depts;
import org.net9.redbud.storage.hibernate.organs.Organs;
import org.net9.redbud.storage.hibernate.posts.Posts;

public final class ComparatorUtils {
	public static class PostComparator implements Comparator<Object> {
		public int compare(Object ob1, Object ob2) {
			Posts post1 = (Posts) ob1;
			Posts post2 = (Posts) ob2;
			if (Integer.parseInt(post1.getCode()) < Integer.parseInt(post2
					.getCode())) {
				return -1;
			} else if (Integer.parseInt(post1.getCode()) == Integer
					.parseInt(post2.getCode())) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public static class DeptComparator implements Comparator<Object> {
		public int compare(Object ob1, Object ob2) {
			Depts dept1 = (Depts) ob1;
			Depts dept2 = (Depts) ob2;
			if (Integer.parseInt(dept1.getCode()) < Integer.parseInt(dept2
					.getCode())) {
				return -1;
			} else if (Integer.parseInt(dept1.getCode()) == Integer
					.parseInt(dept2.getCode())) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public static class OrganComparator implements Comparator<Object> {
		public int compare(Object ob1, Object ob2) {
			Organs organ1 = (Organs) ob1;
			Organs organ2 = (Organs) ob2;
			if (Integer.parseInt(organ1.getCode()) < Integer.parseInt(organ2
					.getCode())) {
				return -1;
			} else if (Integer.parseInt(organ1.getCode()) == Integer
					.parseInt(organ2.getCode())) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public static class CatComparator implements Comparator<Object> {
		public int compare(Object ob1, Object ob2) {
			Category cat1 = (Category) ob1;
			Category cat2 = (Category) ob2;
			if (Integer.parseInt(cat1.getCode()) < Integer.parseInt(cat2
					.getCode())) {
				return -1;
			} else if (Integer.parseInt(cat1.getCode()) == Integer
					.parseInt(cat2.getCode())) {
				return 0;
			} else {
				return 1;
			}
		}
	}

}
