package com.cyclo;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyclo.zvxgw008_srv_entities.v1.entitytypes.Material;

/**
 * List adapter.
 */
public class MaterialListAdapter extends BaseAdapter
{	
	private List<Material> entries;
	private List<Material> filteredEntries;

	private Context mContext;

	/**
	 * Constructs a new list adapter with the given context.
	 * @param c - application context.
	 */
	public MaterialListAdapter(Context c)
	{
		mContext = c;
	}

	/**
	 * Returns the list of entries.
	 * @return - list of entries.
	 */
	public List<Material> getEntries()
	{
		return entries;
	}

	/**
	 * Sets the given entries.
	 * @param entries
	 */
	public void setEntries(List<Material> entries)
	{
		this.entries = entries;
		this.filteredEntries = entries;
	}

	/**
	 * Returns the number of entries.
	 * @return - the number of entries.
	 */
	public int getCount()
	{
		return filteredEntries.size();
	}

	/**
	 * Returns the item in the given position.
	 * @param position - the position of the desired item.
	 * @return - the item in the given position.
	 */
	public Object getItem(int position)
	{
		return filteredEntries.get(position);
	}

	/**
	 * Returns the id of the item in the given position.
	 * @param position - the position of the item.
	 * @return - the id of the item in the given position.
	 */
	public long getItemId(int position)
	{
		return position;
	}
	
	private static class ViewHolder 
	{
		public LinearLayout ll;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		
		if (rowView == null) 
		{
			LayoutInflater mInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate a view template
			rowView = mInflater.inflate(com.cyclo.R.layout.item_entry, parent, false);
			
			ViewHolder holder = new ViewHolder();
			holder.ll = (LinearLayout) rowView.findViewById(com.cyclo.R.id.linearLayout1);
			holder.ll.setPadding(10, 10, 10, 10);
			holder.ll.setOrientation(LinearLayout.VERTICAL);
			
			rowView.setTag(holder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.ll.removeAllViews();
		
		Material entry = filteredEntries.get(position);
		
		TextView MaterialIDTextView = new TextView(mContext);
		//set the first property to bigger font
		MaterialIDTextView.setTextSize(22); 
		MaterialIDTextView.setText(getPropertyValue(String.valueOf(entry.getMaterialID())));
		holder.ll.addView(MaterialIDTextView);	
		
		TextView MatlDescTextView = new TextView(mContext);
		MatlDescTextView.setText(getPropertyValue(String.valueOf(entry.getMatlDesc())));
		holder.ll.addView(MatlDescTextView);	
		return rowView;
	}
	
	/**
	 * Returns the property value.
	 * @param value
	 * @return - property value.
	 */
	public String getPropertyValue(String value)
	{
		if (value.equalsIgnoreCase("null"))
		{
			return mContext.getString(com.cyclo.R.string.no_value);
		}

		return value;
	}

	/**
	 * Filters the items by the given constraint.
	 * @param constraint
	 */
	public void filter(CharSequence constraint)
	{
		if (constraint != null)
		{
			constraint = constraint.toString().toLowerCase();
			this.filteredEntries = new ArrayList<Material>();
			for (Material entry : entries)
			{
				ArrayList<String> values = new ArrayList<String>();

				values.add(String.valueOf(entry.getMaterialID()).toLowerCase());
				values.add(String.valueOf(entry.getMatlDesc()).toLowerCase());
				boolean found = false;
				for (String s : values)
				{
					if (s != null && s.contains(constraint))
					{
						found = true;
						break;
					}
				}
				
				if(found)
					filteredEntries.add(entry);
			}
		} 
		else
		{
			this.filteredEntries = entries;
		}
		
		notifyDataSetChanged();
	}
}