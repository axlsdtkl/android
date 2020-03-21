package com.example.artshoes2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PdfAdapter extends ArrayAdapter<Pdf> {
    private int resourceId;
    public PdfAdapter(Context context, int textViewResourceId, List<Pdf> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Pdf pdf=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView pdfImage=(ImageView)view.findViewById(R.id.pdf_image);
        TextView pdfName=(TextView)view.findViewById(R.id.pdf_name);
        pdfImage.setImageResource(pdf.getImageId());
        pdfName.setText(pdf.getFileName());
        return view;
    }
}
