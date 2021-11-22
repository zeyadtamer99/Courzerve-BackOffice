package com.zth.backoffice.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.zth.backoffice.Common.Common;
import com.zth.backoffice.Interface.RetrofitServiceRound;
import com.zth.backoffice.Model.Customer;
import com.zth.backoffice.Model.Payment;
import com.zth.backoffice.Model.Round;
import com.zth.backoffice.Model.isContactInfo;
import com.zth.backoffice.R;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zth.backoffice.R.drawable.background;

public class CustomerAdapter extends BaseAdapter {
    private final String roundId;
    Activity activity;
    Round round;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private RetrofitServiceRound serviceRound;


    public CustomerAdapter(Activity activity, Round round, String roundId) {
        this.activity = activity;
        this.round = round;
        this.roundId = roundId;
    }

    @Override
    public int getCount() {
        return round.getCustomerApplied().size();
    }

    @Override
    public Object getItem(int position) {
        return round.getCustomerApplied().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        serviceRound = Common.putRound();
        convertView = activity.getLayoutInflater().inflate(R.layout.list_item_customer, parent, false);

        Button btn_contacted_blue = convertView.findViewById(R.id.contact_btn1);
        Button btn_contacted_white = convertView.findViewById(R.id.contact_btn);
        Button btn_payment_blue = convertView.findViewById(R.id.PaymentStatus_btn1);
        Button btn_payment_white = convertView.findViewById(R.id.PaymentStatus_btn);
        CardView cardView = convertView.findViewById(R.id.cv_round);
        Button btn_Attendance_white = convertView.findViewById(R.id.AttendanceStatus_btn);
        Button btn_Attendance_blue = convertView.findViewById(R.id.AttendanceStatus_btn1);
        Button btn_editData = convertView.findViewById(R.id.editData_btn);
        TextView tv_customerAppliedAt = convertView.findViewById(R.id.AppliedAt);
        TextView tv_customerName = convertView.findViewById(R.id.customerName_tv_customer);
        LinearLayout expandableView = convertView.findViewById(R.id.expandableView);
        Button btn_call = convertView.findViewById(R.id.call_customer);
        Button btn_expand = convertView.findViewById(R.id.expand);
        TextView tv_age = convertView.findViewById(R.id.customerAge);
        TextView tv_email = convertView.findViewById(R.id.customerEmail);
        TextView tv_number = convertView.findViewById(R.id.customerNumber);
        ArrayList<String> types = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayList<String> names = new ArrayList<String>();
        ArrayAdapter<String> contactedAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, types);
        contactedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);


        if (round.getCustomerApplied().get(position).getIsContacted() == null) {
            btn_contacted_white.setVisibility(View.VISIBLE);
            btn_contacted_blue.setVisibility(View.INVISIBLE);
        } else if (round.getCustomerApplied().get(position).getIsContacted() == true) {
            btn_contacted_white.setVisibility(View.INVISIBLE);
            btn_contacted_blue.setVisibility(View.VISIBLE);
        } else {
            btn_contacted_white.setVisibility(View.VISIBLE);
            btn_contacted_blue.setVisibility(View.INVISIBLE);
        }

        if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
            btn_payment_white.setVisibility(View.VISIBLE);
            btn_payment_blue.setVisibility(View.INVISIBLE);

        } else {
            btn_payment_blue.setVisibility(View.VISIBLE);
            btn_payment_white.setVisibility(View.INVISIBLE);
            btn_payment_blue.setText(round.getCustomerApplied().get(position).getPayment().getPaymentAmount());
        }

        if (round.getCustomerApplied().get(position).getIsContacted() != null) {
            if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                btn_contacted_blue.setText("CONFIRMED");
            } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy() == null) {
                btn_contacted_blue.setText("CONFIRMED");
            } else {
                if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy().equals("Select a Name")) {
                    btn_contacted_blue.setText("CONFIRMED");
                } else
                    btn_contacted_blue.setText(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy());
            }
        }

        if (round.getCustomerApplied().get(position).getAttendanceStatus().equals("APPLIED")) {
            btn_Attendance_white.setVisibility(View.VISIBLE);
            btn_Attendance_blue.setVisibility(View.INVISIBLE);
        } else if (round.getCustomerApplied().get(position).getAttendanceStatus().equals("RUNNING")) {
            btn_Attendance_white.setVisibility(View.INVISIBLE);
            btn_Attendance_blue.setVisibility(View.VISIBLE);
        } else {
            btn_Attendance_white.setVisibility(View.INVISIBLE);
            btn_Attendance_blue.setVisibility(View.VISIBLE);
        }


        btn_contacted_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(activity).inflate(R.layout.concernsdialog, null);

                final EditText concerns = view.findViewById(R.id.concerns_et);
                final EditText comments = view.findViewById(R.id.comments_et);
                final Spinner ContactedBy = view.findViewById(R.id.contactedBySpinner);


                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                        R.array.names, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ContactedBy.setAdapter(adapter);

                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
                    concerns.setText(null);
                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                    concerns.setText(null);
                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns() == null) {
                    concerns.setText(null);
                } else {
                    concerns.setText(round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns());
                }

                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
                    comments.setText(null);
                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                    comments.setText(null);
                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments() == null) {
                    comments.setText(null);
                } else {
                    comments.setText(round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
                }

                if (round.getCustomerApplied().get(position).getIsContacted() != null) {

                    if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {

                    } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy() == null) {

                    } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy().equalsIgnoreCase("Karim")) {
                        ContactedBy.setSelection(2);
                    } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy().equalsIgnoreCase("Yousef")) {
                        ContactedBy.setSelection(1);
                    }
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Confirm Contact")
                        .setView(view).setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String concern = concerns.getText().toString();
                                String comment = comments.getText().toString();

                                if (comment.equals("") || comment.equals(null)) {
                                    comment = null;
                                }
                                if (concern.equals("") || concern.equals(null)) {
                                    concern = null;
                                }

                                String nameOfContacted = ContactedBy.getSelectedItem().toString();

                                isContactInfo isContactInfo;
                                if (nameOfContacted.equals("Select a Name")) {
                                    nameOfContacted = null;
                                }
                                isContactInfo = new isContactInfo(nameOfContacted, concern, comment);


                                Payment payment;
                                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
                                    payment = null;
                                } else if (round.getCustomerApplied().get(position).getPayment() == null) {
                                    payment = null;
                                } else {
                                    payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
                                }

                                Customer customer = new Customer(isContactInfo,
                                        payment,
                                        round.getCustomerApplied().get(position).getEmail(),
                                        round.getCustomerApplied().get(position).getName(),
                                        round.getCustomerApplied().get(position).getAppliedAt(),
                                        round.getCustomerApplied().get(position).getPaymentStatus(),
                                        round.getCustomerApplied().get(position).getPhoneNumber(),
                                        round.getCustomerApplied().get(position).getAge(),
                                        round.getCustomerApplied().get(position).getAttendanceStatus(),
                                        round.getCustomerApplied().get(position).getIsEarly(),
                                        true,
                                        round.getCustomerApplied().get(position).getLearningBackground());
                                String finalNameOfContacted = nameOfContacted;
                                serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
                                    @Override
                                    public void onResponse(Call<Round> call, Response<Round> response) {
                                        round.getCustomerApplied().get(position).setIsContactInfo(isContactInfo);
                                        round.getCustomerApplied().get(position).setIsContacted(true);

                                        if (finalNameOfContacted == null) {
                                            btn_contacted_blue.setText("CONFIRMED");

                                        } else if (finalNameOfContacted.equals("Select a Name")) {
                                            btn_contacted_blue.setText("CONFIRMED");
                                        } else {
                                            btn_contacted_blue.setText(finalNameOfContacted);
                                        }
                                        Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
                                        btn_contacted_white.setVisibility(View.INVISIBLE);
                                        btn_contacted_blue.setVisibility(View.VISIBLE);

                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(Call<Round> call, Throwable t) {
                                        Log.d("error", "the error is: " + t.getMessage());
                                    }
                                });
                                dialog.dismiss();
                            }
                        }).setNegativeButton("CANCEL", null).setCancelable(false);
                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();
