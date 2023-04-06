package com.xq.wwwwwxxxxx.paydialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class PayDialog extends Dialog implements View.OnClickListener {

    private Context context;

    public static final int WX_PAY = 0;
    public static final int ALI_PAY = 1;
    public static final int BALANCE_PAY = 2;


    private TextView tvCancel;
    private TextView tvPayMoney;
    private RadioButton rbWxPay;
    private RelativeLayout rlWxPay;
    private RadioButton rbAliPay;
    private RelativeLayout rlAliPay;
    private ImageView ivBalanceIcon;
    private TextView tvBalancePay;
    private TextView tvBalancePayContext;
    private TextView tvBalance;
    private RadioButton rbBalancePay;
    private RelativeLayout rlBalancePay;
    private Button btnPay;

    private int payType = 0;//支付方式，默认微信
    private OnPayClickListener listener;
    private double payMoney = 0;//支付金额
    private double balance = 0;//余额

    private Boolean haveBalance = true;
    private Boolean haveAliPay = true;
    private Boolean haveWXPay = true;

    public PayDialog setListener(OnPayClickListener listener) {
        this.listener = listener;
        return this;
    }

    public PayDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
    }

    public PayDialog haveBalance(Boolean haveBalance) {
        this.haveBalance = haveBalance;
        return this;
    }

    public PayDialog haveAliPay(Boolean haveAliPay) {
        this.haveAliPay = haveAliPay;
        return this;
    }

    public PayDialog haveWXPay(Boolean haveWXPay) {
        this.haveWXPay = haveWXPay;
        return this;
    }

    /**
     * @param payMoney 支付金额
     * @param balance  余额
     * @return
     */
    public PayDialog setData(double payMoney, double balance) {
        this.payMoney = payMoney;
        this.balance = balance;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_dialog_layout);
        tvCancel = findViewById(R.id.tv_cancel);
        tvPayMoney = findViewById(R.id.tv_pay_money);
        rbWxPay = findViewById(R.id.rb_wx_pay);
        rbAliPay = findViewById(R.id.rb_ali_pay);
        ivBalanceIcon = findViewById(R.id.iv_balance_icon);
        tvBalancePay = findViewById(R.id.tv_balance);
        tvBalancePayContext = findViewById(R.id.tv_balance_pay_context);
        tvBalance = findViewById(R.id.tv_balance);
        rbBalancePay = findViewById(R.id.rb_balance_pay);
        rlBalancePay = findViewById(R.id.rl_balance_pay);
        btnPay = findViewById(R.id.btn_pay);
        rlWxPay = findViewById(R.id.rl_wx_pay);
        rlAliPay = findViewById(R.id.rl_ali_pay);
        rlWxPay.setOnClickListener(this);
        rlAliPay.setOnClickListener(this);
        rlBalancePay.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        java.text.DecimalFormat format = new java.text.DecimalFormat("0.00");
        String payMoneyStr = format.format(payMoney);
        String balanceStr = format.format(balance);
        tvPayMoney.setText(payMoneyStr);
        tvBalance.setText(balanceStr);
        if (balance < payMoney) {
            tvBalance.setText("");
            tvBalancePay.setTextColor(context.getResources().getColor(R.color.colorTextHint));
            tvBalancePayContext.setTextColor(context.getResources().getColor(R.color.colorTextHint));
            tvBalancePayContext.setText(context.getResources().getString(R.string.common_balance_not_enough_context));
            ivBalanceIcon.setBackgroundColor(context.getResources().getColor(R.color.colorTextHint));
            rlBalancePay.setClickable(false);
            rbBalancePay.setEnabled(false);
        }
        if (!haveWXPay) {
            rlWxPay.setVisibility(View.GONE);
            rbWxPay.setChecked(false);
            rbAliPay.setChecked(true);
            rbBalancePay.setChecked(false);
        }
        if (!haveAliPay) {
            rlAliPay.setVisibility(View.GONE);
            if (rlWxPay.getVisibility() == View.VISIBLE) {
                rbWxPay.setChecked(true);
                rbAliPay.setChecked(false);
                rbBalancePay.setChecked(false);
            } else {
                rbWxPay.setChecked(false);
                rbAliPay.setChecked(false);
                rbBalancePay.setChecked(true);
            }
        }
        if (!haveBalance) {
            rlBalancePay.setVisibility(View.GONE);
            if (rlWxPay.getVisibility() == View.VISIBLE && rlAliPay.getVisibility() == View.VISIBLE) {
                rbWxPay.setChecked(true);
                rbAliPay.setChecked(false);
                rbBalancePay.setChecked(false);
            } else if (rlAliPay.getVisibility() == View.VISIBLE && rlWxPay.getVisibility() == View.GONE) {
                rbWxPay.setChecked(false);
                rbAliPay.setChecked(true);
                rbBalancePay.setChecked(false);
            }
        }
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        if (getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(params);
            getWindow().setGravity(Gravity.BOTTOM);
            setCanceledOnTouchOutside(true);
        }
        if (!haveWXPay&&!haveAliPay&&!haveBalance){
            dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_wx_pay) {
            rbWxPay.setChecked(true);
            rbAliPay.setChecked(false);
            rbBalancePay.setChecked(false);
            payType = WX_PAY;
        }
        if (v.getId() == R.id.rl_ali_pay) {
            rbWxPay.setChecked(false);
            rbAliPay.setChecked(true);
            rbBalancePay.setChecked(false);
            payType = ALI_PAY;
        }
        if (v.getId() == R.id.rl_balance_pay) {
            rbWxPay.setChecked(false);
            rbAliPay.setChecked(false);
            rbBalancePay.setChecked(true);
            payType = BALANCE_PAY;
        }
        if (v.getId() == R.id.btn_pay) {
            listener.onPayClick(payType);
            dismiss();
        }
        if (v.getId() == R.id.tv_cancel) {
            dismiss();
        }
    }

    public interface OnPayClickListener {
        void onPayClick(int payType);
    }
}
