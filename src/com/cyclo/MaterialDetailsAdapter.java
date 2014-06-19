package com.cyclo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyclo.zvxgw008_srv_entities.v1.entitytypes.Material;
	
/**
 * Details adapter.
 */
public class MaterialDetailsAdapter extends BaseAdapter
{
	public static enum SapSemantics {tel, email, url};
	

	private Context mContext;
	private Material entry;

	private List<String> propertiesValues = new ArrayList<String>();
	private List<String> labels = new ArrayList<String>();
	private List<String> sapSemanticsList = new ArrayList<String>();

	/**
	 * Constructs a new details adapter with the given parameters.
	 * @param c - application context.
	 * @param e - entry.
	 */
	public MaterialDetailsAdapter(Context c, Material e)
	{
		
		mContext = c;
		entry = e;
		propertiesValues.add(String.valueOf(entry.getMaterialID()));
		labels.add(Material.getLabelForProperty("MaterialID"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getMatlDesc()));
		labels.add(Material.getLabelForProperty("MatlDesc"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getMatlType()));
		labels.add(Material.getLabelForProperty("MatlType"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getIndSector()));
		labels.add(Material.getLabelForProperty("IndSector"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getBaseUom()));
		labels.add(Material.getLabelForProperty("BaseUom"));
		sapSemanticsList.add("unit-of-measure");
		propertiesValues.add(String.valueOf(entry.getUnitOfWt()));
		labels.add(Material.getLabelForProperty("UnitOfWt"));
		sapSemanticsList.add("unit-of-measure");
		propertiesValues.add(String.valueOf(entry.getGrossWt()));
		labels.add(Material.getLabelForProperty("GrossWt"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getSizeDim()));
		labels.add(Material.getLabelForProperty("SizeDim"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getCreatedOn()));
		labels.add(Material.getLabelForProperty("CreatedOn"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getCreatedBy()));
		labels.add(Material.getLabelForProperty("CreatedBy"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getChangedBy()));
		labels.add(Material.getLabelForProperty("ChangedBy"));
		sapSemanticsList.add(null);
		propertiesValues.add(String.valueOf(entry.getLastChnge()));
		labels.add(Material.getLabelForProperty("LastChnge"));
		sapSemanticsList.add(null);
	}

	/**
	 * Returns the amount of entries.
	 * @return - the amount of entries.
	 */
	public int getCount()
	{
		return propertiesValues.size();
	}

	/**
	 * Returns the item in the given position.
	 * @param position - the position of the desired item.
	 * @return - the item in the given position.
	 */
	public Object getItem(int position)
	{
		return position;
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
	
	private class ViewHolder 
	{
		public ImageView imageView;
		public TextView textView1;
		public TextView textView2;
	}
		
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		
		if (rowView == null || position == propertiesValues.size()) 
		{			
			LayoutInflater mInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// Inflate a view template
			rowView = mInflater.inflate(com.cyclo.R.layout.item_detail_row, parent, false);
			
			ViewHolder holder = new ViewHolder();
			
			holder.textView1 = (TextView) rowView.findViewById(com.cyclo.R.id.textView1);
			holder.textView1.setTextSize(22);
			holder.textView2 = (TextView) rowView.findViewById(com.cyclo.R.id.textView2);
			holder.imageView = (ImageView) rowView.findViewById(com.cyclo.R.id.imageView1);
			rowView.setTag(holder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		

		String value = getPropertyValue(position);
		String label = labels.get(position);

		holder.textView1.setText(label);
		holder.textView2.setText(value);
		holder.imageView.setVisibility(View.INVISIBLE);
		
		SapSemantics sapSemantics = getSapSemantics(position);
		if (sapSemantics != null)
		{
			switch (sapSemantics)
			{
				case tel:   holder.imageView.setImageResource(com.cyclo.R.drawable.tel);
							holder.imageView.setVisibility(View.VISIBLE);
						    break;
						    
				case email: holder.imageView.setImageResource(com.cyclo.R.drawable.email);
							holder.imageView.setVisibility(View.VISIBLE);
					        break;
					        
				case url:   holder.imageView.setImageResource(com.cyclo.R.drawable.url);
							holder.imageView.setVisibility(View.VISIBLE);
					        break;
			}
		}

		return rowView;
	}
	
	/**
	 * Returns the SAP semantics in the given position.
	 * @param position
	 * @return - SAP semantics in the given position.
	 */
	public SapSemantics getSapSemantics(int position)
	{
		if (sapSemanticsList == null || sapSemanticsList.isEmpty() || position >= sapSemanticsList.size())
		{
			return null;
		}
		
		String value = this.sapSemanticsList.get(position);
		if (value == null)
		{
			return null;
		}
		
		value = value.toLowerCase();
		
		SapSemantics[] values = SapSemantics.values();
		for (SapSemantics sapSemantics : values) 
		{
			String semanticName = sapSemantics.name();
			if (semanticName.equals(value) || value.contains(semanticName + ";"))
			{
				return sapSemantics;
			}
		}
		return null;
	}
	
	/**
	 * Returns the property value.
	 * @param value
	 * @return - property value.
	 */
	public String getPropertyValue(int position)
	{
		if (propertiesValues == null || propertiesValues.isEmpty() || position >= propertiesValues.size())
		{
			return mContext.getString(com.cyclo.R.string.no_value);
		}

		String value = propertiesValues.get(position);
		if (value == null || value.length() == 0 || value.equalsIgnoreCase("null"))
		{
			return mContext.getString(com.cyclo.R.string.no_value);
		}
		
		return value;
	}
}