//
            }
        });
        btn_contacted_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(activity).inflate(R.layout.concernsdialog, null);

                final EditText concerns = view.findViewById(R.id.concerns_et);
                final EditText comments = view.findViewById(R.id.comments_et);
                final Spinner ContactedBy = view.findViewById(R.id.contactedBySpinner);


                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                        R.array.names, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                ContactedBy.setAdapter(adapter);

                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
                    concerns.setText(null);
                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                    concerns.setText(null);
                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns() == null) {
                    concerns.setText(null);
                } else {
                    concerns.setText(round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns());
                }

                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
                    comments.setText(null);
                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                    comments.setText(null);
                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments() == null) {
                    comments.setText(null);
                } else {
                    comments.setText(round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
                }
                if (round.getCustomerApplied().get(position).getIsContacted() != null) {

                    if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {

                    } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy() == null) {

                    } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy().equalsIgnoreCase("Karim")) {
                        ContactedBy.setSelection(2);
                    } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy().equalsIgnoreCase("Yousef")) {
                        ContactedBy.setSelection(1);
                    }
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Confirm Contact")
                        .setView(view).setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String concern = concerns.getText().toString();
                                String comment = comments.getText().toString();

                                if (comment.equals("") || comment.equals(null)) {
                                    comment = null;
                                }
                                if (concern.equals("") || concern.equals(null)) {
                                    concern = null;
                                }

                                String nameOfContacted = ContactedBy.getSelectedItem().toString();

                                isContactInfo isContactInfo;
                                if (nameOfContacted.equals("Select a Name")) {
                                    nameOfContacted = null;
                                }
                                isContactInfo = new isContactInfo(nameOfContacted, concern, comment);


                                Payment payment;
                                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
                                    payment = null;
                                } else if (round.getCustomerApplied().get(position).getPayment() == null) {
                                    payment = null;
                                } else {
                                    payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
                                }

                                Customer customer = new Customer(isContactInfo,
                                        payment,
                                        round.getCustomerApplied().get(position).getEmail(),
                                        round.getCustomerApplied().get(position).getName(),
                                        round.getCustomerApplied().get(position).getAppliedAt(),
                                        round.getCustomerApplied().get(position).getPaymentStatus(),
                                        round.getCustomerApplied().get(position).getPhoneNumber(),
                                        round.getCustomerApplied().get(position).getAge(),
                                        round.getCustomerApplied().get(position).getAttendanceStatus(),
                                        round.getCustomerApplied().get(position).getIsEarly(),
                                        true,
                                        round.getCustomerApplied().get(position).getLearningBackground());

                                String finalNameOfContacted = nameOfContacted;

                                serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
                                    @Override
                                    public void onResponse(Call<Round> call, Response<Round> response) {
                                        round.getCustomerApplied().get(position).setIsContactInfo(isContactInfo);
                                        round.getCustomerApplied().get(position).setIsContacted(true);


                                        if (finalNameOfContacted == null) {
                                            btn_contacted_blue.setText("CONFIRMED");
                                        } else if (finalNameOfContacted.equals("Select a Name")) {
                                            btn_contacted_blue.setText("CONFIRMED");
                                        } else {
                                            btn_contacted_blue.setText(finalNameOfContacted);
                                        }


                                        btn_contacted_white.setVisibility(View.INVISIBLE);
                                        btn_contacted_blue.setVisibility(View.VISIBLE);
                                        notifyDataSetChanged();
                                        Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Round> call, Throwable t) {
                                        Log.d("error", "the error is: " + t.getMessage());
                                    }
                                });
                                dialog.dismiss();
                            }
                        }).setNegativeButton("CANCEL", null).setCancelable(false);
                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();

            }
        });


        btn_payment_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(activity).inflate(R.layout.paymentdialog, null);

                final EditText Amount = view.findViewById(R.id.paymentAmount_et);
                final Button whiteDate = view.findViewById(R.id.paymentDate_btn);
                final Button blueDate = view.findViewById(R.id.paymentDate_btn1);
                final Spinner PaymentMethodSp = view.findViewById(R.id.PaymentMethodSpinner);
                final String[] sentDate = new String[1];
                final String[] finalDate = new String[1];


                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
                    whiteDate.setVisibility(View.VISIBLE);
                    blueDate.setVisibility(View.INVISIBLE);
                } else {
                    whiteDate.setVisibility(View.INVISIBLE);
                    blueDate.setVisibility(View.VISIBLE);
                    if (round.getCustomerApplied().get(position).getPayment().getPaymentDate().length() == 8) {
                        blueDate.setText(round.getCustomerApplied().get(position).getPayment().getPaymentDate().substring(0, 8));
                    } else if (round.getCustomerApplied().get(position).getPayment().getPaymentDate().length() == 9) {
                        blueDate.setText(round.getCustomerApplied().get(position).getPayment().getPaymentDate().substring(0, 9));
                    } else {
                        blueDate.setText(round.getCustomerApplied().get(position).getPayment().getPaymentDate().substring(0, 10));
                    }

                }


                whiteDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                finalDate[0] = dayOfMonth + "-" + month + "-" + year;
                                Toast.makeText(activity, "Date chosen: " + finalDate[0], Toast.LENGTH_SHORT).show();
                                whiteDate.setVisibility(View.INVISIBLE);
                                blueDate.setVisibility(View.VISIBLE);
                                blueDate.setText(finalDate[0]);

                                sentDate[0] = year + "/" + month + "/" + (dayOfMonth + 1);

                            }
                        };
                    }

                });
                blueDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                finalDate[0] = dayOfMonth + "/" + month + "/" + year;
                                Toast.makeText(activity, "Date chosen: " + finalDate[0], Toast.LENGTH_SHORT).show();
                                whiteDate.setVisibility(View.INVISIBLE);
                                blueDate.setVisibility(View.VISIBLE);
                                blueDate.setText(finalDate[0]);
                                sentDate[0] = year + "/" + month + "/" + (dayOfMonth + 1);

                            }
                        };
                    }
                });
                /*dates buttons*/

                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
                    Amount.setText(null);
                } else {
                    Amount.setText(round.getCustomerApplied().get(position).getPayment().getPaymentAmount());
                }


                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("CONFIRMED")) {
                    if (round.getCustomerApplied().get(position).getPayment() == null) {
                        types.add(0, "--Select--");
                        types.add(1, "PayPal");
                        types.add(2, "Aman");
                        types.add(3, "VodafoneCash");
                        types.add(4, "Cash");
                        types.add(5, "CreditCard");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod() == null) {
                        types.add(0, "--Select--");
                        types.add(1, "PayPal");
                        types.add(2, "Aman");
                        types.add(3, "VodafoneCash");
                        types.add(4, "Cash");
                        types.add(5, "CreditCard");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("VodafoneCash")) {
                        types.add(0, "VodafoneCash");
                        types.add(1, "Cash");
                        types.add(2, "Aman");
                        types.add(3, "CreditCard");
                        types.add(4, "PayPal");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("Aman")) {
                        types.add(0, "Aman");
                        types.add(1, "Cash");
                        types.add(2, "VodafoneCash");
                        types.add(3, "CreditCard");
                        types.add(4, "PayPal");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("Cash")) {
                        types.add(0, "Cash");
                        types.add(1, "Aman");
                        types.add(2, "VodafoneCash");
                        types.add(3, "CreditCard");
                        types.add(4, "PayPal");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("CreditCard")) {
                        types.add(0, "CreditCard");
                        types.add(1, "Aman");
                        types.add(2, "VodafoneCash");
                        types.add(3, "Cash");
                        types.add(4, "PayPal");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("PayPal")) {
                        types.add(0, "PayPal");
                        types.add(1, "Aman");
                        types.add(2, "VodafoneCash");
                        types.add(3, "Cash");
                        types.add(4, "CreditCard");
                    }

                } else {
                    types.add(0, "--Select--");
                    types.add(1, "PayPal");
                    types.add(2, "Aman");
                    types.add(3, "VodafoneCash");
                    types.add(4, "Cash");
                    types.add(5, "CreditCard");
                }
                /*types.add*/

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, types);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                PaymentMethodSp.setAdapter(arrayAdapter);


                final AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setView(view)
                        .setTitle("Confirm Payment")
                        .setPositiveButton("CONFIRM", null) //Set to null. We override the onclick
                        .setNegativeButton("CANCEL", null)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String paymentAmount = Amount.getText().toString();
                                String paymentMethod = PaymentMethodSp.getSelectedItem().toString();

                                isContactInfo isContactInfo;
                                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
                                    isContactInfo = null;
                                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                                    isContactInfo = null;
                                } else {
                                    isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
                                }

                                String paymentStatus = paymentAmount;
                                Payment payment;

                                String datefinal = sentDate[0];

                                payment = new Payment(paymentAmount, datefinal, paymentMethod);

                                Customer customer = new Customer(isContactInfo,
                                        payment,
                                        round.getCustomerApplied().get(position).getEmail(),
                                        round.getCustomerApplied().get(position).getName(),
                                        round.getCustomerApplied().get(position).getAppliedAt(),
                                        "CONFIRMED",
                                        round.getCustomerApplied().get(position).getPhoneNumber(),
                                        round.getCustomerApplied().get(position).getAge(),
                                        round.getCustomerApplied().get(position).getAttendanceStatus(),
                                        round.getCustomerApplied().get(position).getIsEarly(),
                                        round.getCustomerApplied().get(position).getIsContacted(),
                                        round.getCustomerApplied().get(position).getLearningBackground());

                                if (payment.getPaymentAmount() == null || payment.getPaymentAmount().equalsIgnoreCase("")) {
                                    Toast.makeText(activity, "Please make sure of Payment Amount", Toast.LENGTH_SHORT).show();
                                } else if (payment.getPaymentDate() == null || payment.getPaymentDate().equalsIgnoreCase("")) {
                                    Toast.makeText(activity, "Please make sure of Payment Date", Toast.LENGTH_SHORT).show();
                                } else if (payment.getcollectionMethod() == null || payment.getcollectionMethod().equalsIgnoreCase("--select--")) {
                                    Toast.makeText(activity, "Please make sure of collection method", Toast.LENGTH_SHORT).show();
                                } else {
                                    serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
                                        @Override
                                        public void onResponse(Call<Round> call, Response<Round> response) {
                                            round.getCustomerApplied().get(position).setPaymentStatus("CONFIRMED");
                                            payment.setPaymentDate(finalDate[0]);
                                            round.getCustomerApplied().get(position).setPayment(payment);

                                            blueDate.setText(payment.getPaymentDate());
                                            btn_payment_white.setVisibility(View.INVISIBLE);
                                            btn_payment_blue.setVisibility(View.VISIBLE);
                                            btn_payment_blue.setText(payment.getPaymentAmount());
                                            notifyDataSetChanged();
                                            Toast.makeText(activity, "Change was made successfully", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Round> call, Throwable t) {
                                            Log.d("error", "the error is: " + t.getMessage());
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();

            }
        });
        btn_payment_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(activity).inflate(R.layout.paymentdialog, null);

                final EditText Amount = view.findViewById(R.id.paymentAmount_et);
                final Button whiteDate = view.findViewById(R.id.paymentDate_btn);
                final Button blueDate = view.findViewById(R.id.paymentDate_btn1);
                final Spinner PaymentMethodSp = view.findViewById(R.id.PaymentMethodSpinner);
                final String[] sentDate = new String[1];
                final String[] finalDate = new String[1];


                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
                    whiteDate.setVisibility(View.VISIBLE);
                    blueDate.setVisibility(View.INVISIBLE);
                } else {
                    whiteDate.setVisibility(View.INVISIBLE);
                    blueDate.setVisibility(View.VISIBLE);
                    if (round.getCustomerApplied().get(position).getPayment().getPaymentDate().length() == 8) {
                        blueDate.setText(round.getCustomerApplied().get(position).getPayment().getPaymentDate().substring(0, 8));
                    } else if (round.getCustomerApplied().get(position).getPayment().getPaymentDate().length() == 9) {
                        blueDate.setText(round.getCustomerApplied().get(position).getPayment().getPaymentDate().substring(0, 9));
                    } else {
                        blueDate.setText(round.getCustomerApplied().get(position).getPayment().getPaymentDate().substring(0, 10));
                    }

                }


                whiteDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                finalDate[0] = dayOfMonth + "-" + month + "-" + year;
                                Toast.makeText(activity, "Date chosen: " + finalDate[0], Toast.LENGTH_SHORT).show();
                                whiteDate.setVisibility(View.INVISIBLE);
                                blueDate.setVisibility(View.VISIBLE);
                                blueDate.setText(finalDate[0]);

                                sentDate[0] = year + "/" + month + "/" + (dayOfMonth + 1);

                            }
                        };
                    }

                });
                blueDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                finalDate[0] = dayOfMonth + "/" + month + "/" + year;
                                Toast.makeText(activity, "Date chosen: " + finalDate[0], Toast.LENGTH_SHORT).show();
                                whiteDate.setVisibility(View.INVISIBLE);
                                blueDate.setVisibility(View.VISIBLE);
                                blueDate.setText(finalDate[0]);
                                sentDate[0] = year + "/" + month + "/" + (dayOfMonth + 1);

                            }
                        };
                    }
                });
                /*dates buttons*/

                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
                    Amount.setText(null);
                } else {
                    Amount.setText(round.getCustomerApplied().get(position).getPayment().getPaymentAmount());
                }


                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("CONFIRMED")) {
                    if (round.getCustomerApplied().get(position).getPayment() == null) {
                        types.add(0, "--Select--");
                        types.add(1, "PayPal");
                        types.add(2, "Aman");
                        types.add(3, "VodafoneCash");
                        types.add(4, "Cash");
                        types.add(5, "CreditCard");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod() == null) {
                        types.add(0, "--Select--");
                        types.add(1, "PayPal");
                        types.add(2, "Aman");
                        types.add(3, "VodafoneCash");
                        types.add(4, "Cash");
                        types.add(5, "CreditCard");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("VodafoneCash")) {
                        types.add(0, "VodafoneCash");
                        types.add(1, "Cash");
                        types.add(2, "Aman");
                        types.add(3, "CreditCard");
                        types.add(4, "PayPal");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("Aman")) {
                        types.add(0, "Aman");
                        types.add(1, "Cash");
                        types.add(2, "VodafoneCash");
                        types.add(3, "CreditCard");
                        types.add(4, "PayPal");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("Cash")) {
                        types.add(0, "Cash");
                        types.add(1, "Aman");
                        types.add(2, "VodafoneCash");
                        types.add(3, "CreditCard");
                        types.add(4, "PayPal");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("CreditCard")) {
                        types.add(0, "CreditCard");
                        types.add(1, "Aman");
                        types.add(2, "VodafoneCash");
                        types.add(3, "Cash");
                        types.add(4, "PayPal");
                    } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("PayPal")) {
                        types.add(0, "PayPal");
                        types.add(1, "Aman");
                        types.add(2, "VodafoneCash");
                        types.add(3, "Cash");
                        types.add(4, "CreditCard");
                    }

                } else {
                    types.add(0, "--Select--");
                    types.add(1, "PayPal");
                    types.add(2, "Aman");
                    types.add(3, "VodafoneCash");
                    types.add(4, "Cash");
                    types.add(5, "CreditCard");
                }
                /*types.add*/

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, types);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                PaymentMethodSp.setAdapter(arrayAdapter);


                final AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setView(view)
                        .setTitle("Confirm Payment")
                        .setPositiveButton("CONFIRM", null) //Set to null. We override the onclick
                        .setNegativeButton("CANCEL", null)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String paymentAmount = Amount.getText().toString();
                                String paymentMethod = PaymentMethodSp.getSelectedItem().toString();

                                isContactInfo isContactInfo;
                                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
                                    isContactInfo = null;
                                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                                    isContactInfo = null;
                                } else {
                                    isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
                                }

                                String paymentStatus = paymentAmount;
                                Payment payment;

                                String datefinal = sentDate[0];

                                payment = new Payment(paymentAmount, datefinal, paymentMethod);

                                Customer customer = new Customer(isContactInfo,
                                        payment,
                                        round.getCustomerApplied().get(position).getEmail(),
                                        round.getCustomerApplied().get(position).getName(),
                                        round.getCustomerApplied().get(position).getAppliedAt(),
                                        "CONFIRMED",
                                        round.getCustomerApplied().get(position).getPhoneNumber(),
                                        round.getCustomerApplied().get(position).getAge(),
                                        round.getCustomerApplied().get(position).getAttendanceStatus(),
                                        round.getCustomerApplied().get(position).getIsEarly(),
                                        round.getCustomerApplied().get(position).getIsContacted(),
                                        round.getCustomerApplied().get(position).getLearningBackground());

                                if (payment.getPaymentAmount() == null || payment.getPaymentAmount().equalsIgnoreCase("")) {
                                    Toast.makeText(activity, "Please make sure of Payment Amount", Toast.LENGTH_SHORT).show();
                                } else if (payment.getPaymentDate() == null || payment.getPaymentDate().equalsIgnoreCase("")) {
                                    Toast.makeText(activity, "Please make sure of Payment Date", Toast.LENGTH_SHORT).show();
                                } else if (payment.getcollectionMethod() == null || payment.getcollectionMethod().equalsIgnoreCase("--select--")) {
                                    Toast.makeText(activity, "Please make sure of collection method", Toast.LENGTH_SHORT).show();
                                } else {
                                    serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
                                        @Override
                                        public void onResponse(Call<Round> call, Response<Round> response) {
                                            round.getCustomerApplied().get(position).setPaymentStatus("CONFIRMED");
                                            payment.setPaymentDate(finalDate[0]);
                                            round.getCustomerApplied().get(position).setPayment(payment);

                                            blueDate.setText(payment.getPaymentDate());
                                            btn_payment_white.setVisibility(View.INVISIBLE);
                                            btn_payment_blue.setVisibility(View.VISIBLE);
                                            btn_payment_blue.setText(payment.getPaymentAmount());
                                            notifyDataSetChanged();
                                            Toast.makeText(activity, "Change was made successfully", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Round> call, Throwable t) {
                                            Log.d("error", "the error is: " + t.getMessage());
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();

            }
        });


        btn_Attendance_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isContacted;
                isContactInfo isContactInfo;
                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
                    isContacted = null;
                    isContactInfo = null;
                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                    isContacted = true;
                    isContactInfo = null;
                } else {
                    isContacted = true;
                    isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
                }

                Payment payment;
                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
                    payment = null;
                } else {
                    if (round.getCustomerApplied().get(position).getPayment() == null) {
                        payment = null;
                    } else
                        payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
                }
                Customer customer = new Customer(isContactInfo,
                        payment,
                        round.getCustomerApplied().get(position).getEmail(),
                        round.getCustomerApplied().get(position).getName(),
                        round.getCustomerApplied().get(position).getAppliedAt() + "",
                        round.getCustomerApplied().get(position).getPaymentStatus() + "",
                        round.getCustomerApplied().get(position).getPhoneNumber() + "",
                        round.getCustomerApplied().get(position).getAge() + "",
                        "RUNNING",
                        round.getCustomerApplied().get(position).getIsEarly() + "",
                        isContacted,
                        round.getCustomerApplied().get(position).getLearningBackground() + "");
                serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
                    @Override
                    public void onResponse(Call<Round> call, Response<Round> response) {
                        btn_Attendance_blue.setText("CONFIRMED");
                        btn_Attendance_white.setVisibility(View.INVISIBLE);
                        btn_Attendance_blue.setVisibility(View.VISIBLE);
                        Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();

                        round.getCustomerApplied().get(position).setAttendanceStatus("RUNNING");
                    }

                    @Override
                    public void onFailure(Call<Round> call, Throwable t) {
                        Toast.makeText(activity, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        btn_Attendance_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isContacted;
                isContactInfo isContactInfo;
                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
                    isContacted = null;
                    isContactInfo = null;
                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                    isContacted = true;
                    isContactInfo = null;
                } else {
                    isContacted = true;
                    isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
                }

                Payment payment;
                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
                    payment = null;
                } else {
                    if (round.getCustomerApplied().get(position).getPayment() == null) {
                        payment = null;
                    } else
                        payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
                }
                Customer customer = new Customer(isContactInfo,
                        payment,
                        round.getCustomerApplied().get(position).getEmail(),
                        round.getCustomerApplied().get(position).getName(),
                        round.getCustomerApplied().get(position).getAppliedAt() + "",
                        round.getCustomerApplied().get(position).getPaymentStatus() + "",
                        round.getCustomerApplied().get(position).getPhoneNumber() + "",
                        round.getCustomerApplied().get(position).getAge() + "",
                        "APPLIED",
                        round.getCustomerApplied().get(position).getIsEarly() + "",
                        isContacted,
                        round.getCustomerApplied().get(position).getLearningBackground() + "");
                serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
                    @Override
                    public void onResponse(Call<Round> call, Response<Round> response) {
                        btn_Attendance_white.setText("ATTENDANCE");
                        btn_Attendance_white.setVisibility(View.VISIBLE);
                        btn_Attendance_blue.setVisibility(View.INVISIBLE);

                        Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
                        round.getCustomerApplied().get(position).setAttendanceStatus("RUNNING");
                    }

                    @Override
                    public void onFailure(Call<Round> call, Throwable t) {
                        Toast.makeText(activity, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });//null object contacted by

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                }
            }
        });
        btn_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                }
            }
        });

        btn_editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(activity).inflate(R.layout.edit_data_dialog, null);
                final EditText age = view.findViewById(R.id.age_editdata);
                final EditText email = view.findViewById(R.id.email_editdata);
                final EditText phone = view.findViewById(R.id.phone_editdata);


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Edit Data")
                        .setView(view).setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editedAge = age.getText().toString();
                        String editedPhone = phone.getText().toString();
                        String editedEmail = email.getText().toString();

                        Boolean isContacted;
                        isContactInfo isContactInfo;
                        if (round.getCustomerApplied().get(position).getIsContacted() == null) {
                            isContacted = null;
                            isContactInfo = null;
                        } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
                            isContacted = true;
                            isContactInfo = null;
                        } else {
                            isContacted = true;
                            isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
                        }

                        Payment payment;
                        if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
                            payment = null;
                        } else {
                            if (round.getCustomerApplied().get(position).getPayment() == null) {
                                payment = null;
                            } else
                                payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
                        }

                        if (editedAge == null || editedAge.equals("")) {
                            if (round.getCustomerApplied().get(position).getAge() != null) {
                                editedAge = round.getCustomerApplied().get(position).getAge();
                            } else
                                editedAge = null;
                        }

                        if (editedEmail == null || editedEmail.equals("")) {
                            if (round.getCustomerApplied().get(position).getEmail() != null) {
                                editedEmail = round.getCustomerApplied().get(position).getEmail();
                            } else {
                                editedEmail = null;
                            }
                        }

                        if (editedPhone == null || editedPhone.equals("")) {
                            if (round.getCustomerApplied().get(position).getPhoneNumber() != null) {
                                editedPhone = round.getCustomerApplied().get(position).getPhoneNumber();
                            } else {
                                editedPhone = null;
                            }
                        }

                        Customer customer = new Customer(isContactInfo,
                                payment,
                                editedEmail + "",
                                round.getCustomerApplied().get(position).getName() + "",
                                round.getCustomerApplied().get(position).getAppliedAt() + "",
                                round.getCustomerApplied().get(position).getPaymentStatus() + "",
                                editedPhone + "",
                                editedAge + "",
                                round.getCustomerApplied().get(position).getAttendanceStatus() + "",
                                round.getCustomerApplied().get(position).getIsEarly() + "",
                                isContacted,
                                round.getCustomerApplied().get(position).getLearningBackground() + "");
                        String finalEditedAge = editedAge;
                        String finalEditedEmail = editedEmail;
                        String finalEditedPhone = editedPhone;
                        serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
                            @Override
                            public void onResponse(Call<Round> call, Response<Round> response) {
                                round.getCustomerApplied().get(position).setAge(finalEditedAge);
                                round.getCustomerApplied().get(position).setEmail(finalEditedEmail);
                                round.getCustomerApplied().get(position).setPhoneNumber(finalEditedPhone);
                                Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Round> call, Throwable t) {
                                Toast.makeText(activity, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("CANCEL", null).setCancelable(false);


                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();

            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] clicked = {false};
                Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.calldialog);

                dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity, background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                Button btn_call_dialog = dialog.findViewById(R.id.call_customer_dialog);
                Button btn_whatsapp = dialog.findViewById(R.id.whatsapp_customer_dialog);

                dialog.show();
                btn_whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String number = round.getCustomerApplied().get(position).getPhoneNumber();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com//send?phone=+2" + number + "&text=" + "Hello, Courzerve"));
                        activity.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                btn_call_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + round.getCustomerApplied().get(position).getPhoneNumber()));
                        activity.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.setCanceledOnTouchOutside(true);
            }

        });//to call

        tv_age.setText(round.getCustomerApplied().get(position).getAge());
        tv_email.setText(round.getCustomerApplied().get(position).getEmail());
        tv_number.setText(round.getCustomerApplied().get(position).getPhoneNumber() + "");
        tv_customerAppliedAt.setText("Applied At: " + round.getCustomerApplied().get(position).getAppliedAt().substring(0, 10));
        tv_customerName.setText(round.getCustomerApplied().get(position).getName());
        return convertView;


