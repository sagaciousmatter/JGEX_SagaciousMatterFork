package wprover;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


public class DialogProof extends DialogBase {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8601297981255226615L;
	ProvePane m_cp;

    public DialogProof(DrawPanelFrame owner) {
        super((JFrame) null);
        m_cp = new ProvePane(owner, this);
        this.getContentPane().add(m_cp);
        this.setSize(650, 230);
    }

    void showDialog(ProofText cp) {
        m_cp.setValue(cp);
        this.setVisible(true);
    }

    public void setSelect(ArrayList<GraphicEntity> v) {
        m_cp.setSelect(v);
    }

//    public void setProveField(CProveField cpv) {
//        m_cp.setProveField(cpv);
//    }
}

class ProvePane extends JPanel
        implements ActionListener, ListSelectionListener, ItemListener, PopupMenuListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5821709625660989829L;
	//private CProveField Cpv = null;
    private JDialog dialog;

    private JTextField captainField;
    private JTextArea proveField;
    private JList<String> selectField;
    private DefaultListModel<String> listModel;
    private ArrayList<GraphicEntity> vlist;
    private DrawPanelFrame gxInstance;

    private ProofText cptext;
    private JButton b_select, b_ok, b_cancel;

    private PanelColorButton color_captain, color_text;
    //private Vector<String> fontfamily;
    private JComboBox<String> bfonts;
    private JComboBox<Integer> bsize;
    private JCheckBox cbox;

    private JTextField lrule;
    private JMenu mrule;
    private String srule;

    final static int GAP = 10;


    public ProvePane(DrawPanelFrame gx, JDialog dlg) {
        gxInstance = gx;
        dialog = dlg;
        vlist = new ArrayList<GraphicEntity>();
        setRuleList();

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        JPanel leftHalf = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -7138746795078312701L;

			public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE,
                        pref.height);
            }
        };
        leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.PAGE_AXIS));
        leftHalf.add(createEntryFields());

        add(leftHalf);
        add(createAddressDisplay());
    }

//    public void setProveField(CProveField cpv) {
//        Cpv = cpv;
//    }

    public void setRuleList() {
        String user_directory = DrawPanelFrame.getUserDir();
        File f = new File(user_directory + "/wprover/rules");
        mrule = new JMenu("-->");
        addDirectory(mrule, f, "rules");
    }

    public void addDirectory(JMenu menu, File f, String apath) {
        if (!f.exists()) return;
        File[] flist = f.listFiles();
        String sp = DrawPanelFrame.getFileSeparator();

        for (int i = 0; i < flist.length; i++) {
            String sn = flist[i].getName();

            if (flist[i].isFile()) {
                if (sn.endsWith(".gex")) {
                    String sf = sn.substring(0, sn.length() - 4);
                    JMenuItem item = new JMenuItem(sf);
                    item.addActionListener(this);
                    item.setName(apath + sp + sn);
                    menu.add(item);
                }
            } else if (flist[i].isDirectory()) {
                JMenu m = new JMenu(sn);
                menu.add(m);
                addDirectory(m, flist[i], apath + sp + sn);
            }
        }
    }

    public void setValue(ProofText cp) {
        cptext = cp;
        captainField.setForeground(cp.getCaptainColor());

        captainField.setText(cp.getHead());
        proveField.setText(cp.getMessage());
        proveField.setForeground(cp.getMessageColor());
        ArrayList<GraphicEntity> v = new ArrayList<GraphicEntity>();
	cp.getObjectList(v);
        vlist.clear();

        for (int i = 0; i < v.size(); i++) {
            GraphicEntity cc = v.get(i);
            if (cc.m_type != GraphicEntity.TEXT)
                vlist.add(cc);
        }
        resetModel();
        Font f = cptext.getFont();

        bfonts.setSelectedItem(f.getName());
        bsize.setSelectedItem(new Integer(f.getSize()));

        Color c1 = cptext.getCaptainColor();
        color_captain.setForeground(c1);
        color_captain.setBackground(c1);

        Color c2 = cptext.getMessageColor();
        color_text.setForeground(c2);
        color_text.setBackground(c2);
        cbox.setSelected(cp.isVisible());
        srule = cp.getRule();
        lrule.setText(cp.getRule());

    }

    public void resetModel() {
        listModel.clear();
        for (int i = 0; i < vlist.size(); i++) {
            GraphicEntity cc = vlist.get(i);
            listModel.addElement(cc.TypeString());
        }

    }

    protected JComponent createButtons() {
        JPanel top = new JPanel();
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JPanel prule = new JPanel();

        prule.setLayout(new BoxLayout(prule, BoxLayout.Y_AXIS));
        lrule = new JTextField(20);
        prule.add(lrule);
        JMenuBar jbar = new JMenuBar();
        jbar.add(mrule);
        prule.add(jbar);

        b_select = new JButton("clear");
        b_select.addActionListener(this);

        panel.add(b_select);
        b_ok = new JButton("OK");
        b_ok.addActionListener(this);
        panel.add(b_ok);

        b_cancel = new JButton("Cancel");
        b_cancel.addActionListener(this);
        panel.add(b_cancel);

        Font f = new Font("Dialog", Font.PLAIN, 14);
        b_select.setFont(f);
        b_ok.setFont(f);
        b_cancel.setFont(f);
        b_select.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        b_ok.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        b_cancel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        top.setBorder(BorderFactory.createEmptyBorder(0, 0, GAP - 5, GAP - 5));
        top.add(prule);
        top.add(panel);

        return top;
    }

    public void valueChanged(ListSelectionEvent e) {
        int index = selectField.getSelectedIndex();
        if (index >= 0 && index < vlist.size()) {
            ArrayList<GraphicEntity> vc = new ArrayList<GraphicEntity>();
            vc.add(vlist.get(index));
            gxInstance.dp.setObjectListForFlash(vc);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        String sname = e.getActionCommand();

        if (obj == this.b_select) {
            listModel.clear();
            vlist.clear();
            gxInstance.dp.SetCurrentAction(DrawPanel.SELECT);
        } else if (obj == this.b_ok) {
            cptext.setHead(captainField.getText());
            cptext.setMessage(proveField.getText());
            cptext.setObjectList(vlist);
            String s = (String) bfonts.getSelectedItem();
            int size = ((Integer) bsize.getSelectedItem()).intValue();
            if (size < 10) return;
            Font ff = cptext.getFont();
            cptext.setFont(new Font(s, ff.getStyle(), size));
            cptext.setCaptainColor(captainField.getForeground());
            cptext.setMessageColor(proveField.getForeground());
            cptext.setVisible(cbox.isSelected());

            String rule = lrule.getText();
            cptext.setRule(rule);
            if (rule.length() > 0)
                cptext.setRulePath(srule);
            else cptext.setRulePath("");
            gxInstance.dp.cpfield.reGenerateIndex();
            dialog.setVisible(false);
            gxInstance.d.repaint();

        } else if (obj == this.b_cancel)
            dialog.setVisible(false);
        else if (obj instanceof JMenuItem) {
            JMenuItem item = (JMenuItem) e.getSource();
            String path = item.getName();
            lrule.setText(sname);
            srule = path;
        }

    }

    public void setSelect(ArrayList<GraphicEntity> v) {
        vlist.clear();
        vlist.addAll(v);
        resetModel();

    }


    protected JComponent createAddressDisplay() {
        JPanel panel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<String>();
        selectField = new JList<String>(listModel);
        selectField.addListSelectionListener(this);
        selectField.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectField.setBackground(new Color(220, 220, 220));
        panel.add(new JScrollPane(selectField), BorderLayout.CENTER);
        panel.add(createButtons(), BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(GAP / 2, GAP / 2, GAP / 2, GAP / 2));
        panel.setPreferredSize(new Dimension(250, 150));
        JPanel all = new JPanel(new BorderLayout());
        all.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_START);
        all.add(panel, BorderLayout.CENTER);
        return all;
    }


    protected JComponent createEntryFields() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        JLabel label1 = new JLabel("Captain", JLabel.TRAILING);
        panel.add(label1);

        captainField = new JTextField();
        captainField.setColumns(20);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(captainField);
        color_captain = new PanelColorButton(20, 20);
        color_captain.getColorMenu().addPopupMenuListener(this);
        p.add(color_captain);
        panel.add(p);

        label1 = new JLabel("Prove",
                JLabel.TRAILING);
        panel.add(label1);
        proveField = new JTextArea(5, 20);
        panel.add(new JScrollPane(proveField));

        JPanel lbpanel = new JPanel();
        lbpanel.setLayout(new BoxLayout(lbpanel, BoxLayout.X_AXIS));
        lbpanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        color_text = new PanelColorButton(30, 25);
        color_text.getColorMenu().addPopupMenuListener(this);
        lbpanel.add(color_text);
        lbpanel.add(Box.createHorizontalStrut(5));

        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String envfonts[] = gEnv.getAvailableFontFamilyNames();
