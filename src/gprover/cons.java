package gprover;

import java.util.Arrays;

public class cons {
    final public static int MAXLEN = 16;

    private int id = 0;
    public int type = 0;
    public int no = 0;
    private boolean bHasConclusion = false;

    public int[] ps;
    public Object[] pss;
    private String sd = null;


    public cons(int t) {
        type = t;
        ps = new int[MAXLEN];
        pss = new Object[MAXLEN];
        no = -1;
        bHasConclusion = false;
    }

    public cons(cons c) {
        this(c.type);

        id = c.id;
        type = c.type;
        no = c.no;
        for (int i = 0; i <= no; i++) {
            ps[i] = c.ps[i];
            pss[i] = c.pss[i];
        }
        sd = c.sd;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bHasConclusion ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + no;
		result = prime * result + Arrays.hashCode(ps);
		result = prime * result + Arrays.hashCode(pss);
		result = prime * result + ((sd == null) ? 0 : sd.hashCode());
		result = prime * result + type;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof cons))
			return false;
		cons other = (cons) obj;
		if (bHasConclusion != other.bHasConclusion)
			return false;
		if (id != other.id)
			return false;
		if (no != other.no)
			return false;
		if (!Arrays.equals(ps, other.ps))
			return false;
		if (!Arrays.equals(pss, other.pss))
			return false;
		if (sd == null) {
			if (other.sd != null)
				return false;
		} else if (!sd.equals(other.sd))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	int getPts() {
        for (int i = 0; i < ps.length; i++)
            if (ps[i] == 0) return i;
        return ps.length;
    }

    public void setId(int newId) {
        id = newId;
    }

    public final int getId() {
        return id;
    }

    public boolean contains(int pt) {
        for (int i = 0; i < ps.length; i++)
            if (pt == ps[i])
                return true;
        return false;
    }

    public int getPtIndex(int pt) {
        for (int i = 0; i < ps.length; i++)
            if (pt == ps[i])
                return i;
        return -1;
    }

    public int getLastPt() {
        int pt = 0;
        for (int i = 0; i < ps.length; i++)
            if (pt < ps[i])
                pt = ps[i];
        return pt;
    }
    public int getLessPt(int n) {
        int k = 0;
        for (int i = 0; i <= no; i++) {
            if (ps[i] < n && ps[i] > k)
                k = ps[i];
        }
        return k;
    }
    
    public cons(int t, int len) {
        type = t;
        ps = new int[len + 1];
        pss = new Object[len + 1];
        no = -1;
    }

    public void add_pt(int n) {
        if (n == 0)
            return;

        ps[++no] = n;
    }

    public void add_pt(Object s) {
        pss[++no] = s;
    }

    public void add_pt(int n, int id) {
        ps[no = id] = n;
    }

    public void add_pt(Object s, int id) {
        pss[no = id] = s;
    }

    public String toString() {
        if (sd == null) {
            String s = "";
            for (int i = 0; i <= no; i++)
                if (pss[i] != null)
                    s += " " + pss[i];

            if (!is_conc()) {
                sd = CST.get_preds(type) + s;
            } else {
                sd = "SHOW: " + CST.getClus(type) + s;
            }
        } else if (type == gib.CO_NANG || type == gib.CO_NSEG) {
            return "SHOW: " + CST.getClus(type) + " " + sd;
        }

        if (type == gib.C_POINT)
            return trim(sd);

        return sd;
    }

    public String toStringEx() {
        if (sd == null) {
            String s = "";
            for (int i = 0; i <= no; i++)
                if (pss[i] != null)
                    s += " " + pss[i];

            if (!is_conc()) {
                sd = CST.get_preds(type) + s;
            } else {
                sd = "SHOW: " + CST.getClus(type) + s;
            }
        } else if (type == gib.CO_NANG || type == gib.CO_NSEG) {
            return "SHOW: " + CST.getClus(type) + " " + sd;
        }

        return sd;
    }

    public void setText(String s) {
        sd = s;
    }

    public String getPrintText(boolean isSelected) {
        if (sd == null) {
            String s = "";
            for (int i = 0; i <= no; i++)
                if (pss[i] != null)
                    s += " " + pss[i];
            if (!is_conc()) {
                sd = CST.get_preds(type) + s;
            } else {
                sd = "SHOW: " + CST.getClus(type) + s;
            }
        } else if (type == gib.CO_NANG || type == gib.CO_NSEG) {
            return "SHOW: " + sd;
        }

        if (!isSelected && type == gib.C_POINT)
            return trim(sd);

        return sd;
    }

    public static String trim(String st, int len) {
        if (st.length() > len)
            return st.substring(0, len) + "...";
        return st;
    }

    public static String trim(String st) {
        return trim(st, 32);
    }

    public void revalidate() {
        if (type != gib.CO_NANG && type != gib.CO_NSEG)
            sd = null;
    }

    public void setHasConclusion(boolean r) {
        bHasConclusion = r;
    }

    public boolean is_conc() {
        return type >= 50 && type < 100 && bHasConclusion;
    }

    public Object getPTN(int n) {
        if (n < 0 || n >= pss.length)
            return null;
        return pss[n];
    }

    public String toSString() {
        return CST.getDString(pss, type);
    }

    public String toDString() {
        String s = CST.getDString(pss, type);
        if (bHasConclusion)
            return "To Prove: " + s;
        if (type == gib.C_POINT)
            return trim(s);
        return s;
    }

    public String toDDString() {
        String s = CST.getDString(pss, type, false);
        if (bHasConclusion)
            return "To Prove: " + s;
        return s;
    }

    public static cons copy(cons c) {
        cons c1 = new cons(c.type, c.no);
        for (int i = 0; i < c1.no; i++) {
            c1.ps[i] = c.ps[i];
            c1.pss[i] = c.pss[i];
        }

        c1.id = c.id;
        return c1;
    }


    ///////////////////////////////////////////////////////////////////
    public void replace(int a, int b) {
        for (int i = 0; i <= no; i++) {
            if (ps[i] == a)
                ps[i] = b;
        }
    }

    public boolean isEqual(cons c) {
        if (c.type != type)
            return false;
        if (c.no != no)
            return false;
        for (int i = 0; i <= no; i++) {
            if (c.ps[i] != ps[i])
                return false;
        }
        return true;
    }

    public void reorder() {
        switch (type) {
            case gib.C_O_L:
                reorder1(0, 1);
                reorder1(0, 2);
                reorder1(1, 2);
                break;
            case gib.C_O_P:
            case gib.C_O_T:
                reorder2();

                break;
            case gib.C_I_EQ:
                reorder1(0, 1);

                break;
            case gib.C_CIRCUM:
                reorder1(1, 2);
                reorder1(0, 2);
                reorder1(1, 2);
                break;
        }
    }

    private void reorder1(int m, int n) {
        if (m == n)
            return;
        if (ps[m] < ps[n]) {
            int d = ps[m];
            ps[m] = ps[n];
            ps[n] = d;
        }
    }

    private void reorder2() {
        reorder1(0, 1);
        reorder1(2, 3);
        if (ps[0] < ps[2]) {
            int a = ps[0];
            ps[0] = ps[2];
            ps[2] = a;
            a = ps[1];
            ps[1] = ps[3];
            ps[3] = a;
        }
    }

}