//        collectionMethodSp.setAdapter(adapter);

//        btn_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//
//                onDateSetListener = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        month = month + 1;
//                        dayOfMonth = dayOfMonth;
//
//
//                        isContactInfo isContactInfo;
//                        if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                            isContactInfo = new isContactInfo(null, null, null);
//                        } else {
//                            isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                        }
//
//                        Payment payment;
//                        Payment payment1;
//                        payment1 = new Payment("", null, null);
//                        if (round.getCustomerApplied().get(position).getPaymentStatus().equals("CONFIRMED")) {
//                            if (round.getCustomerApplied().get(position).getPayment() == null) {
//                                round.getCustomerApplied().get(position).setPayment(payment1);
//                            }
//                            payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), year + "/" + month + "/" + (dayOfMonth + 1), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
//                        } else {
//                            payment = null;
//                        }
//                        Customer customer = new Customer(isContactInfo,
//                                payment,
//                                round.getCustomerApplied().get(position).getEmail(),
//                                round.getCustomerApplied().get(position).getName(),
//                                round.getCustomerApplied().get(position).getAppliedAt() + "",
//                                round.getCustomerApplied().get(position).getPaymentStatus() + "",
//                                round.getCustomerApplied().get(position).getPhoneNumber() + "",
//                                round.getCustomerApplied().get(position).getAge() + "",
//                                round.getCustomerApplied().get(position).getAttendanceStatus() + "",
//                                round.getCustomerApplied().get(position).getIsEarly() + "",
//                                round.getCustomerApplied().get(position).getIsContacted(),
//                                round.getCustomerApplied().get(position).getLearningBackground() + "");
//                        int finalDayOfMonth = dayOfMonth;
//                        int finalMonth = month;
//                        serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                            @Override
//                            public void onResponse(Call<Round> call, Response<Round> response) {
//                                Log.d("not error ", "did enter");
//                                round.getCustomerApplied().get(position).setPayment(payment);
//                                PaymentDate.setText(finalDayOfMonth + "/" + finalMonth + "/" + year);
//                            }
//
//                            @Override
//                            public void onFailure(Call<Round> call, Throwable t) {
//                                Log.d("error ", "not enter");
//                            }
//                        });
//                    }
//                };
//            }
//        });
        /*Date*/
