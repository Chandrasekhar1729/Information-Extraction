package com.workfusion.lab.final_assignment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test_chandra {


    public static void main(String[] args) {


        String n = ".*[0-9].*";
        String a = ".*[A-Z].*";
        String INVOICE_NUMBER_REGEX = "\\d";
        String ele_value = "1243563894";
        Pattern pattern = Pattern.compile(INVOICE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(ele_value);
        if (ele_value.matches(n) && ele_value.matches(a)) {
            System.out.println(ele_value);
            System.out.println("hello");
        }
        else if (matcher.find() & ele_value.length()>5) {
            System.out.println("dscsd");
        }

    }
}