package com.example.quinten.netpay;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{

    Activity context;
    String Betaler[];
    String Bedrag[];
    String Datum[];

    public ListViewAdapter(Activity context, String[] Betaler, String[] Bedrag, String[] Datum) {
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflator = context.getLayoutInflater();

        if(convertView == null) {
            convertView = inflator.inflate(R.layout.listview_transacties, null);
            holder = new ViewHolder();
            holder.txtDatum =  convertView.findViewById(R.id.txtDatum);
            holder.txtBetaler =  convertView.findViewById(R.id.txtBetaler);
            holder.txtBedrag =  convertView.findViewById(R.id.txtBedrag);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtDatum.setText(Datum[position]);
        holder.txtBetaler.setText(Betaler[position]);
        holder.txtBedrag.setText(Bedrag[position]);



    return convertView;
    }



}