//        ContactedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    if ((round.getCustomerApplied().get(position).getIsContactInfo() == null)) {
//                        btn_yousef.setVisibility(View.VISIBLE);
//                        btn_karim.setVisibility(View.VISIBLE);
//                    } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy().equals("Karim")) {
//                        btn_karim.setVisibility(View.VISIBLE);
//                        btn_karim.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.Blue));
//                        btn_yousef.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.white));
//                    } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy().equals("Yousef")) {
//                        btn_yousef.setVisibility(View.VISIBLE);
//                        btn_yousef.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.Blue));
//                        btn_karim.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.white));
//                    }
//                    btn_concerns.setVisibility(View.VISIBLE);
//                    ContactedCB.setChecked(true);
//
//
//                    isContactInfo isContactInfo;
//                    if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                        isContactInfo = new isContactInfo("", null, null);
//                    } else {
//                        isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                    }
//
//                    Payment payment;
//                    if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
//                        payment = null;
//                    } else {
//                        payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
//                    }
//
//                    Customer customer = new Customer(isContactInfo,
//                            payment,
//                            round.getCustomerApplied().get(position).getEmail() + "",
//                            round.getCustomerApplied().get(position).getName() + "",
//                            round.getCustomerApplied().get(position).getAppliedAt() + "",
//                            round.getCustomerApplied().get(position).getPaymentStatus() + "",
//                            round.getCustomerApplied().get(position).getPhoneNumber() + "",
//                            round.getCustomerApplied().get(position).getAge() + "",
//                            round.getCustomerApplied().get(position).getAttendanceStatus() + "",
//                            round.getCustomerApplied().get(position).getIsEarly() + "",
//                            true,
//                            round.getCustomerApplied().get(position).getLearningBackground() + "");
//
//                    serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                        @Override
//                        public void onResponse(Call<Round> call, Response<Round> response) {
//                            Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
//                            round.getCustomerApplied().get(position).setIsContacted(true);
//                            btn_yousef.setVisibility(View.VISIBLE);
//                            btn_karim.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onFailure(Call<Round> call, Throwable t) {
//                            Log.d("error", "the error is: " + t.getMessage());
//                        }
//                    });
//
//                } else {
//                    btn_concerns.setVisibility(View.INVISIBLE);
//                    btn_yousef.setVisibility(View.INVISIBLE);
//                    btn_karim.setVisibility(View.INVISIBLE);
//                    Payment payment;
//                    if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
//                        payment = null;
//                    } else if (round.getCustomerApplied().get(position).getPayment() == null) {
//                        payment = null;
//                    } else {
//                        payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
//                    }
//                    Customer customer = new Customer(null,
//                            payment,
//                            round.getCustomerApplied().get(position).getEmail(),
//                            round.getCustomerApplied().get(position).getName(),
//                            round.getCustomerApplied().get(position).getAppliedAt() + "",
//                            round.getCustomerApplied().get(position).getPaymentStatus() + "",
//                            round.getCustomerApplied().get(position).getPhoneNumber() + "",
//                            round.getCustomerApplied().get(position).getAge() + "",
//                            round.getCustomerApplied().get(position).getAttendanceStatus() + "",
//                            round.getCustomerApplied().get(position).getIsEarly() + "",
//                            null,
//                            round.getCustomerApplied().get(position).getLearningBackground() + "");
//                    ContactedCB.setChecked(false);
//                    serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                        @Override
//                        public void onResponse(Call<Round> call, Response<Round> response) {
//                            Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
//                            round.getCustomerApplied().get(position).setIsContacted(null);
//                        }
//
//                        @Override
//                        public void onFailure(Call<Round> call, Throwable t) {
//                            Log.d("error", "the error is: " + t.getMessage());
//                        }
//                    });
//                }
//            }
//        });
        /*Contacted checkbox*/
