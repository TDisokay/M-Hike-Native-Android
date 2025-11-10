package com.duongnguyen.m_hike.helpers;

import android.content.Context;
import android.widget.Toast;

public class ValidationHelper {
    public static boolean validateHikeInput(Context context, String name, String location, String date, String lengthStr) {
        if (name == null || name.trim().isEmpty()) {
            Toast.makeText(context, "Please enter a hike name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (location == null || location.trim().isEmpty()) {
            Toast.makeText(context, "Please enter a location", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (date == null || date.trim().isEmpty()) {
            Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (lengthStr == null || lengthStr.trim().isEmpty()) {
            Toast.makeText(context, "Please enter a length", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double length = Double.parseDouble(lengthStr);
            if (length <= 0) {
                Toast.makeText(context, "Length must be greater than 0", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (length > 2000) {
                Toast.makeText(context, "Length seems unreasonably high", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Please enter a valid number for length", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
