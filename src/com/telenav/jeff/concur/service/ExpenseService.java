package com.telenav.jeff.concur.service;

import java.util.List;

import android.util.Log;

import com.telenav.jeff.concur.vo.ExpenseReport;

public class ExpenseService extends ConcurService
{
    private static final String URI_EXPENSE_REPORT_GET = "https://www.concursolutions.com/api/expense/expensereport/v2.0/Reports";
    
    private static final String LOG_TAG = "ExpenseService";
    
    public List<ExpenseReport> getExpenseReports()
    {
        byte[] data = get(URI_EXPENSE_REPORT_GET);
        
        Log.d(LOG_TAG, new String(data));
        
        return null;
    }

}
