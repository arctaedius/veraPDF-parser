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
package org.verapdf.pd.font.truetype;

import org.verapdf.as.io.ASInputStream;
import org.verapdf.cos.COSObject;
import org.verapdf.pd.font.CIDToGIDMapping;
import org.verapdf.pd.font.FontProgram;
import org.verapdf.pd.font.cmap.CMap;

import java.io.IOException;

/**
 * Represents CIDFontType2 font program.
 *
 * @author Sergey Shemyakov
 */
public class CIDFontType2Program extends BaseTrueTypeProgram implements FontProgram {

    private CMap cMap;
    private CIDToGIDMapping cidToGID;

    /**
     * Constructor from font stream and encoding details.
     *
     * @param stream
     * @param cMap
     * @param cidToGID
     * @throws IOException
     */
    public CIDFontType2Program(ASInputStream stream, CMap cMap, COSObject cidToGID) throws IOException {
        super(stream);
        this.cMap = cMap;
        this.cidToGID = new CIDToGIDMapping(cidToGID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth(int code) {
        int cid = cMap.toCID(code);
        return this.getWidthWithCheck(cidToGID.getGID(cid));
    }

    @Override
    public float getWidth(String glyphName) {
        return 0;   // no need in this method
    }

    @Override
    public boolean containsCode(int code) {
        if (this.cMap.containsCode(code)) {
            int cid = this.cMap.toCID(code);
            if (this.cidToGID.contains(cid) && cid != 0) {
                int gid = this.cidToGID.getGID(cid);
                TrueTypeMaxpTable maxpParser = parser.getMaxpParser();
                return maxpParser != null &&
                        gid != 0 &&
                        gid < maxpParser.getNumGlyphs();
            }
        }
        return false;
    }
}
