package br.feevale.accelerokey;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Gallery adapter to show a list of characters (a..z) to be selected
 */
public class CharacterListAdapter extends BaseAdapter {
	private List<TextView> buttons;

	public CharacterListAdapter(Context c, int size, float height) {

		buttons = new ArrayList<TextView>();
		for (char i = 'A'; i <= 'Z'; i++) {
			TextView child = new TextView(c);
			child.setId(100 + i);
			child.setText("" + new Character(i));
			child.setWidth(size);
			child.setHeight(size);
			child.setTextSize(height);
			buttons.add(child);
		}

	}

	public int getCount() {
		return buttons.size();
	}

	public Object getItem(int position) {
		return buttons.get(position).getText();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return buttons.get(position);
	}
}
