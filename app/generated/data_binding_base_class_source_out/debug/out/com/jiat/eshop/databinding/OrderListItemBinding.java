// Generated by view binder compiler. Do not edit!
package com.jiat.eshop.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.jiat.eshop.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class OrderListItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView orderItemOrderDate;

  @NonNull
  public final TextView orderItemOrderDeliveryStatus;

  @NonNull
  public final TextView orderItemOrderId;

  @NonNull
  public final TextView orderItemOrderPaymentStatus;

  @NonNull
  public final TextView orderItemOrderPrice;

  private OrderListItemBinding(@NonNull LinearLayout rootView, @NonNull TextView orderItemOrderDate,
      @NonNull TextView orderItemOrderDeliveryStatus, @NonNull TextView orderItemOrderId,
      @NonNull TextView orderItemOrderPaymentStatus, @NonNull TextView orderItemOrderPrice) {
    this.rootView = rootView;
    this.orderItemOrderDate = orderItemOrderDate;
    this.orderItemOrderDeliveryStatus = orderItemOrderDeliveryStatus;
    this.orderItemOrderId = orderItemOrderId;
    this.orderItemOrderPaymentStatus = orderItemOrderPaymentStatus;
    this.orderItemOrderPrice = orderItemOrderPrice;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static OrderListItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static OrderListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.order_list_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static OrderListItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.order_item_order_date;
      TextView orderItemOrderDate = ViewBindings.findChildViewById(rootView, id);
      if (orderItemOrderDate == null) {
        break missingId;
      }

      id = R.id.order_item_order_delivery_status;
      TextView orderItemOrderDeliveryStatus = ViewBindings.findChildViewById(rootView, id);
      if (orderItemOrderDeliveryStatus == null) {
        break missingId;
      }

      id = R.id.order_item_order_id;
      TextView orderItemOrderId = ViewBindings.findChildViewById(rootView, id);
      if (orderItemOrderId == null) {
        break missingId;
      }

      id = R.id.order_item_order_payment_status;
      TextView orderItemOrderPaymentStatus = ViewBindings.findChildViewById(rootView, id);
      if (orderItemOrderPaymentStatus == null) {
        break missingId;
      }

      id = R.id.order_item_order_price;
      TextView orderItemOrderPrice = ViewBindings.findChildViewById(rootView, id);
      if (orderItemOrderPrice == null) {
        break missingId;
      }

      return new OrderListItemBinding((LinearLayout) rootView, orderItemOrderDate,
          orderItemOrderDeliveryStatus, orderItemOrderId, orderItemOrderPaymentStatus,
          orderItemOrderPrice);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}