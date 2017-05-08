package com.ryan.vo;

import java.util.Map;

public class TreeNode {
	private Long id;
	private Long parent;
	private String text;
	private String icon;
	private boolean children;
	private Map<String, Boolean> state;

	public TreeNode(Long id, Long parent, String text, String icon, boolean children, Map<String, Boolean> state) {
		super();
		this.id = id;
		this.parent = parent;
		this.text = text;
		this.icon = icon;
		this.children = children;
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isChildren() {
		return children;
	}

	public void setChildren(boolean children) {
		this.children = children;
	}

	public Map<String, Boolean> getState() {
		return state;
	}

	public void setState(Map<String, Boolean> state) {
		this.state = state;
	}
}
