package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pro1121_nhom3.model.hoadon;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class thongkeActivity extends AppCompatActivity {

    ArrayList<BarEntry> entries, entries2;
    EditText etnam;
    ImageView bttk;
    Button btback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        BarChart barChart = findViewById(R.id.barcharttk);
        BarChart barChart2 = findViewById(R.id.barcharttk2);
        etnam = findViewById(R.id.etnamtk);
        bttk = findViewById(R.id.bttk);
        btback = findViewById(R.id.btbacktk);


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.getDefault());
        String namnay = df.format(c);
        etnam.setText(namnay);

        bttk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDoanhthuByYear(etnam.getText().toString(), barChart, barChart2);
            }
        });

        barChart.setNoDataText("");
        barChart2.setNoDataText("");
        barChart.getAxisRight().setDrawLabels(false);
        barChart2.getAxisRight().setDrawLabels(false);
        entries = new ArrayList<>();
        entries2 = new ArrayList<>();

        getDoanhthuByYear(etnam.getText().toString(), barChart, barChart2);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setEnabled(false);
        yAxis.setSpaceBottom(0);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(12);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(12);
        barChart.invalidate();
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.invalidate();


        YAxis yAxis2 = barChart2.getAxisLeft();
        yAxis2.setEnabled(false);
        yAxis2.setSpaceBottom(0);
        XAxis xAxis2 = barChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setLabelCount(12);
        xAxis2.setDrawGridLines(false);
        xAxis2.setAxisMaximum(12);
        barChart2.invalidate();
        barChart2.getAxisRight().setEnabled(false);
        barChart2.getDescription().setEnabled(false);
        barChart2.setDrawGridBackground(false);
        barChart2.invalidate();

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getDoanhthuByYear(String year, BarChart barChart, BarChart barChart2)
    {
        entries.clear();
        entries2.clear();

        DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("hoadon");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        billRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i=1;i<=12; i++)
                {
                    float doanhthuthang = 0;
                    int doanhsothang = 0;
                    DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("TempData");
                    for(DataSnapshot billsnap : snapshot.getChildren())
                    {
                        hoadon hoadon1 = billsnap.getValue(hoadon.class);
                        Date ngayhd;
                        try {
                            ngayhd = format.parse(hoadon1.getNgaymua());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        String billyear = (String) DateFormat.format("yyyy",  ngayhd);
                        String billmonth  = (String) DateFormat.format("MM",  ngayhd);
                        if(Integer.parseInt(billmonth) == i && billyear.equals(year)){
                            doanhthuthang += hoadon1.getGame().getGiaban();
                            doanhsothang += 1;
                        }
                        tempRef.child(String.valueOf(i)).setValue(doanhthuthang);
                        tempRef.child("a" + i).setValue(doanhsothang);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=1;i<=12;i++)
                {
                    DatabaseReference TempRef = FirebaseDatabase.getInstance().getReference("TempData");
                    int month = i;
                    TempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            float x = snapshot.child(String.valueOf(month)).getValue(float.class);
                            int y = snapshot.child("a" + month).getValue(int.class);


                            entries.add(new BarEntry(month, x));
                            BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
                            dataSet.setColor(Color.rgb(100,200,100));
                            BarData barData = new BarData(dataSet);
                            barData.setValueTextSize(10);
                            barChart.setData(barData);
                            barChart.getXAxis().setAxisMaximum(barData.getXMax() + 0.25f);

                            entries2.add(new BarEntry(month, y));
                            BarDataSet dataSet2 = new BarDataSet(entries2, "Doanh số");
                            dataSet2.setColor(Color.rgb(0, 153, 255));
                            BarData barData2 = new BarData(dataSet2);
                            barChart2.setData(barData2);
                            barData2.setValueTextSize(10);
                            barChart2.getXAxis().setAxisMaximum(barData2.getXMax() + 0.25f);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                Toast.makeText(thongkeActivity.this, "Vui lòng đợi biểu đồ cập nhật", Toast.LENGTH_SHORT).show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                        barChart2.notifyDataSetChanged();
                        barChart2.invalidate();
                        Toast.makeText(thongkeActivity.this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        }, 1000);




    }

}