//        btn_yousef.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_yousef.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.Blue));
//                btn_karim.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.white));
//                isContactInfo isContactInfo;
//                if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                    isContactInfo = new isContactInfo("Yousef", null, null);
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns() == null && round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments() == null) {
//                    isContactInfo = new isContactInfo("Yousef", null, null);
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns() == null) {
//                    isContactInfo = new isContactInfo("Yousef", null, round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments() == null) {
//                    isContactInfo = new isContactInfo("Yousef", round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), null);
//                } else {
//                    isContactInfo = new isContactInfo("Yousef", round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                }
//                Payment payment;
//                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
//                    payment = null;
//                } else if (round.getCustomerApplied().get(position).getPayment() == null) {
//                    payment = null;
//                } else {
//                    payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
//                }
//                Customer customer = new Customer(isContactInfo,
//                        payment,
//                        round.getCustomerApplied().get(position).getEmail(),
//                        round.getCustomerApplied().get(position).getName(),
//                        round.getCustomerApplied().get(position).getAppliedAt() + "",
//                        round.getCustomerApplied().get(position).getPaymentStatus() + "",
//                        round.getCustomerApplied().get(position).getPhoneNumber() + "",
//                        round.getCustomerApplied().get(position).getAge() + "",
//                        round.getCustomerApplied().get(position).getAttendanceStatus() + "",
//                        round.getCustomerApplied().get(position).getIsEarly() + "",
//                        true,
//                        round.getCustomerApplied().get(position).getLearningBackground() + "");
//                serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                    @Override
//                    public void onResponse(Call<Round> call, Response<Round> response) {
//                        Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
//                        round.getCustomerApplied().get(position).setIsContactInfo(isContactInfo);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Round> call, Throwable t) {
//                        Log.d("error", "the error is: " + t.getMessage());
//                    }
//                });
//
//            }
//        });
//        btn_karim.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_karim.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.Blue));
//                btn_yousef.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.white));
//                isContactInfo isContactInfo;
//                if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                    isContactInfo = new isContactInfo("Karim", null, null);
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns() == null && round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments() == null) {
//                    isContactInfo = new isContactInfo("Karim", null, null);
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns() == null) {
//                    isContactInfo = new isContactInfo("Karim", null, round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments() == null) {
//                    isContactInfo = new isContactInfo("Karim", round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), null);
//                } else {
//                    isContactInfo = new isContactInfo("Karim", round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                }
//                Payment payment;
//                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
//                    payment = null;
//                } else if (round.getCustomerApplied().get(position).getPayment() == null) {
//                    payment = null;
//                } else {
//                    payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
//                }
//                Customer customer = new Customer(isContactInfo,
//                        payment,
//                        round.getCustomerApplied().get(position).getEmail(),
//                        round.getCustomerApplied().get(position).getName(),
//                        round.getCustomerApplied().get(position).getAppliedAt() + "",
//                        round.getCustomerApplied().get(position).getPaymentStatus() + "",
//                        round.getCustomerApplied().get(position).getPhoneNumber() + "",
//                        round.getCustomerApplied().get(position).getAge() + "",
//                        round.getCustomerApplied().get(position).getAttendanceStatus() + "",
//                        round.getCustomerApplied().get(position).getIsEarly() + "",
//                        true,
//                        round.getCustomerApplied().get(position).getLearningBackground() + "");
//                serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                    @Override
//                    public void onResponse(Call<Round> call, Response<Round> response) {
//                        Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
//                        round.getCustomerApplied().get(position).setIsContactInfo(isContactInfo);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Round> call, Throwable t) {
//                        Log.d("error", "the error is: " + t.getMessage());
//                    }
//                });
//            }
//        });
        /*btn yousef and karim*/
//        btn_concerns.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View view = LayoutInflater.from(activity).inflate(R.layout.concernsdialog, null);
//
//                final EditText concerns = view.findViewById(R.id.concerns_et);
//                final EditText comments = view.findViewById(R.id.comments_et);
//                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
//                    concerns.setText(null);
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                    concerns.setText(null);
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns() == null) {
//                    concerns.setText(null);
//                } else {
//                    concerns.setText(round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns());
//                }
//
//                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
//                    comments.setText(null);
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                    comments.setText(null);
//                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments() == null) {
//                    comments.setText(null);
//                } else {
//                    comments.setText(round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                }
//
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
//                alertDialog.setTitle("Concerns & Comments")
//                        .setView(view).setPositiveButton("Change",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                String concern = concerns.getText().toString();
//                                String comment = comments.getText().toString();
//
//                                if (comment.equals("") || comment.equals(null)) {
//                                    comment = null;
//                                }
//                                if (concern.equals("") || concern.equals(null)) {
//                                    concern = null;
//                                }
//
//                                isContactInfo isContactInfo;
//                                if (round.getCustomerApplied().get(position).getIsContacted() == null) {
//                                    isContactInfo = null;
//                                } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                                    isContactInfo = new isContactInfo(null, concern, comment);
//                                } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy() == null) {
//                                    isContactInfo = new isContactInfo(null, concern, comment);
//                                } else
//                                    isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), concern, comment);
//
//                                Payment payment;
//                                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
//                                    payment = null;
//                                } else {
//                                    payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
//                                }
//                                Customer customer = new Customer(isContactInfo,
//                                        payment,
//                                        round.getCustomerApplied().get(position).getEmail(),
//                                        round.getCustomerApplied().get(position).getName(),
//                                        round.getCustomerApplied().get(position).getAppliedAt(),
//                                        round.getCustomerApplied().get(position).getPaymentStatus(),
//                                        round.getCustomerApplied().get(position).getPhoneNumber(),
//                                        round.getCustomerApplied().get(position).getAge(),
//                                        round.getCustomerApplied().get(position).getAttendanceStatus(),
//                                        round.getCustomerApplied().get(position).getIsEarly(),
//                                        round.getCustomerApplied().get(position).getIsContacted(),
//                                        round.getCustomerApplied().get(position).getLearningBackground());
//                                String finalComment = comment;
//                                String finalConcern = concern;
//                                serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                                    @Override
//                                    public void onResponse(Call<Round> call, Response<Round> response) {
//                                        Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
//                                        round.getCustomerApplied().get(position).setIsContactInfo(isContactInfo);
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<Round> call, Throwable t) {
//                                        Log.d("error", "the error is: " + t.getMessage());
//                                    }
//                                });
//                                dialog.dismiss();
//                            }
//                        }).setNegativeButton("BACK", null).setCancelable(false);
//                AlertDialog alertDialog1 = alertDialog.create();
//                alertDialog1.show();
//
//            }
//        });
        /*btn concerns*/

//        PaymentStatusCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    btn_date.setVisibility(View.VISIBLE);
//                    collectionMethodSp.setVisibility(View.VISIBLE);
//                    PaymentDate.setVisibility(View.VISIBLE);
//                    PaymentAmount.setVisibility(View.VISIBLE);
//                    MoneyImage.setVisibility(View.VISIBLE);
//
//                    isContactInfo isContactInfo;
//
//                    if (round.getCustomerApplied().get(position).getIsContacted() == null) {
//                        isContactInfo = null;
//                    } else if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                        isContactInfo = null;
//                    } else {
//                        isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                    }
//
//                    Payment payment;
//                    if (round.getCustomerApplied().get(position).getPaymentStatus().equals("PENDING_PAYMENT")) {
//                        payment = null;
//                    } else {
//                        if (round.getCustomerApplied().get(position).getPayment() == null) {
//                            payment = null;
//                        } else {
//                            payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
//                        }
//                    }
//                    Customer customer = new Customer(isContactInfo,
//                            payment,
//                            round.getCustomerApplied().get(position).getEmail(),
//                            round.getCustomerApplied().get(position).getName(),
//                            round.getCustomerApplied().get(position).getAppliedAt() + "",
//                            "CONFIRMED",
//                            round.getCustomerApplied().get(position).getPhoneNumber() + "",
//                            round.getCustomerApplied().get(position).getAge() + "",
//                            round.getCustomerApplied().get(position).getAttendanceStatus() + "",
//                            round.getCustomerApplied().get(position).getIsEarly() + "",
//                            round.getCustomerApplied().get(position).getIsContacted(),
//                            round.getCustomerApplied().get(position).getLearningBackground() + "");
//                    serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                        @Override
//                        public void onResponse(Call<Round> call, Response<Round> response) {
//                            Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
//                            round.getCustomerApplied().get(position).setPaymentStatus("CONFIRMED");
//                        }
//
//                        @Override
//                        public void onFailure(Call<Round> call, Throwable t) {
//                            Toast.makeText(activity, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                } else {
//                    btn_date.setVisibility(View.INVISIBLE);
//                    PaymentDate.setVisibility(View.INVISIBLE);
//                    collectionMethodSp.setVisibility(View.GONE);
//                    PaymentAmount.setVisibility(View.INVISIBLE);
//                    MoneyImage.setVisibility(View.INVISIBLE);
//
//                    isContactInfo isContactInfo;
//                    if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                        isContactInfo = null;
//                    } else {
//                        isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                    }
//
//                    Customer customer = new Customer(isContactInfo,
//                            null,
//                            round.getCustomerApplied().get(position).getEmail(),
//                            round.getCustomerApplied().get(position).getName(),
//                            round.getCustomerApplied().get(position).getAppliedAt() + "",
//                            "PENDING_PAYMENT",
//                            round.getCustomerApplied().get(position).getPhoneNumber() + "",
//                            round.getCustomerApplied().get(position).getAge() + "",
//                            round.getCustomerApplied().get(position).getAttendanceStatus() + "",
//                            round.getCustomerApplied().get(position).getIsEarly() + "",
//                            round.getCustomerApplied().get(position).getIsContacted(),
//                            round.getCustomerApplied().get(position).getLearningBackground() + "");
//                    serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                        @Override
//                        public void onResponse(Call<Round> call, Response<Round> response) {
//                            Toast.makeText(activity, "Change was successfully made", Toast.LENGTH_SHORT).show();
//                            round.getCustomerApplied().get(position).setPaymentStatus("PENDING_PAYMENT");
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<Round> call, Throwable t) {
//                            Toast.makeText(activity, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
        /*paymentstatuscheckbox*/

//        collectionMethodSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                String text = parent.getItemAtPosition(pos).toString();
//
//                isContactInfo isContactInfo;
//                if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                    isContactInfo = new isContactInfo(null, null, null);
//                } else {
//                    isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                }
//
//                Payment payment;
//                Payment payment1;
//                payment1 = new Payment("", null, text);
//                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("CONFIRMED")) {
//                    if (round.getCustomerApplied().get(position).getPayment() == null) {
//                        payment = payment1;
//                        round.getCustomerApplied().get(position).setPayment(payment1);
//                    } else
//                        payment = new Payment(round.getCustomerApplied().get(position).getPayment().getPaymentAmount(), round.getCustomerApplied().get(position).getPayment().getPaymentDate(), text);
//                } else {
//                    payment = null;
//                }
//                Customer customer = new Customer(isContactInfo,
//                        payment,
//                        round.getCustomerApplied().get(position).getEmail(),
//                        round.getCustomerApplied().get(position).getName(),
//                        round.getCustomerApplied().get(position).getAppliedAt() + "",
//                        round.getCustomerApplied().get(position).getPaymentStatus() + "",
//                        round.getCustomerApplied().get(position).getPhoneNumber() + "",
//                        round.getCustomerApplied().get(position).getAge() + "",
//                        round.getCustomerApplied().get(position).getAttendanceStatus() + "",
//                        round.getCustomerApplied().get(position).getIsEarly() + "",
//                        round.getCustomerApplied().get(position).getIsContacted(),
//                        round.getCustomerApplied().get(position).getLearningBackground() + "");
//                serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                    @Override
//                    public void onResponse(Call<Round> call, Response<Round> response) {
//                        round.getCustomerApplied().get(position).setPayment(payment);
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<Round> call, Throwable t) {
//                    }
//                });
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        /*collectionMethodSpinner*/

//        PaymentAmount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String et = null;
//                if (PaymentAmount.getText().toString().equals("")) {
//                    et = null;
//                } else if (!PaymentAmount.getText().toString().equals("")) {
//                    et = PaymentAmount.getText().toString();
//                }
//
//
//                isContactInfo isContactInfo;
//
//                if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//                    isContactInfo = new isContactInfo(null, null, null);
//                } else {
//                    isContactInfo = new isContactInfo(round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy(), round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns(), round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments());
//                }
//
//                Payment payment;
//                Payment payment1;
//                payment1 = new Payment(et, null, null);
//                if (round.getCustomerApplied().get(position).getPaymentStatus().equals("CONFIRMED")) {
//
//                    if (round.getCustomerApplied().get(position).getPayment() == null) {
//                        payment = payment1;
//                        round.getCustomerApplied().get(position).setPayment(payment1);
//                    }
//                    payment = new Payment(et, round.getCustomerApplied().get(position).getPayment().getPaymentDate(), round.getCustomerApplied().get(position).getPayment().getcollectionMethod());
//                } else {
//                    payment = null;
//                }
//
//                Customer customer = new Customer(isContactInfo,
//                        payment,
//                        round.getCustomerApplied().get(position).getEmail(),
//                        round.getCustomerApplied().get(position).getName(),
//                        round.getCustomerApplied().get(position).getAppliedAt() + "",
//                        "CONFIRMED",
//                        round.getCustomerApplied().get(position).getPhoneNumber() + "",
//                        round.getCustomerApplied().get(position).getAge() + "",
//                        round.getCustomerApplied().get(position).getAttendanceStatus() + "",
//                        round.getCustomerApplied().get(position).getIsEarly() + "",
//                        round.getCustomerApplied().get(position).getIsContacted(),
//                        round.getCustomerApplied().get(position).getLearningBackground() + "");
//                String finalEt = et;
//                serviceRound.putRound(roundId + "", "application/json", customer).enqueue(new Callback<Round>() {
//                    @Override
//                    public void onResponse(Call<Round> call, Response<Round> response) {
//                        round.getCustomerApplied().get(position).getPayment().setPaymentAmount(finalEt);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Round> call, Throwable t) {
//                        Log.d("error", "the error is: " + t.getMessage());
//                    }
//                });
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        PaymentAmount.setFocusable(true);
//
//        tv_customerAge.setText(round.getCustomerApplied().get(position).getAge() + " Years Old");
        /*PaymentAmount*/

        //        if (round.getCustomerApplied().get(position).getIsContacted() == null) {
//            btn_karim.setVisibility(View.INVISIBLE);
//            btn_yousef.setVisibility(View.INVISIBLE);
//            btn_concerns.setVisibility(View.INVISIBLE);
//            ContactedCB.setChecked(false);
//        } else if (round.getCustomerApplied().get(position).getIsContacted() == true) {
//            ContactedCB.setChecked(true);
//            btn_concerns.setVisibility(View.VISIBLE);
//            if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//
//            } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy() == null) {
//
//            } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy().equals("Karim")) {
//                btn_karim.setVisibility(View.VISIBLE);
//                btn_karim.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.Blue));
//                btn_yousef.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.white));
//            }
//            if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//            } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy() == null) {
//
//            } else if (round.getCustomerApplied().get(position).getIsContactInfo().getContactedBy().equals("Yousef")) {
//                btn_yousef.setVisibility(View.VISIBLE);
//                btn_yousef.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.Blue));
//                btn_karim.setBackgroundColor(finalConvertView.getContext().getResources().getColor(R.color.white));
//            }
//        }

//        if (round.getCustomerApplied().get(position).getPaymentStatus().equals("CONFIRMED")) {
//            if (round.getCustomerApplied().get(position).getPayment() == null) {
//                types.add(0, "--Select--");
//                types.add(1, "PayPal");
//                types.add(2, "Aman");
//                types.add(3, "VodafoneCash");
//                types.add(4, "Cash");
//                types.add(5, "CreditCard");
//            } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod() == null) {
//                types.add(0, "--Select--");
//                types.add(1, "PayPal");
//                types.add(2, "Aman");
//                types.add(3, "VodafoneCash");
//                types.add(4, "Cash");
//                types.add(5, "CreditCard");
//            } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("VodafoneCash")) {
//                types.add(0, "VodafoneCash");
//                types.add(1, "Cash");
//                types.add(2, "Aman");
//                types.add(3, "CreditCard");
//                types.add(4, "PayPal");
//            } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("Aman")) {
//                types.add(0, "Aman");
//                types.add(1, "Cash");
//                types.add(2, "VodafoneCash");
//                types.add(3, "CreditCard");
//                types.add(4, "PayPal");
//            } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("Cash")) {
//                types.add(0, "Cash");
//                types.add(1, "Aman");
//                types.add(2, "VodafoneCash");
//                types.add(3, "CreditCard");
//                types.add(4, "PayPal");
//            } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("CreditCard")) {
//                types.add(0, "CreditCard");
//                types.add(1, "Aman");
//                types.add(2, "VodafoneCash");
//                types.add(3, "Cash");
//                types.add(4, "PayPal");
//            } else if (round.getCustomerApplied().get(position).getPayment().getcollectionMethod().equals("PayPal")) {
//                types.add(0, "PayPal");
//                types.add(1, "Aman");
//                types.add(2, "VodafoneCash");
//                types.add(3, "Cash");
//                types.add(4, "CreditCard");
//            }
//
//        } else {
//            types.add(0, "--Select--");
//            types.add(1, "PayPal");
//            types.add(2, "Aman");
//            types.add(3, "VodafoneCash");
//            types.add(4, "Cash");
//            types.add(5, "CreditCard");
//        }

//        if (round.getCustomerApplied().get(position).getPaymentStatus().equals("CONFIRMED")) {
//            PaymentDate.setVisibility(View.VISIBLE);
//            btn_date.setVisibility(View.VISIBLE);
//            PaymentAmount.setVisibility(View.VISIBLE);
//            MoneyImage.setVisibility(View.VISIBLE);
//            collectionMethodSp.setVisibility(View.VISIBLE);
//            PaymentStatusCB.setChecked(true);
//
//            if (round.getCustomerApplied().get(position).getPayment() == null) {
//                PaymentAmount.setHint("--Amount--");
//            } else if (round.getCustomerApplied().get(position).getPayment().getPaymentAmount() == null) {
//                PaymentAmount.setHint("--Amount--");
//            } else {
//                PaymentAmount.setText(round.getCustomerApplied().get(position).getPayment().getPaymentAmount() + "");
//            }
//
//            if (round.getCustomerApplied().get(position).getPayment() == null) {
//                PaymentDate.setText("Select date");
//            } else if (round.getCustomerApplied().get(position).getPayment().getPaymentDate() == null) {
//                PaymentDate.setText("Select date");
//            } else if (round.getCustomerApplied().get(position).getPayment().getPaymentDate().length() < 10) {
//                PaymentDate.setText(round.getCustomerApplied().get(position).getPayment().getPaymentDate().substring(0, 8) + "");
//            } else {
//                PaymentDate.setText(round.getCustomerApplied().get(position).getPayment().getPaymentDate().substring(0, 10) + "");
//            }
//
//
//        } else {
//            PaymentStatusCB.setChecked(false);
//            btn_date.setVisibility(View.INVISIBLE);
//            collectionMethodSp.setVisibility(View.GONE);
//            PaymentDate.setVisibility(View.INVISIBLE);
//            PaymentAmount.setVisibility(View.INVISIBLE);
//            MoneyImage.setVisibility(View.INVISIBLE);
//        } // law dafa3 yban kol haga
//
//        if (round.getCustomerApplied().get(position).getIsContactInfo() == null) {
//            btn_concerns.setBackground(finalConvertView.getContext().getResources().getDrawable(R.drawable.concerns_empty));
//        } else if ((round.getCustomerApplied().get(position).getIsContactInfo().getAdminComments() == null) && (round.getCustomerApplied().get(position).getIsContactInfo().getCustomerConcerns() == null)) {
//            btn_concerns.setBackground(finalConvertView.getContext().getResources().getDrawable(R.drawable.concerns_empty));
//        }
    }
}