//        fontfamily = new Vector<String>();
//        for (int i = 1; i < envfonts.length; i++)
//            fontfamily.add(envfonts[i]);
        bfonts = new JComboBox<String>(envfonts);
        bfonts.setMaximumRowCount(9);
        bfonts.addItemListener(this);
        lbpanel.add(bfonts);
        lbpanel.add(Box.createHorizontalStrut(5));

        bsize = new JComboBox<Integer>(new Integer[]{new Integer(10), new Integer(11), new Integer(12),
                new Integer(13), new Integer(14), new Integer(15),
                new Integer(16), new Integer(18), new Integer(20),
                new Integer(22), new Integer(24), new Integer(26),
                new Integer(27), new Integer(28), new Integer(29),
                new Integer(30), new Integer(36), new Integer(72)});
        bsize.setMaximumRowCount(9);
        bsize.addItemListener(this);
        lbpanel.add(bsize);
        lbpanel.add(Box.createHorizontalStrut(5));
        cbox = new JCheckBox("visible");
        cbox.setSelected(false);
        lbpanel.add(cbox);

        lbpanel.setMaximumSize(lbpanel.getPreferredSize());
        panel.add(lbpanel);
        return panel;

    }

    public void itemStateChanged(ItemEvent e) {
        Object obj = e.getSource();

        if (obj == this.bfonts) {
            String s = (String) bfonts.getSelectedItem();
            if (s.length() == 0) return;
            Font f = cptext.getFont();
            Font tf = new Font(s, f.getStyle(), f.getSize());
            proveField.setFont(tf);
        } else if (obj == this.bsize) {
            int size = ((Integer) bsize.getSelectedItem()).intValue();
            if (size < 10) return;

            Font f = cptext.getFont();
            Font tf = new Font(f.getPSName(), f.getStyle(), size);
            proveField.setFont(tf);
        }
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

        Object obj = e.getSource();


        if (obj == color_captain.getColorMenu()) {
            Color color = color_captain.setNewColor();
            if (color != null) {
                captainField.setForeground(color);
            }

        } else if (obj == color_text.getColorMenu()) {
            Color c = color_text.setNewColor();
            proveField.setForeground(c);
        }

    }

    public void popupMenuCanceled(PopupMenuEvent e) {
    }

////////////////////////////////////////////////////////////////////////////////


}