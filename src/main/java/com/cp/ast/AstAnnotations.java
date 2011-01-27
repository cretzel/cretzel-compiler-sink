package com.cp.ast;

import java.util.HashMap;
import java.util.Map;

import com.cp.ast.nodes.AstNode;

public class AstAnnotations {

	public enum AnnotationType {
		NUMBER_OF_VARIABLES,
		VARIABLE_NUMBER;

	}

	private Map<AstNode, Map<AnnotationType, Object>> annotations;

	public AstAnnotations() {
		annotations = new HashMap<AstNode, Map<AnnotationType, Object>>();
	}

	public void set(AstNode node, AnnotationType type, Object value) {
		Map<AnnotationType, Object> entry = annotations.get(node);
		if (entry == null) {
			entry = new HashMap<AnnotationType, Object>();
			annotations.put(node, entry);
		}
		entry.put(type, value);
	}

	public Object get(AstNode node, AnnotationType type) {
		Map<AnnotationType, Object> entry = annotations.get(node);
		if (entry == null) {
			throw new IllegalStateException("No annotations found for node "
					+ node);
		}

		Object value = entry.get(type);
		if (value == null) {
			throw new IllegalStateException(
					"No annotation value found for node " + node + ", type "
							+ type);
		}

		return value;
	}

	public boolean has(AstNode node, AnnotationType type) {
		Map<AnnotationType, Object> map = annotations.get(node);
		return map != null && map.containsKey(type);
	}
}
