package com.letcome.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.letcome.R;

/**
 * Created by rjt on 16/9/12.
 */
public class ProductDetailDlg extends Dialog {
    Button mCloseBtn;
    TextView mProductName,mProductPrice;
    public ProductDetailDlg(Context context) {
        this(context, R.style.Dialog_FS);
    }

    public ProductDetailDlg(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_dialog);
        mCloseBtn = (Button)findViewById(R.id.close_btn);

        mProductName = (TextView)findViewById(R.id.product_name);
        mProductPrice = (TextView)findViewById(R.id.product_price);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailDlg.this.hide();

            }
        });

    }


}
