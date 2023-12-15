package app.nover.clothingstore.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class StatusCart   {
    String id;
    String idCheckoutItem;
    List<String> arrayIdItem;
    String name;
    String address;
    String phoneNumber;
    String dateCreateAt;
    String timeCreateAt;
    String payment;
    String statusCode;
    String total;
    int timestampCreateAt;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCheckoutItem() {
        return idCheckoutItem;
    }

    public void setIdCheckoutItem(String idCheckoutItem) {
        this.idCheckoutItem = idCheckoutItem;
    }

    public List<String> getArrayIdItem() {
        return arrayIdItem;
    }

    public void setArrayIdItem(List<String> arrayIdItem) {
        this.arrayIdItem = arrayIdItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateCreateAt() {
        return dateCreateAt;
    }

    public void setDateCreateAt(String dateCreateAt) {
        this.dateCreateAt = dateCreateAt;
    }

    public String getTimeCreateAt() {
        return timeCreateAt;
    }

    public void setTimeCreateAt(String timeCreateAt) {
        this.timeCreateAt = timeCreateAt;
    }

    public int getTimestampCreateAt() {
        return timestampCreateAt;
    }

    public void setTimestampCreateAt(int timestampCreateAt) {
        this.timestampCreateAt = timestampCreateAt;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }



}

