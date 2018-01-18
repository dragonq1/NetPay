package com.example.quinten.netpay;


import android.app.Activity;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import static com.example.quinten.netpay.R.id.txtBetaler;

public class ListViewAdapter extends BaseAdapter{

    Activity context;
    String Betaler[];
    SpannableString Bedrag[];
    String Datum[];

    public ListViewAdapter(Activity context, String[] Betaler, SpannableString[] Bedrag, String[] Datum) {
        super();
        this.context = context;
        this.Betaler = Betaler;
        this.Bedrag = Bedrag;
        this.Datum = Datum;
    }

    public int getCount() {
        return Betaler.length;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtBetaler;
        TextView txtBedrag;
        TextView txtDatum;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflator = context.getLayoutInflater();

        if(convertView == null) {
            convertView = inflator.inflate(R.layout.listview_transacties,parent, false);
            holder = new ViewHolder();
            holder.txtDatum =  convertView.findViewById(R.id.txtDatum);
            holder.txtBetaler =  convertView.findViewById(txtBetaler);
            holder.txtBedrag =  convertView.findViewById(R.id.txtBedrag);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtBetaler.setText(Betaler[position]);
        holder.txtDatum.setText(Datum[position]);
        holder.txtBedrag.setText(Bedrag[position]);




    return convertView;
    }



}
