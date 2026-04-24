package tui.components;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;

/**
 * Wrapper class for a horizontal list of radio buttons
 * Radio buttons consist of text labels
 * @author pogegril
 */
public class HorizontalRadioBox extends Panel {

	private final List<Button> radioList = new ArrayList();
	private int selected = -1;

	/**
	 * Creates the horizontal radio buttons' panel
	 */
	public HorizontalRadioBox() {
		super(new LinearLayout(Direction.HORIZONTAL));
	}

	/**
	 * Adds a new radio button to the panel
	 * @param label - Button's text label
	 */
	public void addButton(String label) {
		int index = radioList.size();

		Button button = new Button(label, () -> this.setSelected(index));
		this.radioList.add(button);
		addComponent(button);
			
		if (this.selected == -1) {
			this.setSelected(0);
		} else {
			updateLabels();
		}
	}

	/**
	 * Sets the selected button
	 * @param selection - Selection's index
	 */
	public void setSelected(int index) {
		this.selected = index;
		this.updateLabels();
	}

	/**
	 * Updates the Buttons' labels
	 */
	private void updateLabels() {
		for (int i = 0; i < radioList.size(); i++) {
			Button button = radioList.get(i);
			String cleanLabel = button.getLabel().replaceFirst("^\\[[X ]\\] ", "");

			if (i == this.selected) {
				button.setLabel("[X] " + cleanLabel);
			} else {
				button.setLabel("[ ] " + cleanLabel);
			}
		}
	}

	/**
	 * Returns the selection's text label
	 * @return label
	 */
	public String getSelectedLabel() {
		if (this.selected >= 0 && this.selected < this.radioList.size()) {
			return this.radioList.get(this.selected).getLabel().replaceFirst("^\\[[X ]\\] ", "");
		}
		return null;
	}

	/**
	 * Returns the selection's index
	 * @return index
	 */
	public int getSelectedIndex() {
		return this.selected;
	}
}
