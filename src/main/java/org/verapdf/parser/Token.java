/**
 * This file is part of veraPDF Parser, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Parser is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Parser as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Parser as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Timur Kamalov
 */
public class Token {

	public Type type;
	public Keyword keyword;

	public long integer;
	public double real;

	private StringBuilder token = new StringBuilder();

	//fields specific for pdf/a validation of strings
	private boolean containsOnlyHex = true;
	private long hexCount = 0;

	public void toKeyword() {
		this.type = Type.TT_KEYWORD;
		this.keyword = getKeyword(token.toString());
	}

	public void append(char c) {
		this.token.append(c);
	}

	public String getValue() {
		return new String(getByteValue());
	}

	public byte[] getByteValue() {
		byte[] res = new byte[this.token.length()];
		for (int i = 0; i < token.length(); i++) {
			res[i] = (byte) token.charAt(i);
		}
		return res;
	}

	public void clearValue() {
		this.token.setLength(0);
	}

	public enum Type {
		TT_NONE,
		TT_KEYWORD,
		TT_INTEGER,
		TT_REAL,
		TT_LITSTRING,
		TT_HEXSTRING,
		TT_NAME,
		TT_OPENARRAY,
		TT_CLOSEARRAY,
		TT_OPENDICT,
		TT_CLOSEDICT,
		TT_EOF,
		TT_STARTPROC,
		TT_ENDPROC
	}

	public enum Keyword {
		KW_NONE,
		KW_NULL,
		KW_TRUE,
		KW_FALSE,
		KW_STREAM,
		KW_ENDSTREAM,
		KW_OBJ,
		KW_ENDOBJ,
		KW_R,
		KW_N,
		KW_F,
		KW_XREF,
		KW_STARTXREF,
		KW_TRAILER
	}

	private static final Map<String, Keyword> KEYWORDS = new HashMap<>();

	static {
		KEYWORDS.put("null", Keyword.KW_NULL);
		KEYWORDS.put("true", Keyword.KW_TRUE);
		KEYWORDS.put("false", Keyword.KW_FALSE);
		KEYWORDS.put("stream", Keyword.KW_STREAM);
		KEYWORDS.put("endstream", Keyword.KW_ENDSTREAM);
		KEYWORDS.put("obj", Keyword.KW_OBJ );
		KEYWORDS.put("endobj", Keyword.KW_ENDOBJ);
		KEYWORDS.put("R", Keyword.KW_R );
		KEYWORDS.put("n", Keyword.KW_N);
		KEYWORDS.put("f", Keyword.KW_F);
		KEYWORDS.put("xref", Keyword.KW_XREF);
		KEYWORDS.put("startxref", Keyword.KW_STARTXREF);
		KEYWORDS.put("trailer", Keyword.KW_TRAILER);
		KEYWORDS.put(null, Keyword.KW_NONE);
	}

	public static Keyword getKeyword(final String keyword) {
		return KEYWORDS.get(keyword);
	}

	//GETTERS & SETTERS
	public boolean isContainsOnlyHex() {
		return containsOnlyHex;
	}

	public void setContainsOnlyHex(boolean containsOnlyHex) {
		this.containsOnlyHex = containsOnlyHex;
	}

	public Long getHexCount() {
		return hexCount;
	}

	public void setHexCount(Long hexCount) {
		this.hexCount = hexCount;
	}

	public void setByteValue(byte[] array) {
		clearValue();
		for (byte b : array) {
			this.token.append((char) b);
		}
	}

}
