import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer<String> {
    String title;

    public ComboBoxRenderer(String title) {
        this.title = title;
    }

    @Override
    // Gets JLabel with selected icon and text
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        if (index == -1 && value == null) {
            setIcon(null);
            setText(title);
        }
        else {
            setIcon(new ImageIcon("icons/" + value + ".png"));
            setText(value);
        }
        return this;
    }
}